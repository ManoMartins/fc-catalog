package com.full.admin.catalog.application.category.retrieve.get

import category.CategoryGateway
import category.CategoryID
import exceptions.DomainException
import validation.Error

class DefaultGetCategoryByIdUseCase(private val categoryGateway: CategoryGateway) : GetCategoryByIdUseCase() {
    override fun execute(aCommand: String): CategoryOutput {
        val aCategoryID = CategoryID.from(aCommand)

        return this.categoryGateway.findById(aCategoryID)?.let(CategoryOutput::from) ?: throw DomainException.with(Error("Category with ID $aCommand was not found"))
    }
}