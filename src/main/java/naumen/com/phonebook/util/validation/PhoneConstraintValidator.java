package naumen.com.phonebook.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneConstraintValidator implements ConstraintValidator<PhoneConstraint, String> {
    private static final String regex = "^\\+\\d.\\d{3}><\\d{7}>$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Pattern pattern = Pattern.compile(regex);

        Matcher m = pattern.matcher(value);

        return m.matches();
    }
}
