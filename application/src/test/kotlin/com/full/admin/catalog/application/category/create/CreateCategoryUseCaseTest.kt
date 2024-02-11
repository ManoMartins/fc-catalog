package com.full.admin.catalog.application.category.create

import arrow.core.*
import category.Category
import category.CategoryGateway
import exceptions.DomainException
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.BeforeTest

@ExtendWith(MockKExtension::class)
class CreateCategoryUseCaseTest {

    @InjectMockKs
    private lateinit var useCase: DefaultCreateCategoryUseCase

    @MockK
    private lateinit var categoryGateway: CategoryGateway

    @BeforeTest
    fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true)

    @Test
    fun givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        val expectedName = "Movies"
        val expectedDescription = "The category most watched"
        val expectedIsActive = true

        val aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive)

        every { categoryGateway.create(any()) } answers { firstArg() }

        val actualOutput = useCase.execute(aCommand)

        Assertions.assertTrue(actualOutput.isRight())

        val rightValue = actualOutput.getOrElse { throw AssertionError("Expected Right, got Left") }
        Assertions.assertNotNull(rightValue.id)

        verify(exactly = 1) { categoryGateway.create(match { category: Category ->
            category.name == expectedName
                    && category.description == expectedDescription
                    && category.isActive == expectedIsActive
                    && category.deletedAt == null
        }) }
    }

    @Test
    fun givenAnInvalidName_thenCallCreateCategory_shouldReturnDomainException() {
        val expectedName = "    "
        val expectedDescription = "The category most watched"
        val expectedIsActive = true
        val expectedErrorMessage = "'name' should not be empty"
        val expectedErrorCount = 1

        val aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive)

        val actualOutput = useCase.execute(aCommand)

        Assertions.assertTrue(actualOutput.isLeft())

        val actualException = actualOutput.swap().getOrElse { throw AssertionError("Expected Right, got Left") }

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size)
        Assertions.assertEquals(expectedErrorMessage, actualException.firstError()?.message)

        verify(exactly = 0) { categoryGateway.create(any()) }
    }

    @Test
    fun givenAValidCommandWithInactiveCategory_thenCallCreateCategory_shouldReturnInactiveCategoryId() {
        val expectedName = "Movies"
        val expectedDescription = "The category most watched"
        val expectedIsActive = false

        val aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive)

        every { categoryGateway.create(any()) } answers { firstArg() }

        val actualOutput = useCase.execute(aCommand)

        val rightValue = actualOutput.getOrElse { throw AssertionError("Expected Right, got Left") }
        Assertions.assertNotNull(rightValue.id)

        verify(exactly = 1) { categoryGateway.create(match { category: Category ->
            category.name == expectedName
                    && category.description == expectedDescription
                    && category.isActive == expectedIsActive
                    && category.deletedAt != null
        }) }
    }

    @Test
    fun givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException() {
        val expectedName = "Movies"
        val expectedDescription = "The category most watched"
        val expectedIsActive = true
        val expectedErrorMessage = "Gateway error"
        val expectedErrorCount = 1

        val aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive)

        every { categoryGateway.create(any()) } throws IllegalStateException(expectedErrorMessage)

        val actualOutput = useCase.execute(aCommand)

        Assertions.assertTrue(actualOutput.isLeft())

        val actualException = actualOutput.swap().getOrElse { throw AssertionError("Expected Right, got Left") }

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size)
        Assertions.assertEquals(expectedErrorMessage, actualException.firstError()?.message)

        verify(exactly = 1) { categoryGateway.create(match { category: Category ->
            category.name == expectedName
                    && category.description == expectedDescription
                    && category.isActive == expectedIsActive
                    && category.deletedAt == null
        }) }
    }
}