package com.full.admin.catalog.application.category.update

import category.CategoryGateway
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class UpdateCategoryUseCaseTest {
    @InjectMockKs
    private lateinit var useCase: DefaultUpdateCategoryUseCase

    @MockK
    private lateinit var categoryGateway: CategoryGateway

    @Test
    fun givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {

    }
}