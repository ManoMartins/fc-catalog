package com.full.admin.catalog.application.category.update

import arrow.core.Either
import arrow.core.toEitherNel
import category.Category
import category.CategoryGateway
import category.CategoryID
import exceptions.DomainException
import validation.Error
import validation.handler.Notification

class DefaultUpdateCategoryUseCase(private val categoryGateway: CategoryGateway) : UpdateCategoryUseCase() {
    override fun execute(aCommand: UpdateCategoryCommand): Either<Notification, UpdateCategoryOutput> {
        val anId = CategoryID.from(aCommand.id)
        val aName = aCommand.name
        val aDescription = aCommand.description
        val isActive = aCommand.isActive

        val aCategory = this.categoryGateway.findById(anId) ?: throw DomainException.with(Error("Category with ID ${anId.value} was not found"))

        val notification = Notification.create()
        aCategory.update(aName, aDescription, isActive).validate(notification)

        return if (notification.hasError()) Either.Left(notification) else update(aCategory)
    }

    private fun update(aCategory: Category): Either<Notification, UpdateCategoryOutput> {
        return Either.catch {
            this.categoryGateway.update(aCategory)
        }
            .toEitherNel()
            .mapLeft { Notification.create(it.extract()) }
            .map { UpdateCategoryOutput.from(it) }
    }
}