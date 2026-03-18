package com.example.english.Service.Implement;

import com.example.english.Entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> filterUser(String username, String fullName, String email) {
        return (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            if (username != null && !username.trim().isEmpty()) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + username.toLowerCase() + "%"));
            }

            if (fullName != null && !fullName.trim().isEmpty()) {
                String kw = "%" + fullName.trim().toLowerCase() + "%";

                // first + " " + last
                var firstLast = criteriaBuilder.lower(
                        criteriaBuilder.concat(
                                criteriaBuilder.concat(root.get("firstName"), " "),
                                root.get("lastName")
                        )
                );

                // last + " " + first (để hỗ trợ nhập ngược)
                var lastFirst = criteriaBuilder.lower(
                        criteriaBuilder.concat(
                                criteriaBuilder.concat(root.get("lastName"), " "),
                                root.get("firstName")
                        )
                );

                predicates = criteriaBuilder.and(predicates, criteriaBuilder.or(
                        criteriaBuilder.like(firstLast, kw),
                        criteriaBuilder.like(lastFirst, kw)
                ));
            }

            if (email != null && !email.trim().isEmpty()) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }

            return predicates;
        };
    }
}
