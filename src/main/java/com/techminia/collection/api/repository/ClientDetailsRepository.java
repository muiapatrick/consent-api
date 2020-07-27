package com.techminia.collection.api.repository;

import com.techminia.collection.api.domain.OauthClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientDetailsRepository extends JpaRepository<OauthClientDetails, Long>, JpaSpecificationExecutor<OauthClientDetails> {
    OauthClientDetails findByClientId(String clientid);
}
