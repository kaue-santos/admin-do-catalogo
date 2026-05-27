package com.admin.catalogo.infrastructure.genre.models;

import com.admin.catalogo.JacksonTest;
import com.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.util.List;

@JacksonTest
class CreateGenreRequestTest {

    @Autowired
    private JacksonTester<CreateGenreRequest> json;

    @Test
    public void testUnmarshall() throws Exception {
        final var expectedName = "Ação";
        final var expectedCategory = "123";
        final var expectedIsActive = false;

        final var json = """
                {
                "name": "%s",
                "categories_id": ["%s"],
                "is_active": "%s"
                }
                """.formatted(
                expectedName,
                expectedCategory,
                expectedIsActive);

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("categories", List.of(expectedCategory))
                .hasFieldOrPropertyWithValue("active", expectedIsActive);
    }

    @Test
    public void testMarshall() throws Exception {
        final var expectedName = "Ação";
        final var expectedCategories = List.of("123");
        final var expectedIsActive = false;

        final var request = new CreateGenreRequest(
                expectedName,
                expectedCategories,
                expectedIsActive
        );

        final var actualJson = this.json.write(request);

        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.categories_id", expectedCategories)
                .hasJsonPathValue("$.is_active", expectedIsActive);
    }
}