package naumen.com.phonebook.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import naumen.com.phonebook.entity.Phone;
import naumen.com.phonebook.util.validation.PhoneConstraint;

@Data
@RequiredArgsConstructor
public class PhoneDto {

    private final Long id;
    private final String name;
    @PhoneConstraint
    private final String number;

    public static PhoneDto of(Phone phone) {
        return new PhoneDto(phone.getId(), phone.getName(), phone.getNumber());
    }
}
