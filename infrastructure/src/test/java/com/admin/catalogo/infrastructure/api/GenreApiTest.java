package com.admin.catalogo.infrastructure.api;

import com.admin.catalogo.ControllerTest;
import com.admin.catalogo.application.genre.create.CreateGenreOutput;
import com.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.admin.catalogo.domain.exceptions.NotificationException;
import com.admin.catalogo.domain.validation.handler.Notification;
import com.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
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

    @Test
    public void givenAValidCommand_whenCallsCreateGenre_shouldReturnedGereId() throws Exception {
        //given
        final var expectedName = "Ação";
        final var expectedCategries = List.of("123", "456");
        final var expectedActive = true;
        final var expectedId = "123";

        final var aCommand =
                new CreateGenreRequest(expectedName, expectedCategries, expectedActive);

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
                .andExpect(MockMvcResultMatchers.jsonPath("$._id", Matchers.equalTo(expectedId))
                );

        Mockito.verify(createGenreUseCase).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategries, cmd.categories())
                        && Objects.equals(expectedActive, cmd.isActive())
        ));
    }

    @Test
    public void givenInvalidName_whenCallsCreateGenre_shouldReturnNotification() throws Exception {
//given
        final String expectedName = null;
        final var expectedCategries = List.of("123", "456");
        final var expectedActive = true;
        final var expectedId = "123";
        final var expectedErrorMessage = "'name' should nor be null";

        final var aCommand =
                new CreateGenreRequest(expectedName, expectedCategries, expectedActive);

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
                        && Objects.equals(expectedCategries, cmd.categories())
                        && Objects.equals(expectedActive, cmd.isActive())
        ));
    }
}
