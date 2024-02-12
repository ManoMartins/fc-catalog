package com.full.admin.catalog.application.category.delete

import category.CategoryGateway
import category.CategoryID

class DefaultDeleteCategoryUseCase(private val categoryGateway: CategoryGateway) : DeleteCategoryUseCase() {
    override fun execute(anId: String) {
        this.categoryGateway.deleteById(CategoryID.from(anId))
    }
}