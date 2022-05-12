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
@Table(name = "checks")
public class Check {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Генерирует СУРБД
    @Column(name = "id")
    private Long id;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "vk_id")
    private int vkId;

    @Column(name = "status")
    private CheckStatus status;

    @Column(name = "payload")
    private String payload;

    @Temporal(TemporalType.TIMESTAMP) //or @Temporal(TemporalType.DATE)
    @Column(name = "created_at", insertable = true, updatable = false)
    private Date createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Check check = (Check) o;
        return id != null && Objects.equals(id, check.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
