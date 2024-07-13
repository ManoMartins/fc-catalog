package com.catalog.application.category.delete;

import com.catalog.IntegrationTest;
import com.catalog.domain.category.Category;
import com.catalog.domain.category.CategoryGateway;
import com.catalog.domain.category.CategoryID;
import com.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
public class DeleteCategoryUseCaseIT {
    @Autowired
    private DeleteCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOK() {
        final var aCategory = Category.newCategory("Movies", "Category most watched", true);
        final var expectedId = aCategory.getId();

        save(aCategory);

        Assertions.assertEquals(1, categoryRepository.count());
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        Assertions.assertEquals(0, categoryRepository.count());

    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteCategory_shouldBeOK() {
        final var expectedId = CategoryID.from("123");

        Assertions.assertEquals(0, categoryRepository.count());
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAValidId_whenGatewayThrowsError_shouldReturnException() {
        final var aCategory = Category.newCategory("Movies", "Category most watched", true);
        final var expectedId = aCategory.getId();

        doThrow(new IllegalStateException("Gateway error"))
                .when(categoryGateway)
                .deleteById(Mockito.any());

        Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(0, categoryRepository.count());
        verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }

    private void save(final Category... aCategory) {
        categoryRepository.saveAllAndFlush(Arrays.stream(aCategory)
                .map(CategoryJpaEntity::from)
                .toList());
    }
}
