package com.admin.catalogo.application.category.retrieve.get;

import com.admin.catalogo.IntegrationTest;
import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.DomainException;
import com.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.Optional;

@IntegrationTest
public class GetCategoryByIdUseCaseIT {

    @Autowired
    private GetCategoryByIdUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldOk(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectedId = aCategory.getId();

        save(aCategory);

        final var actualCategory = useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId, actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.createdAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.updatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.deletedAt());
//        Assertions.assertEquals(CategoryOutput.from(aCategory), actualCategory);
    }

    @Test
    public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound(){
        final var expectedMessage = "Category with 123 was not found";
        final var expectedId = CategoryID.from("123");

        final var actualException = Assertions.assertThrows(DomainException.class, () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException(){
        final var expectedMessage = "Gateway error";
        final var expectedId = CategoryID.from("123");

        Mockito.doThrow(new IllegalStateException(expectedMessage))
                        .when(categoryGateway).findById(Mockito.eq(expectedId));

        final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }

    private void save(final Category... aCategory) {
       categoryRepository.saveAllAndFlush(
                Arrays.stream(aCategory)
                .map(CategoryJpaEntity::from)
                .toList()
       );
    }
}
