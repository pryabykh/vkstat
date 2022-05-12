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
@Table(name = "target_users")
public class TargetUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Генерирует СУРБД
    @Column(name = "id")
    private Long id;

    @Column(name = "vk_id")
    private int vkId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "status")
    private TargetUserStatus status;

    @Version
    @Column(name = "version")
    private int version;

    @Temporal(TemporalType.TIMESTAMP) //or @Temporal(TemporalType.DATE)
    @Column(name = "created_at", insertable = true, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP) //or @Temporal(TemporalType.DATE)
    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    void onCreate() {
        Date now = new Date();
        this.setCreatedAt(now);
        this.setUpdatedAt(now);
    }

    @PreUpdate
    void onUpdate() {
        this.setUpdatedAt(new Date());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TargetUser that = (TargetUser) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
