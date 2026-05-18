package com.admin.catalogo.application.genre.delete;

import com.admin.catalogo.application.UseCaseTest;
import com.admin.catalogo.domain.genre.Genre;
import com.admin.catalogo.domain.genre.GenreGateway;
import com.admin.catalogo.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class DeleteGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidGenreId_whenCallsDeleteGenre_shouldBeDeleteGenre() {
        //given
        final var aGenre = Genre.newGenre("Ação", true);

        final var expectedId = aGenre.getId();

        Mockito.doNothing()
                .when(genreGateway).deleteById(Mockito.any());

        //when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        //then
        Mockito.verify(genreGateway, Mockito.times(1)).deleteById(expectedId);
    }

    @Test
    public void givenAnInvalidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        //given
        final var expectedId = GenreID.from("123");

        Mockito.doNothing()
                .when(genreGateway).deleteById(Mockito.any());

        //when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        //then
        Mockito.verify(genreGateway, Mockito.times(1)).deleteById(expectedId);
    }

    @Test
    public void givenAValidGenreId_whenCallsDeleteGenreAndGatewayThrowsUnexpectedError_shouldReceiveException() {
        //given
        final var aGenre = Genre.newGenre("Ação", true);

        final var expectedId = aGenre.getId();

        Mockito.doThrow(new IllegalStateException("Gateway error"))
                .when(genreGateway).deleteById(Mockito.any());

        //when
        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        //then
        Mockito.verify(genreGateway, Mockito.times(1)).deleteById(expectedId);
    }
}
