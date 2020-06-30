package naumen.com.phonebook.dto.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FilterField {
    NAME("name"),
    NUMBER("number");

    private final String name;
}
