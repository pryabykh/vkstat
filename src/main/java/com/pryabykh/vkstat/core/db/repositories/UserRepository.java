package com.pryabykh.vkstat.core.db.repositories;

import com.pryabykh.vkstat.core.db.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByVkId(int vkId);
}
