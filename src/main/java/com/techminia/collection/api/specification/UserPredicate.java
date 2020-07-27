package com.techminia.collection.api.specification;

import com.techminia.collection.api.domain.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class UserPredicate implements Specification<User> {
    User model;
    String fullName;

    public UserPredicate(User model, String fullName) {
        this.model = model;
        this.fullName = fullName;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        Predicate p = cb.and();

        if (model != null) {
            if (model.getId() != null) {
                p.getExpressions().add(cb.equal(root.get("id"), model.getId()));
            }

            if (model.getUsername() != null && !model.getUsername().trim().isEmpty()) {
                p.getExpressions().add(cb.equal(root.get("username"), model.getUsername()));
            }

            if (model.getPhoneNumber() != null && !model.getPhoneNumber().trim().isEmpty()) {
                p.getExpressions().add(cb.equal(root.get("phoneNumber"), model.getPhoneNumber()));
            }

        }

        if (fullName != null && !fullName.trim().isEmpty()) {
            Expression<String> nameExpression = cb.upper(cb.concat(cb.concat(root.get("firstName"), " "), root.get("lastName")));
            p.getExpressions().add(cb.like(nameExpression, "%"+fullName.trim().toUpperCase()+"%"));
        }

        p.getExpressions().add(cb.equal(root.get("deleted"), false));

        return p;
    }
}
