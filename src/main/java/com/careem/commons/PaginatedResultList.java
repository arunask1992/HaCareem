package com.careem.commons;

import javax.persistence.TypedQuery;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


public class PaginatedResultList<T> implements PaginatedList<T> {

    private final Iterator<List<T>> iterator;

    private int pageSize = 100;

    private int currentPage = 0;

    public PaginatedResultList(TypedQuery<T> query, long totalEntries) {
        iterator = new Iterator<List<T>>() {
            @Override
            public boolean hasNext() {
                return totalEntries > currentPage * pageSize;
            }

            @Override
            public List<T> next() {
                query.setFirstResult(currentPage * pageSize);
                query.setMaxResults(pageSize);

                currentPage += 1;

                List<T> resultList = query.getResultList();
                if (resultList.size() == 0)
                    throw new NoSuchElementException(String.format("Not able to find entries for page: %s", currentPage));
                return resultList;
            }
        };
    }

    public PaginatedResultList(TypedQuery<T> query, long totalCount, int pageSize) {
        this(query, totalCount);
        this.pageSize = pageSize;
    }

    @Override
    public Iterator<List<T>> iterator() {
        return iterator;
    }
}

