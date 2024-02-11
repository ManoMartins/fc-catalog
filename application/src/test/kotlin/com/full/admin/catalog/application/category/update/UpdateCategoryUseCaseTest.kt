package com.full.admin.catalog.application.category.update

import arrow.core.getOrElse
import category.Category
import category.CategoryGateway
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions
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
        val aCategory = Category.newCategory("Movie", null, true)

        val expectedName = "Movies"
        val expectedDescription = "The category most watched"
        val expectedIsActive = true

        val expectedId = aCategory.id
        val aCommand = UpdateCategoryCommand.with(
            expectedId,
            expectedName,
            expectedDescription,
            expectedIsActive
        )

        every { categoryGateway.findById(eq(expectedId)) } returns aCategory

        every { categoryGateway.update(any()) } answers { firstArg() }

        val actualOutput = useCase.execute(aCommand)

        Assertions.assertTrue(actualOutput.isRight())

        val rightValue = actualOutput.getOrElse { throw AssertionError("Expected Right, got Left") }
        Assertions.assertNotNull(rightValue.id)

        verify(exactly = 1) { categoryGateway.findById(eq(expectedId)) }
        verify(exactly = 1) { categoryGateway.update(match { category: Category ->
            category.id == expectedId
                    && category.name == expectedName
                    && category.description == expectedDescription
                    && category.isActive == expectedIsActive
                    && category.createdAt == aCategory.createdAt
                    && category.updatedAt.isAfter(aCategory.updatedAt)
                    && category.deletedAt == null
        }) }
    }
}