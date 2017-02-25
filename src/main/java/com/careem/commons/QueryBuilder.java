package com.careem.commons;

import lombok.AllArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class QueryBuilder<T extends BaseModel<T>> {
    private final Class<T> klass;

    private final EntityManager entityManager;

    private final List<Filter> filters = new ArrayList<>();

    private final List<Order> orders = newArrayList();

    @AllArgsConstructor
    private class Order {
        public String fieldName;

        public boolean isAscending;
    }

    @AllArgsConstructor
    private class Filter {
        public String fieldName;

        public Object value;
    }

    public QueryBuilder(Class<T> klass, EntityManager entityManager) {
        this.klass = klass;
        this.entityManager = entityManager;
    }

    /**
     * Adds a criteria that the field with the name {@code fieldName} equals the value {@code value}.
     *
     * TODO Support inequality checks
     */
    public QueryBuilder<T> where(String fieldName, Object value) {
        filters.add(new Filter(fieldName, value));
        return this;
    }

    public QueryBuilder<T> orderBy(String field, boolean isAscending) {
        orders.add(new Order(field, isAscending));
        return this;
    }

    public int count() {
        return getList().size();
    }

    /**
     * Executes a query with the predicates and returns the record set.
     */
    public List<T> getList() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(klass);
        Root<T> variableRoot = query.from(klass);

        query = query.select(variableRoot);

        for (Filter filter : filters) {
            query = query.where(criteriaBuilder.equal(variableRoot.get(filter.fieldName), filter.value));
        }

        for (Order order : orders) {
            if (order.isAscending) {
                query.orderBy(criteriaBuilder.asc(variableRoot.get(order.fieldName)));
            } else {
                query.orderBy(criteriaBuilder.desc(variableRoot.get(order.fieldName)));
            }
        }

        return entityManager.createQuery(query).getResultList();
    }

    /**
     * Executes a query with the predicates and returns a single record from the result set.
     */
    public T get() {
        return getList().get(0);
    }
}

