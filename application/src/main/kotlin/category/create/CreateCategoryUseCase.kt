package com.full.admin.catalog.application.category.create

import arrow.core.Either
import com.full.admin.catalog.application.UseCase
import validation.handler.Notification


abstract class CreateCategoryUseCase : UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>>() {

}