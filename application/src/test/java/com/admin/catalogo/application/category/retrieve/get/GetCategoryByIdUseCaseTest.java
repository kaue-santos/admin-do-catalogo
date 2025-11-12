package com.admin.catalogo.application.category.retrieve.get;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseTest {

    @InjectMocks
    private  DefaultGetCategoryByIdUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp(){
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldOk(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectedId = aCategory.getId();

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

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

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(DomainException.class, () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException(){
        final var expectedMessage = "Gateway error";
        final var expectedId = CategoryID.from("123");

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenThrow( new IllegalStateException(expectedMessage));

        final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }
}
