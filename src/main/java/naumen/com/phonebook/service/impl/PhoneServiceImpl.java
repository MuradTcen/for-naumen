package naumen.com.phonebook.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naumen.com.phonebook.dto.PhoneDto;
import naumen.com.phonebook.dto.PhoneFilterAndPagination;
import naumen.com.phonebook.dto.SimplePhoneDto;
import naumen.com.phonebook.entity.Phone;
import naumen.com.phonebook.repository.PhoneRepository;
import naumen.com.phonebook.repository.specification.FilterPhoneByField;
import naumen.com.phonebook.service.PhoneService;
import naumen.com.phonebook.util.cache.LruCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class PhoneServiceImpl implements PhoneService {

    private final PhoneRepository repository;

    // Размер выбран для "демонстрации" работы дампа
    private final int SIZE = 5;
    private final String LAST_PART_OF_FILENAME = "_dump.csv";
    private LruCache cache = new LruCache(2);

    @Value("${dump.directory}")
    private String directory;

    public Optional<Phone> update(PhoneDto dto) {
        log.info(String.format("Updating phone with id {}", dto.getId()));

        Phone phone = repository.findById(dto.getId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Phone Not Found")
                );

        Optional.ofNullable(dto.getName()).ifPresent(phone::setName);
        Optional.ofNullable(dto.getNumber()).ifPresent(phone::setNumber);

        return Optional.of(phone);
    }

    @Override
    public Optional<Phone> getById(Long id) {
        Phone phone = (Phone) cache.get(id);

        if (phone != null) {
            log.info("Getting phone from cache.. " + +id);

            return Optional.of(phone);
        } else {
            log.info("Getting phone from DB.. " + id);

            Optional<Phone> phoneFromDB = repository.findById(id);
            cache.put(id, phoneFromDB.get());

            return phoneFromDB;
        }
    }

    @Override
    @Async
    public void dump() {
        try {
            log.info("Starting dumping.. ");
            CompletableFuture.runAsync(() -> {
                File csvOutputFile = new File(directory + LocalDate.now() + LAST_PART_OF_FILENAME);

                try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
                    final long count = repository.count() / SIZE;

                    for (int i = 0; i <= count; i++) {
                        Pageable page = PageRequest.of(i, SIZE);
                        repository.findAll(page)
                                .map(phone -> SimplePhoneDto.of(phone))
                                .forEach(phone -> pw.println(phone.getName() + ", " + phone.getNumber()));
                    }
                } catch (FileNotFoundException e) {
                    log.error("Error during creating file.. " + e.getMessage());
                }
            });
        } catch (Exception e) {
            log.error("Error during dumping.. " + e.getMessage());
        }
    }

    @Override
    public Phone add(PhoneDto dto) {
        log.info(String.format("Saving phone with name {} and number {}", dto.getName(), dto.getNumber()));

        Phone phone = new Phone();
        Optional.ofNullable(dto.getName()).ifPresent(phone::setName);
        Optional.ofNullable(dto.getNumber()).ifPresent(phone::setNumber);

        return repository.save(phone);
    }

    @Override
    public Page<PhoneDto> getAll(PhoneFilterAndPagination dto) {
        log.info(String.format("Getting phones.."));

        Sort sort = Sort.by("name");

        Pageable sortByField = dto.isAscending() ?
                PageRequest.of(dto.getPage(), dto.getSize(), sort.descending()) :
                PageRequest.of(dto.getPage(), dto.getSize(), sort.ascending());

        FilterPhoneByField filterPhoneByField = new FilterPhoneByField(dto.getDesiredContent(), dto.getFilterField().getName());

        return repository.findAll(filterPhoneByField, sortByField).map(phone -> PhoneDto.of(phone));
    }

    @Override
    public void deleteById(Long id) {
        log.info(String.format("Deleting phone number with id {}", id));

        repository.deleteById(id);
    }

    @Override
    @Scheduled(cron = "${cron.expression}")
    public void deleteOldPhones() {
        log.info("Deleting..  created before " + LocalDate.now().minusDays(1));
        repository.deleteByCreatedAtBefore(LocalDate.now().minusDays(1));
    }
}
