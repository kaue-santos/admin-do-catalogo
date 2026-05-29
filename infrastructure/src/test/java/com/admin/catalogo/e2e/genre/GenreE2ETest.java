package com.admin.catalogo.e2e.genre;

import com.admin.catalogo.E2Etest;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.e2e.MockDsl;
import com.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@E2Etest
@Testcontainers
public class GenreE2ETest implements MockDsl {

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

    @Override
    public MockMvc mvc() {
        return this.mvc;
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewGenreWithValidValues() throws Exception {
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
    public void asACatalogAdminIShouldBeAbleToCreateANewGenreWithCategories() throws Exception {
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

    @Test
    public void asACatalogAdminIShouldBeAbleToNavigateThruAllGenres() throws Exception {

        givenAGenre("Acao", List.of(), true);
        givenAGenre("Esportes", List.of(), true);
        givenAGenre("Drama", List.of(), true);

        listGenres(0, 1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Acao")));

        listGenres(1, 1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Drama")));

        listGenres(2, 1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Esportes")));

        listGenres(3, 1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(0)));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSearchBetweenAllGenres() throws Exception {

        givenAGenre("Acao", List.of(), true);
        givenAGenre("Esportes", List.of(), true);
        givenAGenre("Drama", List.of(), true);

        listGenres(0, 1, "dra")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Drama")));
    }

    @Test
    public void asACategoryAdminIShouldBeAbleToSortAllGenresByNameDesc() throws Exception {

        givenAGenre("Acao", List.of(), true);
        givenAGenre("Esportes", List.of(), true);
        givenAGenre("Drama", List.of(), true);

        listGenres(0, 3, "", "name", "desc")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Esportes")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].name", Matchers.equalTo("Drama")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[2].name", Matchers.equalTo("Acao")));
    }

}
