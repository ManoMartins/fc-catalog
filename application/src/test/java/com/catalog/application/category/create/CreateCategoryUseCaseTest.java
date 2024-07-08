package com.catalog.application.category.create;

import com.catalog.domain.category.CategoryGateway;
import com.catalog.domain.exceptions.DomainException;
import com.catalog.domain.validation.handler.Notification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {
    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(this.gateway);
    }

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        final var expectName = "Movies";
        final var expectDescription = "Category of movies";
        final var expectIsActive = true;

        final var aCommand = CreateCategoryCommand.with(expectName, expectDescription, expectIsActive);

        Mockito.when(gateway.create(Mockito.any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(gateway, Mockito.times(1))
                .create(Mockito.argThat(aCategory ->
                                Objects.equals(expectName, aCategory.getName())
                                        && Objects.equals(expectDescription, aCategory.getDescription())
                                        && Objects.equals(expectIsActive, aCategory.isActive()
                                        && Objects.nonNull(aCategory.getId())
                                        && Objects.nonNull(aCategory.getCreatedAt())
                                        && Objects.nonNull(aCategory.getUpdatedAt())
                                        && Objects.isNull(aCategory.getDeletedAt())
                                )
                        ));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_shouldReturnDomainException() {
        final var expectName = " ";
        final var expectDescription = "Category of movies";
        final var expectIsActive = true;

        final var aCommand = CreateCategoryCommand.with(expectName, expectDescription, expectIsActive);

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(1, notification.getErrors().size());
        Assertions.assertEquals("'name' should not be empty", notification.firstError().message());

        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnCategoryId() {
        final var expectName = "Movies";
        final var expectDescription = "Category of movies";
        final var expectIsActive = false;

        final var aCommand = CreateCategoryCommand.with(expectName, expectDescription, expectIsActive);

        Mockito.when(gateway.create(Mockito.any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(gateway, Mockito.times(1))
                .create(Mockito.argThat(aCategory ->
                                Objects.equals(expectName, aCategory.getName())
                                        && Objects.equals(expectDescription, aCategory.getDescription())
                                        && Objects.equals(expectIsActive, aCategory.isActive())
                                        && Objects.nonNull(aCategory.getId())
                                        && Objects.nonNull(aCategory.getCreatedAt())
                                        && Objects.nonNull(aCategory.getUpdatedAt())
                                        && Objects.nonNull(aCategory.getDeletedAt())
                        ));
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException() {
        final var expectName = "Movies";
        final var expectDescription = "Category of movies";
        final var expectIsActive = false;

        final var aCommand = CreateCategoryCommand.with(expectName, expectDescription, expectIsActive);

        Mockito.when(gateway.create(Mockito.any())).thenThrow(new IllegalStateException("Gateway error"));

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(1, notification.getErrors().size());
        Assertions.assertEquals("Gateway error", notification.firstError().message());

        Mockito.verify(gateway, Mockito.times(1))
                .create(Mockito.argThat(aCategory ->
                        Objects.equals(expectName, aCategory.getName())
                                && Objects.equals(expectDescription, aCategory.getDescription())
                                && Objects.equals(expectIsActive, aCategory.isActive())
                                && Objects.nonNull(aCategory.getId())
                                && Objects.nonNull(aCategory.getCreatedAt())
                                && Objects.nonNull(aCategory.getUpdatedAt())
                                && Objects.nonNull(aCategory.getDeletedAt())
                ));
    }
}
