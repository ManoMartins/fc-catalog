package com.full.admin.catalog.domain.category

import category.Category
import exceptions.DomainException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import validation.handler.ThrowsValidationHandler

class CategoryTest {
    @Test
    fun givenAValidParams_whenCallNewCategory_thenInstantiateACategory() {
        val expectedName = "Filmes";
        val expectedDescription = "A categoria mais assistida";
        val expectedIsActive = true;

        val actualCategory = Category.newCategory(
            expectedName,
            expectedDescription,
            expectedIsActive
        )

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.id);
        Assertions.assertEquals(expectedName, actualCategory.name);
        Assertions.assertEquals(expectedDescription, actualCategory.description);
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive);
        Assertions.assertNotNull(actualCategory.createdAt);
        Assertions.assertNotNull(actualCategory.updatedAt);
        Assertions.assertNull(actualCategory.deletedAt);
    }

    @Test
    fun givenAnInvalidEmptyName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        val expectedErrorCount = 1;
        val expectedErrorMessage = "'name' should not be empty"

        val expectedName = "    ";
        val expectedDescription = "A categoria mais assistida";
        val expectedIsActive = true;

        val actualCategory = Category.newCategory(
            expectedName,
            expectedDescription,
            expectedIsActive
        )

        val actualException = Assertions.assertThrows(DomainException::class.java) {
            actualCategory.validate(ThrowsValidationHandler())
        }

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size)
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors()[0].message)
    }

    @Test
    fun givenAnInvalidNameLengthLestThan3_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        val expectedErrorCount = 1;
        val expectedErrorMessage = "'name' must be between 3 and 255 characters"

        val expectedName = "Fi ";
        val expectedDescription = "A categoria mais assistida";
        val expectedIsActive = true;

        val actualCategory = Category.newCategory(
            expectedName,
            expectedDescription,
            expectedIsActive
        )

        val actualException = Assertions.assertThrows(DomainException::class.java) {
            actualCategory.validate(ThrowsValidationHandler())
        }

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size)
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors()[0].message)
    }

    @Test
    fun givenAnInvalidNameLengthMoreThan255_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        val expectedErrorCount = 1;
        val expectedErrorMessage = "'name' must be between 3 and 255 characters"

        val expectedName = "O incentivo ao avanço tecnológico, assim como o julgamento imparcial das eventualidades afeta positivamente a correta previsão dos paradigmas corporativos. O incentivo ao avanço tecnológico, assim como o julgamento imparcial das eventualidades afeta positivamente a correta previsão dos paradigmas corporativos.";
        val expectedDescription = "A categoria mais assistida";
        val expectedIsActive = true;

        val actualCategory = Category.newCategory(
            expectedName,
            expectedDescription,
            expectedIsActive
        )

        val actualException = Assertions.assertThrows(DomainException::class.java) {
            actualCategory.validate(ThrowsValidationHandler())
        }

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size)
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors()[0].message)
    }

    @Test
    fun givenAValidEmptyDescription_whenCallNewCategoryAndValidate_thenInstantiateACategory() {
        val expectedName = "Filmes";
        val expectedDescription = "   ";
        val expectedIsActive = true;

        val actualCategory = Category.newCategory(
            expectedName,
            expectedDescription,
            expectedIsActive
        )

        Assertions.assertDoesNotThrow() {
            actualCategory.validate(ThrowsValidationHandler())
        }
        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.id);
        Assertions.assertEquals(expectedName, actualCategory.name);
        Assertions.assertEquals(expectedDescription, actualCategory.description);
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive);
        Assertions.assertNotNull(actualCategory.createdAt);
        Assertions.assertNotNull(actualCategory.updatedAt);
        Assertions.assertNull(actualCategory.deletedAt);
    }

    @Test
    fun givenAValidFalseIsActive_whenCallNewCategoryAndValidate_thenInstantiateACategory() {
        val expectedName = "Movies";
        val expectedDescription = "The category most watched";
        val expectedIsActive = false;

        val actualCategory = Category.newCategory(
            expectedName,
            expectedDescription,
            expectedIsActive
        )

        Assertions.assertDoesNotThrow() {
            actualCategory.validate(ThrowsValidationHandler())
        }
        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.id);
        Assertions.assertEquals(expectedName, actualCategory.name);
        Assertions.assertEquals(expectedDescription, actualCategory.description);
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive);
        Assertions.assertNotNull(actualCategory.createdAt);
        Assertions.assertNotNull(actualCategory.updatedAt);
        Assertions.assertNotNull(actualCategory.deletedAt);
    }

    @Test
    fun givenAValidActiveCategory_whenCallDeactivate_thenReturnCategoryInactivated() {
        val expectedName = "Movies";
        val expectedDescription = "The category most watched";
        val expectedIsActive = false;

        val aCategory = Category.newCategory(
            expectedName,
            expectedDescription,
            true
        )

        Assertions.assertDoesNotThrow() {
            aCategory.validate(ThrowsValidationHandler())
        }

        val updatedAt = aCategory.updatedAt

        Assertions.assertTrue(aCategory.isActive)
        Assertions.assertNull(aCategory.deletedAt)

        val actualCategory = aCategory.deactivate()

        Assertions.assertDoesNotThrow() {
            actualCategory.validate(ThrowsValidationHandler())
        }

        Assertions.assertEquals(aCategory.id, actualCategory.id);
        Assertions.assertEquals(expectedName, actualCategory.name);
        Assertions.assertEquals(expectedDescription, actualCategory.description);
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive);
        Assertions.assertNotNull(actualCategory.createdAt);
        Assertions.assertTrue(actualCategory.updatedAt.isAfter(updatedAt));
        Assertions.assertNotNull(actualCategory.deletedAt);
    }

    @Test
    fun givenAValidInactiveCategory_whenCallActivate_thenReturnCategoryActivated() {
        val expectedName = "Movies";
        val expectedDescription = "The category most watched";
        val expectedIsActive = true;

        val aCategory = Category.newCategory(
            expectedName,
            expectedDescription,
            false
        )

        Assertions.assertDoesNotThrow() {
            aCategory.validate(ThrowsValidationHandler())
        }

        val createdAt = aCategory.createdAt
        val updatedAt = aCategory.updatedAt

        Assertions.assertFalse(aCategory.isActive)
        Assertions.assertNotNull(aCategory.deletedAt)

        val actualCategory = aCategory.activate()

        Assertions.assertDoesNotThrow() {
            actualCategory.validate(ThrowsValidationHandler())
        }

        Assertions.assertEquals(aCategory.id, actualCategory.id);
        Assertions.assertEquals(expectedName, actualCategory.name);
        Assertions.assertEquals(expectedDescription, actualCategory.description);
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive);
        Assertions.assertEquals(createdAt, actualCategory.createdAt);
        Assertions.assertTrue(actualCategory.updatedAt.isAfter(updatedAt));
        Assertions.assertNull(actualCategory.deletedAt);
    }

    @Test
    fun givenAValidCategory_whenCallUpdate_thenReturnCategoryUpdated() {
        val expectedName = "Movies";
        val expectedDescription = "The category most watched";
        val expectedIsActive = true;

        val aCategory = Category.newCategory(
            "Series",
            "The series",
            expectedIsActive
        )

        Assertions.assertDoesNotThrow() {
            aCategory.validate(ThrowsValidationHandler())
        }

        val createdAt = aCategory.createdAt
        val updatedAt = aCategory.updatedAt

        val actualCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive)

        Assertions.assertDoesNotThrow() {
            actualCategory.validate(ThrowsValidationHandler())
        }


        Assertions.assertEquals(aCategory.id, actualCategory.id);
        Assertions.assertEquals(expectedName, actualCategory.name);
        Assertions.assertEquals(expectedDescription, actualCategory.description);
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive);
        Assertions.assertEquals(createdAt, actualCategory.createdAt);
        Assertions.assertTrue(actualCategory.updatedAt.isAfter(updatedAt));
        Assertions.assertNull(actualCategory.deletedAt);
    }

    @Test
    fun givenAValidCategory_whenCallUpdateToInactive_thenReturnCategoryUpdated() {
        val expectedName = "Movies";
        val expectedDescription = "The category most watched";
        val expectedIsActive = false;

        val aCategory = Category.newCategory(
            "Series",
            "The series",
            true
        )

        Assertions.assertDoesNotThrow() {
            aCategory.validate(ThrowsValidationHandler())
        }
        Assertions.assertTrue(aCategory.isActive)
        Assertions.assertNull(aCategory.deletedAt)

        val createdAt = aCategory.createdAt
        val updatedAt = aCategory.updatedAt

        val actualCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive)

        Assertions.assertDoesNotThrow() {
            actualCategory.validate(ThrowsValidationHandler())
        }


        Assertions.assertEquals(aCategory.id, actualCategory.id);
        Assertions.assertEquals(expectedName, actualCategory.name);
        Assertions.assertEquals(expectedDescription, actualCategory.description);
        Assertions.assertFalse(actualCategory.isActive);
        Assertions.assertEquals(createdAt, actualCategory.createdAt);
        Assertions.assertTrue(actualCategory.updatedAt.isAfter(updatedAt));
        Assertions.assertNotNull(actualCategory.deletedAt);
    }

    @Test
    fun givenAValidCategory_whenCallUpdateWithInvalidParams_thenReturnCategoryUpdated() {
        val expectedName = "    ";
        val expectedDescription = "The category most watched";
        val expectedIsActive = true;

        val aCategory = Category.newCategory(
            "Series",
            "The series",
            expectedIsActive
        )

        Assertions.assertDoesNotThrow() {
            aCategory.validate(ThrowsValidationHandler())
        }
        Assertions.assertTrue(aCategory.isActive)
        Assertions.assertNull(aCategory.deletedAt)

        val createdAt = aCategory.createdAt
        val updatedAt = aCategory.updatedAt

        val actualCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive)

        Assertions.assertEquals(aCategory.id, actualCategory.id);
        Assertions.assertEquals(expectedName, actualCategory.name);
        Assertions.assertEquals(expectedDescription, actualCategory.description);
        Assertions.assertTrue(actualCategory.isActive);
        Assertions.assertEquals(createdAt, actualCategory.createdAt);
        Assertions.assertTrue(actualCategory.updatedAt.isAfter(updatedAt));
        Assertions.assertNull(actualCategory.deletedAt);
    }
}