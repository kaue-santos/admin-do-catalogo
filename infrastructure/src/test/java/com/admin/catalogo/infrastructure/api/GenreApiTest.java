package com.admin.catalogo.infrastructure.api;

import com.admin.catalogo.ControllerTest;
import com.admin.catalogo.application.genre.create.CreateGenreOutput;
import com.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.admin.catalogo.application.genre.retrieve.get.GenreOutput;
import com.admin.catalogo.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.admin.catalogo.application.genre.retrieve.list.GenreListOutput;
import com.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import com.admin.catalogo.application.genre.update.UpdateGenreOutput;
import com.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.NotFoundException;
import com.admin.catalogo.domain.exceptions.NotificationException;
import com.admin.catalogo.domain.genre.Genre;
import com.admin.catalogo.domain.genre.GenreID;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.domain.validation.handler.Notification;
import com.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import com.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Objects;

@ControllerTest(controllers = GenreAPI.class)
public class GenreApiTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateGenreUseCase createGenreUseCase;

    @MockBean
    private GetGenreByIdUseCase getGenreByIdUseCase;

    @MockBean
    private UpdateGenreUseCase updateGenreUseCase;

    @MockBean
    private DeleteGenreUseCase deleteGenreUseCase;

    @MockBean
    private ListGenreUseCase listGenreUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateGenre_shouldReturnedGereId() throws Exception {
        //given
        final var expectedName = "Ação";
        final var expectedCategories = List.of("123", "456");
        final var expectedActive = true;
        final var expectedId = "123";

        final var aCommand =
                new CreateGenreRequest(expectedName, expectedCategories, expectedActive);

        Mockito.when(createGenreUseCase.execute(Mockito.any()))
                .thenReturn(CreateGenreOutput.from(expectedId));

        //when
        final var aRequest = MockMvcRequestBuilders.post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));

        final var aResponse = this.mvc.perform(aRequest)
                .andDo(MockMvcResultHandlers.print());

        //then
        aResponse.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/genres/" + expectedId))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId))
                );

        Mockito.verify(createGenreUseCase).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsCreateGenre_shouldReturnNotification() throws Exception {
        //given
        final String expectedName = null;
        final var expectedCategories = List.of("123", "456");
        final var expectedActive = true;
        final var expectedId = "123";
        final var expectedErrorMessage = "'name' should nor be null";

        final var aCommand =
                new CreateGenreRequest(expectedName, expectedCategories, expectedActive);

        Mockito.when(createGenreUseCase.execute(Mockito.any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));

        //when
        final var aRequest = MockMvcRequestBuilders.post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));

        final var aResponse = this.mvc.perform(aRequest)
                .andDo(MockMvcResultHandlers.print());

        //then
        aResponse.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage))
                );

        Mockito.verify(createGenreUseCase).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAValidId_whenCallsGetGenreById_shouldReturnGenre() throws Exception {
        //given
        final var expectedName = "Ação";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = false;

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive)
                .addCategories(expectedCategories.stream()
                        .map(CategoryID::from)
                        .toList());

        final var expectedId= aGenre.getId().getValue();

        Mockito.when(getGenreByIdUseCase.execute(Mockito.any()))
                .thenReturn(GenreOutput.from(aGenre));

        //when
        final var aRequest = MockMvcRequestBuilders.get("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        //then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(expectedName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categories_id", Matchers.equalTo(expectedCategories)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.is_active", Matchers.equalTo(expectedIsActive)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at", Matchers.equalTo(aGenre.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", Matchers.equalTo(aGenre.getUpdatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deleted_at", Matchers.equalTo(aGenre.getDeletedAt().toString())));

        Mockito.verify(getGenreByIdUseCase).execute(Mockito.eq(expectedId));
    }

    @Test
    public void givenAValidId_whenCallsGetGenreById_shouldReturnNotFound() throws Exception {
        //given
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreID.from("123");

        Mockito.when(getGenreByIdUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(Genre.class, expectedId));

        //when
        final var aRequest = MockMvcRequestBuilders.get("/genres/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        //then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(getGenreByIdUseCase).execute(Mockito.eq(expectedId.getValue()));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateGenre_shouldReturnedGereId() throws Exception {
        //given
        final var expectedName = "Ação";
        final var expectedCategories = List.of("123", "456");
        final var expectedActive = true;

        final var aGenre = Genre.newGenre(expectedName, expectedActive);
        final var expectedId = aGenre.getId().getValue();

        final var aCommand =
                new CreateGenreRequest(expectedName, expectedCategories, expectedActive);

        Mockito.when(updateGenreUseCase.execute(Mockito.any()))
                .thenReturn(UpdateGenreOutput.from(aGenre));

        //when
        final var aRequest = MockMvcRequestBuilders.put("/genres/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));

        final var aResponse = this.mvc.perform(aRequest)
                .andDo(MockMvcResultHandlers.print());

        //then
        aResponse.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId))
                );

        Mockito.verify(updateGenreUseCase).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateGenre_shouldReturnNotification() throws Exception {
        //given
        final String expectedName = null;
        final var expectedCategories = List.of("123", "456");
        final var expectedActive = true;
        final var expectedErrorMessage = "'name' should nor be null";

        final var aGenre = Genre.newGenre("Ação", expectedActive);
        final var expectedId = aGenre.getId().getValue();

        final var aCommand =
                new UpdateGenreRequest(expectedName, expectedCategories, expectedActive);

        Mockito.when(updateGenreUseCase.execute(Mockito.any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));

        //when
        final var aRequest = MockMvcRequestBuilders.put("/genres/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));

        final var aResponse = this.mvc.perform(aRequest)
                .andDo(MockMvcResultHandlers.print());

        //then
        aResponse.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage))
                );

        Mockito.verify(updateGenreUseCase).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAValidId_whenCallsDeleteGenre_shouldBeOk() throws Exception {
        //given
        final var expectedId = "123";

        Mockito.doNothing()
                .when(deleteGenreUseCase).execute(Mockito.any());

        //when
        final var aRequest = MockMvcRequestBuilders.delete("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        final var result = this.mvc.perform(aRequest);

        //then
        result.andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(deleteGenreUseCase).execute(Mockito.eq(expectedId));
    }

    @Test
    public void givenValidParams_whenCallsListGenres_shouldReturnGenres() throws Exception {
        //given
        final var aGenre = Genre.newGenre("Ação", false);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "ac";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(GenreListOutput.from(aGenre));

        Mockito.when(listGenreUseCase.execute(Mockito.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        //when
        final var aRequest = MockMvcRequestBuilders.get("/genres")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON);

        final var result = this.mvc.perform(aRequest);

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(expectedPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(aGenre.getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo(aGenre.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].is_active", Matchers.equalTo(aGenre.isActive())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(aGenre.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].deleted_at", Matchers.equalTo(aGenre.getDeletedAt().toString())));

        Mockito.verify(listGenreUseCase).execute(Mockito.argThat(query ->
                        Objects.equals(expectedPage, query.page())
                                && Objects.equals(expectedPerPage, query.perPage())
                                && Objects.equals(expectedDirection, query.direction())
                                && Objects.equals(expectedSort, query.sort())
                                && Objects.equals(expectedTerms, query.terms())
                )
        );
    }
}
