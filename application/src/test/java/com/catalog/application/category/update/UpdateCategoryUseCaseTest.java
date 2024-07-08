package com.catalog.application.category.update;

import com.catalog.domain.category.Category;
import com.catalog.domain.category.CategoryGateway;
import com.catalog.domain.category.CategoryID;
import com.catalog.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {
    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(this.categoryGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var aCategory = Category.newCategory("Horror", null, true);

        final var expectName = "Movies";
        final var expectDescription = "Category of movies";
        final var expectIsActive = true;

        final var expectedId = aCategory.getId();
        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectName,
                expectDescription,
                expectIsActive
        );

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId, actualOutput.id());

        verify(categoryGateway, Mockito.times(1)).findById(eq(expectedId));
        verify(categoryGateway, Mockito.times(1)).update(argThat(
                anUpdatedCategory -> Objects.equals(expectName, anUpdatedCategory.getName())
                        && Objects.equals(expectDescription, anUpdatedCategory.getDescription())
                        && Objects.equals(expectIsActive, anUpdatedCategory.isActive())
                        && Objects.equals(expectedId, anUpdatedCategory.getId())
                        && Objects.equals(aCategory.getCreatedAt(), anUpdatedCategory.getCreatedAt())
                        && aCategory.getUpdatedAt().isBefore(anUpdatedCategory.getUpdatedAt())
                        && Objects.isNull(anUpdatedCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateCategory_shouldReturnDomainException() {
        final var aCategory = Category.newCategory("Horror", null, true);

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

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        final var actualOutput = useCase.execute(aCommand).getLeft();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedErrorMessage, actualOutput.firstError().message());

        verify(categoryGateway, Mockito.times(1)).findById(eq(expectedId));
        verify(categoryGateway, Mockito.times(0)).update(any());
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCategoryToInactive_shouldReturnCategoryId() {
        final var aCategory = Category.newCategory("Horror", null, true);

        final var expectName = "Movies";
        final var expectDescription = "Category of movies";
        final var expectIsActive = false;

        final var expectedId = aCategory.getId();
        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectName,
                expectDescription,
                expectIsActive
        );

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId, actualOutput.id());

        verify(categoryGateway, Mockito.times(1)).findById(eq(expectedId));
        verify(categoryGateway, Mockito.times(1)).update(argThat(
                anUpdatedCategory -> Objects.equals(expectName, anUpdatedCategory.getName())
                        && Objects.equals(expectDescription, anUpdatedCategory.getDescription())
                        && Objects.equals(expectIsActive, anUpdatedCategory.isActive())
                        && Objects.equals(expectedId, anUpdatedCategory.getId())
                        && Objects.equals(aCategory.getCreatedAt(), anUpdatedCategory.getCreatedAt())
                        && aCategory.getUpdatedAt().isBefore(anUpdatedCategory.getUpdatedAt())
                        && Objects.nonNull(anUpdatedCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException() {
        final var aCategory = Category.newCategory("Horror", null, true);

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

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        when(categoryGateway.update(any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualOutput = useCase.execute(aCommand).getLeft();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedErrorMessage, actualOutput.firstError().message());

        verify(categoryGateway, Mockito.times(1)).findById(eq(expectedId));
        verify(categoryGateway, Mockito.times(1)).update(argThat(
                anUpdatedCategory -> Objects.equals(expectName, anUpdatedCategory.getName())
                        && Objects.equals(expectDescription, anUpdatedCategory.getDescription())
                        && Objects.equals(expectIsActive, anUpdatedCategory.isActive())
                        && Objects.equals(expectedId, anUpdatedCategory.getId())
                        && Objects.equals(aCategory.getCreatedAt(), anUpdatedCategory.getCreatedAt())
                        && aCategory.getUpdatedAt().isBefore(anUpdatedCategory.getUpdatedAt())
                        && Objects.isNull(anUpdatedCategory.getDeletedAt())
        ));
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

        when(categoryGateway.findById(eq(CategoryID.from(expectedId))))
                .thenReturn(Optional.empty());

        final var actualOutput = Assertions.assertThrows(
                DomainException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertEquals(1, actualOutput.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualOutput.getErrors().get(0).message());

        verify(categoryGateway, Mockito.times(1)).findById(eq(CategoryID.from(expectedId)));
        verify(categoryGateway, Mockito.times(0)).update(any());
    }
}
