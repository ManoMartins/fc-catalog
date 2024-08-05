package com.catalog.domain.category;

import com.catalog.domain.pagination.SearchQuery;
import com.catalog.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {
    Category create(Category aCategory);
    Optional<Category> findById(CategoryID anId);
    Category update(Category aCategory);
    void deleteById(CategoryID anId);
    Pagination<Category> findAll(SearchQuery aQuery);
}
