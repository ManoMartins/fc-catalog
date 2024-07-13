package com.catalog.application.category.update;

import com.catalog.IntegrationTest;
import com.catalog.application.category.retrieve.get.CategoryOutput;
import com.catalog.domain.category.Category;
import com.catalog.domain.category.CategoryGateway;
import com.catalog.domain.category.CategoryID;
import com.catalog.domain.exceptions.DomainException;
import com.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@IntegrationTest
public class UpdateCategoryUseCaseIT {
    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var aCategory = Category.newCategory("Horror", null, true);

        save(aCategory);

        final var expectedName = "Movies";
        final var expectedDescription = "Category of movies";
        final var expectedIsActive = true;

        final var expectedId = aCategory.getId();
        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Assertions.assertEquals(1, categoryRepository.count());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId, actualOutput.id());

        final var actualCategory =
                categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateCategory_shouldReturnDomainException() {
        final var aCategory = Category.newCategory("Horror", null, true);

        save(aCategory);

        final var expectDescription = "Category of movies";
        final var expectIsActive = true;

        final var expectedId = aCategory.getId();
        final var expectedErrorMessage = "'name' should not be null";

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                null,
                expectDescription,
                expectIsActive
        );

        final var actualOutput = useCase.execute(aCommand).getLeft();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedErrorMessage, actualOutput.firstError().message());

        verify(categoryGateway, Mockito.times(0)).update(any());
    }


    @Test
    public void givenAValidCommand_whenCallsUpdateCategoryToInactive_shouldReturnCategoryId() {
        final var aCategory = Category.newCategory("Horror", null, true);

        save(aCategory);

        final var expectedName = "Movies";
        final var expectedDescription = "Category of movies";
        final var expectedIsActive = false;

        final var expectedId = aCategory.getId();
        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId, actualOutput.id());

        final var actualCategory =
                categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException() {
        final var aCategory = Category.newCategory("Horror", null, true);

        save(aCategory);

        final var expectName = "Movies";
        final var expectDescription = "Category of movies";
        final var expectIsActive = true;

        final var expectedId = aCategory.getId();
        final var expectedErrorMessage = "Gateway error";

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectName,
                expectDescription,
                expectIsActive
        );

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway).update(any());

        final var actualOutput = useCase.execute(aCommand).getLeft();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedErrorMessage, actualOutput.firstError().message());

        final var actualCategory =
                categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(aCategory.getName(), actualCategory.getName());
        Assertions.assertEquals(aCategory.getDescription(), actualCategory.getDescription());
        Assertions.assertEquals(aCategory.isActive(), actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() {
        final var expectName = "Movies";
        final var expectDescription = "Category of movies";
        final var expectIsActive = true;

        final var expectedId = "XXXXXXXXX";
        final var expectedErrorMessage = "Category with ID XXXXXXXXX not found.";

        final var aCommand = UpdateCategoryCommand.with(
                expectedId,
                expectName,
                expectDescription,
                expectIsActive
        );

        final var actualOutput = Assertions.assertThrows(
                DomainException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertEquals(1, actualOutput.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualOutput.getErrors().get(0).message());
    }

    private void save(final Category... aCategory) {
        categoryRepository.saveAllAndFlush(Arrays.stream(aCategory)
                .map(CategoryJpaEntity::from)
                .toList());
    }
}
