package com.full.admin.catalog.application

abstract class NullaryUseCase<OUT> {
    abstract fun execute(): OUT
}