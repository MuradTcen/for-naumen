package phonebook.util.validation;

import naumen.com.phonebook.dto.PhoneDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootApplication
class PhoneNumberConstraintValidatorTest {
    private Validator validator;

    @BeforeEach
    public void setUpClass() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private static PhoneDto getPhoneDto(String number) {
        return new PhoneDto(1L, "name", number);
    }

    @Test
    void isValid_whenLatinAndDigits_thenViolation() {
        PhoneDto phoneDto = getPhoneDto("AF+7<909><0952716>");
        Set<ConstraintViolation<PhoneDto>> constraintViolations = validator.validate(phoneDto);
        assertThat(constraintViolations).hasSize(1);
    }

    @Test
    void isValid_whenDigits_thenViolation() {
        PhoneDto phoneDto = getPhoneDto("+7<999><9999999>");
        Set<ConstraintViolation<PhoneDto>> constraintViolations = validator.validate(phoneDto);
        assertThat(constraintViolations).hasSize(0);
    }

    @Test
    void isValid_whenSpace_thenHasViolations() {
        PhoneDto phoneDto = getPhoneDto(" ");
        Set<ConstraintViolation<PhoneDto>> constraintViolations = validator.validate(phoneDto);
        assertThat(constraintViolations).hasSize(1);
    }

    @Test
    void isValid_whenEmpty_thenHasViolations() {
        PhoneDto phoneDto = getPhoneDto("");
        Set<ConstraintViolation<PhoneDto>> constraintViolations = validator.validate(phoneDto);
        assertThat(constraintViolations).hasSize(1);
    }
    @Test
    void isValid_whenDoubledPhoneNumber_thenHasViolations() {
        PhoneDto phoneDto = getPhoneDto("+7<999><9999999>+7<999><9999999>");
        Set<ConstraintViolation<PhoneDto>> constraintViolations = validator.validate(phoneDto);
        assertThat(constraintViolations).hasSize(1);
    }
}