package naumen.com.phonebook.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import naumen.com.phonebook.dto.PhoneDto;
import naumen.com.phonebook.dto.PhoneFilterAndPagination;
import naumen.com.phonebook.entity.Phone;
import naumen.com.phonebook.service.PhoneService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/phone/")
@RequiredArgsConstructor
public class PhoneController {

    private final PhoneService phoneService;

    @ApiOperation(value = "Получить список телефонных номеров")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "list")
    public Page<PhoneDto> list(@RequestBody PhoneFilterAndPagination phoneFilterAndPagination) {
        return phoneService.getAll(phoneFilterAndPagination);
    }

    @ApiOperation(value = "Получить данные телефонного номера")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "{id}")
    public Phone one(@PathVariable String id, HttpServletResponse response) {
        return phoneService.getById(Long.parseLong(id))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Phone Not Found"));
    }

    @ApiOperation(value = "Обновить телефон по id")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    Phone update(@RequestBody @Valid PhoneDto phoneDto) {
        return phoneService.update(phoneDto).get();
    }

    @ApiOperation(value = "Удалить телефон")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "{id}")
    public void delete(@PathVariable String id) {
        phoneService.deleteById(Long.parseLong(id));
    }

    @ApiOperation(value = "Добавить телефон в справочник")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    Phone add(@RequestBody @Valid PhoneDto phoneDto) {
        return phoneService.add(phoneDto);
    }

    @ApiOperation(value = "Сохранить телефонный справочник в файл")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value="dump")
    void dump() {
        phoneService.dump();
    }
}
