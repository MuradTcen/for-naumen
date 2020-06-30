package naumen.com.phonebook.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import naumen.com.phonebook.entity.Phone;

@Data
@RequiredArgsConstructor
public class SimplePhoneDto {

    private final String name;
    private final String number;

    public static SimplePhoneDto of(Phone phone) {
        return new SimplePhoneDto(phone.getName(), phone.getNumber());
    }
}
