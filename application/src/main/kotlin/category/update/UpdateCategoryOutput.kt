package com.full.admin.catalog.application.category.update

import category.Category
import category.CategoryID

data class UpdateCategoryOutput(val id: CategoryID) {
    companion object {
        fun from(aCategory: Category): UpdateCategoryOutput {
            return UpdateCategoryOutput(aCategory.id)
        }
    }
}
