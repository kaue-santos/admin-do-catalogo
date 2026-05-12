package com.admin.catalogo.application.retrieve.get;

import com.admin.catalogo.application.UseCaseTest;
import com.admin.catalogo.application.genre.retrieve.get.DefaultGetGenreByIdUseCase;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.NotFoundException;
import com.admin.catalogo.domain.genre.Genre;
import com.admin.catalogo.domain.genre.GenreGateway;
import com.admin.catalogo.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

public class GetGenreByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetGenreByIdUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetGenre_shouldReturnGenre(){
        //given
        final var expectName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );

        final var aGenre = Genre.newGenre(expectName, expectedIsActive)
                .addCategories(expectedCategories);

        final var expectedId = aGenre.getId();

        Mockito.when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aGenre));

        //when
        final var actualGenre = useCase.execute(expectedId.getValue());

        //then
        Assertions.assertEquals(expectedId.getValue(), actualGenre.id());
        Assertions.assertEquals(expectName, actualGenre.name());
        Assertions.assertEquals(asString(expectedCategories), actualGenre.categories());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.createdAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.deletedAt());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
    }

    @Test
    public void givenAValidId_whenCallsGetGenreAndDoesNotExists_shouldReturnNotFound() {
        //given
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreID.from("123");

        Mockito.when(genreGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.empty());

        //when
        final var actualException = Assertions.assertThrows(NotFoundException.class, () ->
                useCase.execute(expectedId.getValue()));

        //then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
      }

    private List<String> asString(final List<CategoryID> ids){
            return ids.stream()
                    .map(CategoryID::getValue)
                    .toList();
    }
}
