package com.catalog.application.category.create;

public record CreateCategoryCommand(
        String name,
        String description,
        Boolean isActive
) {
    public static CreateCategoryCommand with(final String aName, final String aDescription, final boolean  isActive) {
        return new CreateCategoryCommand(aName, aDescription, isActive);
    }
}
