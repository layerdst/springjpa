package jpabook.jpashop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Item implements Persistable<String> {

//    @Id @GeneratedValue
//    private Long id;

    /**
     * merge 를 사용하지 않고, 변경감지를 사용하기 위해 인터페이스를 구현하는 방법이다.
     */

    @Id
    private String id;

    @CreatedDate
    private LocalDateTime createDate;

    public Item(String id) {
        this.id = id;
    }

    @Override
    public boolean isNew() {
        return createDate == null;
    }
}
