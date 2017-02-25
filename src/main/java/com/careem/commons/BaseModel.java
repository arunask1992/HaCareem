package com.careem.commons;

import com.google.common.collect.HashMultimap;
import com.careem.exception.RecordNotFoundException;
import com.careem.exception.ValidationFailedException;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableMap.of;

@MappedSuperclass
@EntityListeners(BaseModel.Listener.class)
public abstract class BaseModel<T extends BaseModel<T>> {

    /**
     * Special key for adding errors that are not specific to any field.
     */
    protected static String BASE_ERROR_KEY = "base";
    @Transient
    protected HashMultimap<String, String> errors = HashMultimap.<String, String>create();

    protected static EntityManager entityManager() {
        return BeanUtil.getBean(EntityManager.class);
    }

    /**
     * Returns a QueryBuilder for querying this model.
     * <p>
     * FIXME Use generic T and remove the need for passing the class?
     */
    public static <T extends BaseModel<T>> QueryBuilder<T> query(Class<T> klass) {
        return new QueryBuilder<>(klass, entityManager());
    }

    public boolean alreadySaved() {
        return getId() != null;
    }

    public abstract Long getId();

    @SuppressWarnings("unchecked")
    public T persist() {
        if (!isValid()) {
            throw new ValidationFailedException(getErrors());
        }

        entityManager().persist(this);
        return (T) this;
    }


    public void flush() {
        entityManager().flush();
    }

    @SuppressWarnings("unchecked")
    public T reload() {
        entityManager().refresh(this);
        return (T) this;
    }

    public Map<String, Collection<String>> getErrors() {
        return errors.asMap();
    }

    public boolean isValid() {
        validateModel();
        return errors.isEmpty();
    }

    public void delete() {
        entityManager().remove(this);
    }

    /**
     * Override to provide custom validations. Any validation error must be added to {@code errors}.
     */
    protected void validate() {
    }

    protected void beforeSave() {
    }

    protected void beforeCreate() {
    }

    /**
     * Returns true if this is an unsaved record.
     */
    protected boolean isNewRecord() {
        return getId() == null;
    }

    private void validateModel() {
        errors.clear();
        getValidator().validate(this).stream().forEach(violation -> {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        });

        validate();
    }

    private Validator getValidator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    // TODO Extract creation of criteria query
    public static class Accessor<T> {
        private Class<T> klass;

        public Accessor(Class<T> klass) {
            this.klass = klass;
        }

        public Optional<T> findByColumn(String fieldName, Object value) {
            return findByColumns(of(fieldName, value));
        }

        public List<T> findAllByColumn(String fieldName, Object value) {
            return findAllByColumns(of(fieldName, value));
        }

        public Optional<T> findByColumns(Map<String, Object> columnValueMap) {
            try {
                return Optional.of(queryByColumns(columnValueMap).getSingleResult());
            } catch (NoResultException e) {
                return Optional.empty();
            }
        }

        public List<T> findAllByColumns(Map<String, Object> columnValueMap) {
            return queryByColumns(columnValueMap).getResultList();
        }

        public PaginatedList<T> paginateAll(int pageSize) {
            CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(klass);
            Root<T> root = criteriaQuery.from(klass);
            CriteriaQuery<T> query = criteriaQuery.select(root);
            return new PaginatedResultList<>(entityManager().createQuery(query), count(), pageSize);
        }

        private TypedQuery<T> queryByColumns(Map<String, Object> columnValueMap) {
            return entityManager().createQuery(findByColumnsQuery(columnValueMap));

        }

        public CriteriaQuery<T> findByColumnsQuery(Map<String, Object> columnValueMap) {
            CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();

            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(klass);
            Root<T> root = criteriaQuery.from(klass);


            List<Predicate> predicates = columnValueMap.keySet().stream()
                    .map(key -> criteriaBuilder.equal(root.get(key), columnValueMap.get(key))).collect(Collectors.toList());


            return criteriaQuery
                    .select(root)
                    .where(predicates.toArray(new Predicate[columnValueMap.size()]));
        }

        public Optional<T> find(Long primaryKey) {
            return Optional.ofNullable(entityManager().find(klass, primaryKey));
        }

        public Object selectOneBySql(String sql) {
            return entityManager().createNativeQuery(sql).getResultList().get(0);
        }

        public T findIt(Long primaryKey) {
            return find(primaryKey).orElseThrow(() -> new RecordNotFoundException(String.format("Entity %s with id %s not found", klass.getSimpleName(), primaryKey)));
        }

        public List<T> findIn(String columnName, List<?> values) {
            CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();

            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(klass);
            Root<T> root = criteriaQuery.from(klass);

            CriteriaQuery<T> query = criteriaQuery.select(root)
                    .where(
                            root.get(columnName).in(values)
                    );

            return entityManager().createQuery(query).getResultList();
        }

        public void batchUpdate(List<T> instances) {
            EntityManager entityManager = entityManager();
            instances.stream().forEach(entityManager::merge);
            entityManager.flush();
        }

        public List<T> all() {
            CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();

            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(klass);
            Root<T> root = criteriaQuery.from(klass);

            CriteriaQuery<T> query = criteriaQuery.select(root);

            return entityManager().createQuery(query).getResultList();
        }

        public void deleteAll() {
            entityManager().createQuery("DELETE FROM " + klass.getSimpleName()).executeUpdate();
        }

        public void deleteAll(String where) {
            entityManager().createQuery("DELETE FROM " + klass.getSimpleName() + " WHERE " + where).executeUpdate();
        }

        public long count() {
            CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();

            CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
            Root<T> root = criteriaQuery.from(klass);

            CriteriaQuery<Long> query = criteriaQuery.select(criteriaBuilder.count(root));

            return entityManager().createQuery(query).getSingleResult();
        }

        public Boolean exists(String fieldName, Object value) {
            CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
            CriteriaQuery<Boolean> criteriaQuery = criteriaBuilder.createQuery(Boolean.class);

            Subquery<T> criteriaSubQuery = criteriaQuery.subquery(klass);
            Root<T> root = criteriaSubQuery.from(klass);
            criteriaSubQuery.select(root).where(criteriaBuilder
                    .equal(root.get(fieldName), value)
            );

            criteriaQuery.select(criteriaBuilder.exists(criteriaSubQuery));
            return entityManager().createQuery(criteriaQuery).getSingleResult();

        }
    }

    public static class Listener {
        @PreUpdate
        public <T extends BaseModel<T>> void beforeSave(final T entity) {
            entity.beforeSave();
        }

        @PrePersist
        public <T extends BaseModel<T>> void beforeCreate(final T entity) {
            entity.beforeCreate();
            entity.beforeSave();
        }

    }
}

