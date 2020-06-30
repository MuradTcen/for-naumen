package naumen.com.phonebook.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.io.Serializable;

@Entity(name = "phones")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
public class Phone extends BaseEntity implements Serializable {

    @Id
    private long id;
    @Column(unique = true)
    private String number;
    @Column
    private String name;
}
