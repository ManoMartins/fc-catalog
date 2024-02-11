package com.full.admin.catalog.application.category.create

import arrow.core.Either
import arrow.core.toEitherNel
import category.Category
import category.CategoryGateway
import validation.handler.Notification

class DefaultCreateCategoryUseCase(private val categoryGateway: CategoryGateway) : CreateCategoryUseCase() {
    override fun execute(aCommand: CreateCategoryCommand): Either<Notification, CreateCategoryOutput> {
        val aName = aCommand.name
        val aDescription = aCommand.description
        val isActive = aCommand.isActive

        val notification = Notification.create()

        val aCategory = Category.newCategory(aName, aDescription, isActive)
        aCategory.validate(notification)

        return if (notification.hasError()) Either.Left(notification) else create(aCategory)
    }

    private fun create(aCategory: Category): Either<Notification, CreateCategoryOutput> {
        return Either.catch {
            this.categoryGateway.create(aCategory)
        }
            .toEitherNel()
            .mapLeft { Notification.create(it.extract()) }
            .map { CreateCategoryOutput.from(it) }
    }
}