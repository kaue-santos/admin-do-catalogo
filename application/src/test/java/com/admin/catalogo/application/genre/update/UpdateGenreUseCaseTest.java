package com.admin.catalogo.application.genre.update;

import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.genre.Genre;
import com.admin.catalogo.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class UpdateGenreUseCaseTest {

    @InjectMocks
    private DefaultUpdateGenreUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId(){
        //given
        final var aGenre = Genre.newGenre("acao", true);
        final var expectedId = aGenre.getId();

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        Mockito.when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Genre.with(aGenre)));

        Mockito.when(genreGateway.update(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        //when
        final var actualOutput = useCase.execute(aCommand);

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
        Mockito.verify(genreGateway, Mockito.times(1)).update(Mockito.argThat(aUpdatedGenre ->
                Objects.equals(expectedId, aUpdatedGenre.getId())
                && Objects.equals(expectedName, aUpdatedGenre.getName())
                && Objects.equals(expectedIsActive, aUpdatedGenre.isActive())
                && Objects.equals(expectedCategories, aUpdatedGenre.getCategories())
                && Objects.equals(aGenre.getCreatedAt(), aUpdatedGenre.getCreatedAt())
                && aGenre.getUpdatedAt().isBefore(aUpdatedGenre.getUpdatedAt())
                && Objects.isNull(aUpdatedGenre.getDeletedAt())
                ));
    }

    private List<String> asString(final List<CategoryID> ids){
        return ids.stream()
                .map(CategoryID::getValue)
                .toList();
    }
}
