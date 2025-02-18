package org.ibtissam.dadesadventures.repository.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.ibtissam.dadesadventures.DTO.Activity.ActivitySearchDTO;
import org.ibtissam.dadesadventures.domain.entities.Activity;
import org.ibtissam.dadesadventures.repository.ActivitySearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ActivitySearchRepositoryImpl implements ActivitySearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Activity> findByCriteria(ActivitySearchDTO searchDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Activity> cq = cb.createQuery(Activity.class);
        Root<Activity> activity = cq.from(Activity.class);
        List<Predicate> predicates = new ArrayList<>();

        if (searchDTO.getName() != null && !searchDTO.getName().isEmpty()) {
            predicates.add(cb.like(cb.lower(activity.get("name")), "%" + searchDTO.getName().toLowerCase() + "%"));
        }
        if (searchDTO.getCategoryId() != null) {
            predicates.add(cb.equal(activity.get("category").get("id"), searchDTO.getCategoryId()));
        }
        if (searchDTO.getPlaceId() != null) {
            predicates.add(cb.equal(activity.get("place").get("id"), searchDTO.getPlaceId()));
        }
        if (searchDTO.getStartDate() != null && searchDTO.getEndDate() != null) {
            predicates.add(cb.between(activity.get("date"), searchDTO.getStartDate(), searchDTO.getEndDate()));
        } else if (searchDTO.getStartDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(activity.get("date"), searchDTO.getStartDate()));
        } else if (searchDTO.getEndDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(activity.get("date"), searchDTO.getEndDate()));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(activity.get("date")));

        // Execute query and return result list
        return entityManager.createQuery(cq).getResultList();
    }
}
