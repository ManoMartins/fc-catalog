package com.full.admin.catalog.application.category.update

import arrow.core.Either
import com.full.admin.catalog.application.UseCase
import validation.handler.Notification

abstract class UpdateCategoryUseCase : UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>>() {
}