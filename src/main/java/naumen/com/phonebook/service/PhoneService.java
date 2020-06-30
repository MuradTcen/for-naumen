package naumen.com.phonebook.service;

import naumen.com.phonebook.dto.PhoneDto;
import naumen.com.phonebook.dto.PhoneFilterAndPagination;
import naumen.com.phonebook.entity.Phone;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PhoneService {
    void deleteById(Long id);

    Optional<Phone> update(PhoneDto phoneDto);

    Phone add(PhoneDto phoneDto);

    Page<PhoneDto> getAll(PhoneFilterAndPagination phoneFilterAndPagination);

    Optional<Phone> getById(Long id);

    void dump();

    void deleteOldPhones();
}
