package com.pryabykh.vkstat.core.db.repositories;

import com.pryabykh.vkstat.core.db.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUserId(Long id);
}
