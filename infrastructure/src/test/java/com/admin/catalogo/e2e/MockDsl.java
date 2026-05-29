package com.admin.catalogo.e2e;

import com.admin.catalogo.domain.Identifier;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.genre.GenreID;
import com.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.admin.catalogo.infrastructure.configuration.json.Json;
import com.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import com.admin.catalogo.infrastructure.genre.models.GenreResponse;
import com.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.function.Function;

public interface MockDsl {

    MockMvc mvc();

    default ResultActions deleteACategory(final Identifier anId) throws Exception {
        return this.delete("/categories/", anId);
    }

    default CategoryID givenACategory(final String aName, final String aDescription, final boolean isActive) throws Exception {
        final var aRequestBody = new CreateCategoryRequest(aName, aDescription, isActive);
        final var actualId = this.given("/categories", aRequestBody);

        return CategoryID.from(actualId);
    }

    default ResultActions listCategories(final int page, final int perPage, final String search) throws Exception {
        return listCategories(page, perPage, search, "", "");
    }

    default ResultActions listCategories(final int page, final int perPage) throws Exception {
        return listCategories(page, perPage, "", "", "");
    }

    default ResultActions listCategories(
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String dir
    ) throws Exception {
        return this.list(
                "/categories",
                page,
                perPage, search,
                sort,
                dir
        );
    }

    default CategoryResponse retrieveACategory(final Identifier anId) throws Exception {
        return this.retrieve("/categories/", anId, CategoryResponse.class);
    }

    default ResultActions updateACategory(final Identifier anId, final UpdateCategoryRequest aRequestBody) throws Exception {
        return this.update("/categories/", anId, aRequestBody);
    }

    default ResultActions deleteAGenre(final Identifier anId) throws Exception {
        return this.delete("/genres/", anId);
    }

    default GenreID givenAGenre(final String aName, final List<CategoryID> aCategories, final boolean isActive) throws Exception {
        final var aRequestBody = new CreateGenreRequest(aName, mapTo(aCategories, CategoryID::getValue), isActive);
        final var actualId = this.given("/genres", aRequestBody);

        return GenreID.from(actualId);
    }

    private String given(final String url, final Object body) throws Exception {
        final var aRequest = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        final var actualId = this.mvc().perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("%s/".formatted(url), "");

        return actualId;
    }

    default ResultActions listGenres(final int page, final int perPage, final String search) throws Exception {
        return listGenres(page, perPage, search, "", "");
    }

    default ResultActions listGenres(final int page, final int perPage) throws Exception {
        return listGenres(page, perPage, "", "", "");
    }

    default ResultActions listGenres(
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String dir
    ) throws Exception {
        return this.list(
                "/genres",
                page,
                perPage, search,
                sort,
                dir
        );
    }

    default GenreResponse retrieveAGenre(final Identifier anId) throws Exception {
        return this.retrieve("/genres/", anId, GenreResponse.class);
    }

    default ResultActions updateAGenre(final Identifier anId, final UpdateGenreRequest aRequestBody) throws Exception {
        return this.update("/genres/", anId, aRequestBody);
    }

    default <A, D> List<D> mapTo(final List<A> actual, final Function<A, D> mapper) {
        return actual.stream()
                .map(mapper)
                .toList();
    }

    private ResultActions list(
            final String url,
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String dir) throws Exception {
        final var aRequest = MockMvcRequestBuilders.get(url)
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("dir", dir)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);
    }

    private <T> T retrieve(final String url, final Identifier anId, final Class<T> clazz) throws Exception {
        final var aRequest = MockMvcRequestBuilders.get(url + anId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var json = this.mvc().perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, clazz);
    }

    private ResultActions delete(final String url, final Identifier anId) throws Exception {
        final var aRequest = MockMvcRequestBuilders.delete(
                        url + anId.getValue())
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);
    }

    private ResultActions update(final String url, final Identifier anId, final Object aRequestBody) throws Exception {
        final var aRequest = MockMvcRequestBuilders.put(url + anId.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        return this.mvc().perform(aRequest);
    }
}
