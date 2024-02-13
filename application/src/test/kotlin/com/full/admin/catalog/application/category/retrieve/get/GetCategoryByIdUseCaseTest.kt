package com.full.admin.catalog.application.category.retrieve.get

import category.Category
import category.CategoryGateway
import category.CategoryID
import exceptions.DomainException
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class GetCategoryByIdUseCaseTest {
    @InjectMockKs
    private lateinit var useCase: DefaultGetCategoryByIdUseCase

    @MockK
    private lateinit var categoryGateway: CategoryGateway

    @BeforeEach
    fun cleanUp() {
        clearMocks(categoryGateway)
    }

    @Test
    fun givenAValidId_whenCallsGetCategory_shouldReturnCategory() {
        val aCategory = Category.newCategory("Movies", "The category most watched", true)

        val expectedName = "Movies"
        val expectedDescription = "The category most watched"
        val expectedIsActive = true
        val expectedId = aCategory.id

        every { categoryGateway.findById(eq(expectedId)) } returns aCategory.clone()

        val actualCategory = useCase.execute(expectedId.value)

        Assertions.assertEquals(expectedId, actualCategory.id)
        Assertions.assertEquals(expectedName, actualCategory.name)
        Assertions.assertEquals(expectedDescription, actualCategory.description)
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive)
        Assertions.assertEquals(aCategory.createdAt, actualCategory.createdAt)
        Assertions.assertEquals(aCategory.updatedAt, actualCategory.updatedAt)
        Assertions.assertEquals(aCategory.deletedAt, actualCategory.deletedAt)
        Assertions.assertEquals(CategoryOutput.from(aCategory), actualCategory)
    }

    @Test
    fun givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() {
        val expectedErrorCount = 1
        val expectedId = CategoryID.from("fake-id")
        val expectedErrorMessage = "Category with ID ${expectedId.value} was not found"

        every { categoryGateway.findById(eq(expectedId)) } returns null

        val actualException = Assertions.assertThrows(DomainException::class.java) {
            useCase.execute(expectedId.value)
        }

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size)
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors()[0].message)
    }

    @Test
    fun givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        val aCategory = Category.newCategory("Movies", "The category most watched", true)

        val expectedId = aCategory.id
        val expectedErrorMessage = "Gateway error"

        every { categoryGateway.findById(eq(expectedId)) } throws IllegalStateException(expectedErrorMessage)

        val actualException = Assertions.assertThrows(IllegalStateException::class.java) {
            useCase.execute(expectedId.value)
        }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)

        verify(exactly = 1) { categoryGateway.findById(eq(expectedId)) }
    }
}