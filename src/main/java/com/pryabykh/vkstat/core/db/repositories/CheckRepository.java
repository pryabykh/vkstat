package com.pryabykh.vkstat.core.db.repositories;

import com.pryabykh.vkstat.core.db.Check;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckRepository extends JpaRepository<Check, Long> {
    Page<Check> findByOwnerIdAndVkId(Long ownerId, int vkId, Pageable pageable);
}
