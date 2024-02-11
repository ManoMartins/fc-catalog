package com.full.admin.catalog.application.category.create

import category.Category
import category.CategoryID

data class CreateCategoryOutput(
    val id: CategoryID,
) {
    companion object {
        fun from(aCategory: Category): CreateCategoryOutput {
            return CreateCategoryOutput(aCategory.id)
        }
    }
}
