package com.full.admin.catalog.application.category.retrieve.get

import category.Category
import category.CategoryID
import java.time.Instant

data class CategoryOutput(
    val id: CategoryID,
    val name: String,
    val description: String?,
    val isActive: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant?,
) {
    companion object {
        fun from(aCategory: Category): CategoryOutput {
            return CategoryOutput(
                aCategory.id,
                aCategory.name,
                aCategory.description,
                aCategory.isActive,
                aCategory.createdAt,
                aCategory.updatedAt,
                aCategory.deletedAt,
            )
        }
    }
}