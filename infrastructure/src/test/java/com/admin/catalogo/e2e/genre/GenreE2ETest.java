package com.admin.catalogo.e2e.genre;

import com.admin.catalogo.E2Etest;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.genre.GenreID;
import com.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.admin.catalogo.infrastructure.configuration.json.Json;
import com.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import com.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.function.Function;

@E2Etest
@Testcontainers
public class GenreE2ETest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private GenreRepository genreRepository;

    @Container
    private static final MySQLContainer MY_SQL_CONTAINER = new MySQLContainer("mysql:8.0.33")
            .withPassword("123456")
            .withUsername("root")
            .withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void steDataSourceProperties(final DynamicPropertyRegistry registry){
        registry.add("mysql.port", () -> MY_SQL_CONTAINER.getMappedPort(3306));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCReateANewGenreWithValidValues() throws Exception {
        Assertions.assertEquals(0, genreRepository.count());

        final var expectedName = "Acao";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var actualId = givenAGenre(expectedName, expectedCategories, expectedIsActive);

        final var actualGenre = genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.getCategoriesIDs().size()
                        && expectedCategories.containsAll(actualGenre.getCategoriesIDs())
        );
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCReateANewGenreWithCategories() throws Exception {
        Assertions.assertEquals(0, genreRepository.count());

        final var filmes = givenACategory("Filmes", null, true);
        final var expectedName = "Acao";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes);

        final var actualId = givenAGenre(expectedName, expectedCategories, expectedIsActive);

        final var actualGenre = genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.getCategoriesIDs().size()
                        && expectedCategories.containsAll(actualGenre.getCategoriesIDs())
        );
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    private GenreID givenAGenre(final String aName, final List<CategoryID> aCategories, final boolean isActive) throws Exception {
        final var aRequestBody = new CreateGenreRequest(aName, mapTo(aCategories, CategoryID::getValue), isActive);

        final var aRequest = MockMvcRequestBuilders.post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        final var actualId = this.mvc.perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("/genres/", "");

        return GenreID.from(actualId);
    }

    private <A, D> List<D> mapTo(final List<A> actual, final Function<A, D> mapper){
        return actual.stream()
                .map(mapper)
                .toList();
    }

    private CategoryID givenACategory(final String aName, final String aDescription, final boolean isActive) throws Exception {
        final var aRequestBody = new CreateCategoryRequest(aName, aDescription, isActive);

        final var aRequest = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        final var actualId = this.mvc.perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("/categories/", "");

        return CategoryID.from(actualId);
    }
}
