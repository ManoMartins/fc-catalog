package com.full.admin.catalog.application

abstract class UnitUseCase<IN> {
    abstract fun execute(anIn: IN)
}