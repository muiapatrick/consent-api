package com.techminia.collection.api.repository;

import com.techminia.collection.api.domain.PasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordToken, Integer>, JpaSpecificationExecutor<PasswordToken> {
}
