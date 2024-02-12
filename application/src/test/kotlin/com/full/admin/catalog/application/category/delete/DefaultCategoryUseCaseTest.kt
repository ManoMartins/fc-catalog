package com.full.admin.catalog.application.category.delete

import category.Category
import category.CategoryGateway
import category.CategoryID
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import io.mockk.*
import org.junit.jupiter.api.Assertions

@ExtendWith(MockKExtension::class)
class DefaultCategoryUseCaseTest {
    @InjectMockKs
    private lateinit var useCase: DefaultDeleteCategoryUseCase

    @MockK
    private lateinit var categoryGateway: CategoryGateway

    @BeforeEach
    fun cleanUp() {
        clearMocks(categoryGateway)
    }

    @Test
    fun givenAValidId_whenCallsDeleteCategory_shouldBeOK() {
        val aCategory = Category.newCategory("Movies", "The category most watched", true)

        val expectedId = aCategory.id

        every { categoryGateway.deleteById(eq(expectedId)) } just runs

        Assertions.assertDoesNotThrow() {
            useCase.execute(expectedId.value)
        }

        verify(exactly = 1) { categoryGateway.deleteById(eq(expectedId)) }
    }

    @Test
    fun givenAnInvalidId_whenCallsDeleteCategory_shouldBeOK() {
        val expectedId = CategoryID.from("fake-id")

        every { categoryGateway.deleteById(eq(expectedId)) } just runs

        Assertions.assertDoesNotThrow() {
            useCase.execute(expectedId.value)
        }

        verify(exactly = 1) { categoryGateway.deleteById(eq(expectedId)) }
    }

    @Test
    fun givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        val aCategory = Category.newCategory("Movies", "The category most watched", true)

        val expectedId = aCategory.id
        val expectedErrorMessage = "Gateway error"

        every { categoryGateway.deleteById(eq(expectedId)) } throws IllegalStateException(expectedErrorMessage)

        val actualException = Assertions.assertThrows(IllegalStateException::class.java) {
            useCase.execute(expectedId.value)
        }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)

        verify(exactly = 1) { categoryGateway.deleteById(eq(expectedId)) }
    }
}