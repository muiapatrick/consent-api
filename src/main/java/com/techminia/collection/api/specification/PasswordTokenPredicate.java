package com.techminia.collection.api.specification;

import com.techminia.collection.api.domain.PasswordToken;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class PasswordTokenPredicate implements Specification<PasswordToken> {
    PasswordToken model;

    public PasswordTokenPredicate(PasswordToken model) {
        this.model = model;
    }

    @Override
    public Predicate toPredicate(Root<PasswordToken> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        Predicate p = cb.and();

        if (model != null) {
            if (model.getId() != null) {
                p.getExpressions().add(cb.equal(root.get("id"), model.getId()));
            }

            if (model.getToken() != null) {
                p.getExpressions().add(cb.equal(root.get("token"), model.getToken()));
            }

            if (model.getUserEmail() != null) {
                p.getExpressions().add(cb.equal(root.get("userEmail"), model.getUserEmail()));
            }
        }

        p.getExpressions().add(cb.equal(root.get("tokenUsed"), false));

        return p;
    }
}
