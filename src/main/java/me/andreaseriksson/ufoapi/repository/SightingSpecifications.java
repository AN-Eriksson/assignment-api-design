package me.andreaseriksson.ufoapi.repository;

import me.andreaseriksson.ufoapi.dto.SightingFilter;
import me.andreaseriksson.ufoapi.entity.Sighting;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.JoinType;
import java.util.ArrayList;
import java.util.List;

public final class SightingSpecifications {

    private SightingSpecifications() {
    }

    public static Specification<Sighting> withFilter(SightingFilter filter) {
        return (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

            if (filter.city() != null && !filter.city().isBlank()) {
                predicates.add(cb.equal(cb.lower(root.join("location", JoinType.LEFT).get("city")),
                        filter.city().toLowerCase()));
            }

            if (filter.state() != null && !filter.state().isBlank()) {
                predicates.add(cb.equal(cb.lower(root.join("location", JoinType.LEFT).get("state")),
                        filter.state().toLowerCase()));
            }

            if (filter.countryCode() != null && !filter.countryCode().isBlank()) {
                predicates.add(cb.equal(
                        cb.lower(root.join("location", JoinType.LEFT)
                                .join("country", JoinType.LEFT)
                                .get("code")),
                        filter.countryCode().toLowerCase()
                ));
            }

            if (filter.shapeName() != null && !filter.shapeName().isBlank()) {
                predicates.add(cb.equal(cb.lower(root.join("shape", JoinType.LEFT).get("name")),
                        filter.shapeName().toLowerCase()));
            }

            if (filter.minDurationSeconds() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("durationSeconds"), filter.minDurationSeconds()));
            }

            if (filter.maxDurationSeconds() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("durationSeconds"), filter.maxDurationSeconds()));
            }

            if (filter.fromDatePosted() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("datePosted"), filter.fromDatePosted()));
            }

            if (filter.toDatePosted() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("datePosted"), filter.toDatePosted()));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}
