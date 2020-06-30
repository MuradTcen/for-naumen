package naumen.com.phonebook.repository.specification;

import lombok.extern.slf4j.Slf4j;
import naumen.com.phonebook.entity.Phone;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Slf4j
public class FilterPhoneByField implements Specification<Phone> {
    private String desiredContent;
    private String field;

    public FilterPhoneByField(String desiredContent, String field) {
        this.desiredContent = desiredContent;
        this.field = field;
    }

    @Override
    public Predicate toPredicate(Root<Phone> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (desiredContent.equals("")) return null;

        desiredContent = "%" + desiredContent.toLowerCase() + "%";
        log.info("field: " + field);
        log.info("desiredContent: " + desiredContent);
        return cb.like(cb.lower(root.get(field)), desiredContent);
    }
}
