package com.catalog.application.category.update;

public record UpdateCategoryCommand(
        String id,
        String name,
        String description,
        Boolean isActive
) {
    public static UpdateCategoryCommand with(
            final String anId,
            final String aName,
            final String aDescription,
            final Boolean isActive
    ) {
        return new UpdateCategoryCommand(anId, aName, aDescription, isActive);
    }
}
