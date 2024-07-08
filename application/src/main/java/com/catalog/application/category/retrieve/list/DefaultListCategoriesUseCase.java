package com.catalog.application.category.retrieve.list;

import com.catalog.domain.category.CategoryGateway;
import com.catalog.domain.category.CategorySearchQuery;
import com.catalog.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase {
    private final CategoryGateway categoryGateway;

    public DefaultListCategoriesUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Pagination<CategoryListOutput> execute(final CategorySearchQuery aQuery) {
        return this.categoryGateway.findAll(aQuery).map(CategoryListOutput::from);
    }
}
