package com.pryabykh.vkstat.core.db;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "activities")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "target_user_id")
    private Long targetUserId;

    @Temporal(TemporalType.TIMESTAMP) //or @Temporal(TemporalType.DATE)
    @Column(name = "was_from")
    private Date wasFrom;

    @Temporal(TemporalType.TIMESTAMP) //or @Temporal(TemporalType.DATE)
    @Column(name = "was_to")
    private Date wasTo;

    @Column(name = "status")
    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Activity activity = (Activity) o;
        return id != null && Objects.equals(id, activity.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
