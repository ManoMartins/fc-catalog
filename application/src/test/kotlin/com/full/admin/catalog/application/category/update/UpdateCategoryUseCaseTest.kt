package com.full.admin.catalog.application.category.update

import arrow.core.getOrElse
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
class UpdateCategoryUseCaseTest {
    @InjectMockKs
    private lateinit var useCase: DefaultUpdateCategoryUseCase

    @MockK
    private lateinit var categoryGateway: CategoryGateway

    @BeforeEach
    fun cleanUp() {
        clearMocks(categoryGateway)
    }

    @Test
    fun givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        val aCategory = Category.newCategory("Movie", null, true)

        val expectedName = "Movies"
        val expectedDescription = "The category most watched"
        val expectedIsActive = true

        val expectedId = aCategory.id
        val aCommand = UpdateCategoryCommand.with(
            expectedId.value,
            expectedName,
            expectedDescription,
            expectedIsActive
        )

        every { categoryGateway.findById(eq(expectedId)) } returns aCategory.clone()

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

    @Test
    fun givenAnInvalidName_thenCallUpdateCategory_shouldReturnDomainException() {
        val aCategory = Category.newCategory("Movie", null, true)

        val expectedName = "    "
        val expectedDescription = "The category most watched"
        val expectedIsActive = true
        val expectedErrorMessage = "'name' should not be empty"
        val expectedErrorCount = 1

        val expectedId = aCategory.id

        val aCommand = UpdateCategoryCommand.with(expectedId.value, expectedName, expectedDescription, expectedIsActive)

        every { categoryGateway.findById(eq(expectedId)) } returns aCategory.clone()

        val actualOutput = useCase.execute(aCommand)

        Assertions.assertTrue(actualOutput.isLeft())

        val actualException = actualOutput.swap().getOrElse {
            throw AssertionError("Expected Right, got Left")
        }

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size)
        Assertions.assertEquals(expectedErrorMessage, actualException.firstError()?.message)

        verify(exactly = 0) { categoryGateway.update(any()) }
    }

    @Test
    fun givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
        val aCategory = Category.newCategory("Movie", null, true)

        val expectedName = "Movies"
        val expectedDescription = "The category most watched"
        val expectedIsActive = false

        val expectedId = aCategory.id
        val aCommand = UpdateCategoryCommand.with(
            expectedId.value,
            expectedName,
            expectedDescription,
            expectedIsActive
        )

        every { categoryGateway.findById(eq(expectedId)) } returns aCategory.clone()

        every { categoryGateway.update(any()) } answers { firstArg() }

        Assertions.assertTrue(aCategory.isActive)
        Assertions.assertNull(aCategory.deletedAt)

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
                    && category.deletedAt != null
        }) }
    }

    @Test
    fun givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException() {
        val aCategory = Category.newCategory("Movie", null, true)

        val expectedName = "Movies"
        val expectedDescription = "The category most watched"
        val expectedIsActive = true
        val expectedErrorMessage = "Gateway error"
        val expectedErrorCount = 1

        val expectedId = aCategory.id
        val aCommand = UpdateCategoryCommand.with(expectedId.value, expectedName, expectedDescription, expectedIsActive)

        every { categoryGateway.findById(eq(expectedId)) } returns aCategory.clone()

        every { categoryGateway.update(any()) } throws IllegalStateException(expectedErrorMessage)

        val actualOutput = useCase.execute(aCommand)

        Assertions.assertTrue(actualOutput.isLeft())

        val actualException = actualOutput.swap().getOrElse {
            throw AssertionError("Expected Right, got Left")
        }

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size)
        Assertions.assertEquals(expectedErrorMessage, actualException.firstError()?.message)

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

    @Test
    fun givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() {
        val expectedName = "Movies"
        val expectedDescription = "The category most watched"
        val expectedIsActive = false
        val expectedId = "fake-id"
        val expectedErrorCount = 1
        val expectedErrorMessage = "Category with ID $expectedId was not found"

        val aCommand = UpdateCategoryCommand.with(
            expectedId,
            expectedName,
            expectedDescription,
            expectedIsActive
        )

        every { categoryGateway.findById(eq(CategoryID.from(expectedId))) } returns null

        val actualException = Assertions.assertThrows(DomainException::class.java) {
            useCase.execute(aCommand)
        }

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size)
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors()[0].message)

        verify(exactly = 1) { categoryGateway.findById(eq(CategoryID.from(expectedId))) }
        verify(exactly = 0) { categoryGateway.update(any()) }
    }
}