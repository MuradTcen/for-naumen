package naumen.com.phonebook.dto;

import lombok.Data;
import naumen.com.phonebook.dto.enums.FilterField;

@Data
public class PhoneFilterAndPagination {

    private FilterField filterField = FilterField.NAME;
    private String desiredContent = "";
    private int page = 0;
    private int size = 20;
    private boolean ascending = true;
}
