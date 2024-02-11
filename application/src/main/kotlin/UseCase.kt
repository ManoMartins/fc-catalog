package com.full.admin.catalog.application

abstract class UseCase<IN, OUT> {
    abstract fun execute(anIn: IN): OUT
}