package com.admin.catalogo.domain.genre;

import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GenreTest {

    @Test
    public void givenValidParams_whenCallNewGenre_shouldInstantiateAGenre(){
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenInValidNullName_whenCallNewGenreAndValidate_shouldReceiverAError(){
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";


        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            Genre.newGenre(expectedName, expectedIsActive);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInValidEmptyName_whenCallNewGenreAndValidate_shouldReceiverAError(){
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";


        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            Genre.newGenre(expectedName, expectedIsActive);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInValidNameWithLengthGreaterThan255_whenCallNewGenreAndValidate_shouldReceiverAError(){
        final var expectedName = """
                O incentivo ao avanço tecnológico, assim como a valorização de fatores subjetivos desafia a capacidade 
                de equalização do sistema de formação de quadros que corresponde às necessidades.
                O incentivo ao avanço tecnológico, assim como a valorização de fatores subjetivos desafia a capacidade 
                de equalização do sistema de formação de quadros que corresponde às necessidades.
                """;
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 1 and 255 characters";

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            Genre.newGenre(expectedName, expectedIsActive);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnActivateGenre_whenCallDeactivate_shouldReceiveOK() {
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenre(expectedName, true);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertTrue( actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var actualCreateAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.deactivate();

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertEquals(actualCreateAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }


    @Test
    public void givenAnInactivateGenre_whenCallActivate_shouldReceiveOK() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenre(expectedName, false);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertFalse( actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getDeletedAt());

        final var actualCreateAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.activate();

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertEquals(actualCreateAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidInactivateGenre_whenCallUpdateWithActivate_shouldReceiveGenreUpdated() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"));

        final var actualGenre = Genre.newGenre("ação", false);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertFalse( actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getDeletedAt());

        final var actualCreateAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.update(expectedName, expectedIsActive, expectedCategories);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreateAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidActivateGenre_whenCallUpdateWithInactivate_shouldReceiveGenreUpdated() {
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(CategoryID.from("123"));

        final var actualGenre = Genre.newGenre("ação", true);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertTrue( actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var actualCreateAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.update(expectedName, expectedIsActive, expectedCategories);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreateAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithEmptyName_shouldReceiveNotificationException() {
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"));
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualGenre = Genre.newGenre("ação", false);

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            actualGenre.update(expectedName, expectedIsActive, expectedCategories);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithNullName_shouldReceiveNotificationException() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"));
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualGenre = Genre.newGenre("ação", false);

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            actualGenre.update(expectedName, expectedIsActive, expectedCategories);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithNullCategories_shouldReceiveOk() {
        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = new ArrayList<CategoryID>();

        final var actualGenre = Genre.newGenre("ação", expectedIsActive);

        final var actualCreateAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        Assertions.assertDoesNotThrow(() -> {
            actualGenre.update(expectedName, expectedIsActive, null);
        });

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreateAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidEmptyCategoriesGenre_whenCallAddCategory_shouldReceiveOk() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var seriesID = CategoryID.from("123");
        final var moviesID = CategoryID.from("456");
        final var expectedCategories = List.of(seriesID, moviesID);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, actualGenre.getCategories().size());

        final var actualCreateAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.addCategory(seriesID);
        actualGenre.addCategory(moviesID);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreateAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidEmptyCategoriesGenre_whenCallAddCategories_shouldReceiveOk() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var seriesID = CategoryID.from("123");
        final var moviesID = CategoryID.from("456");
        final var expectedCategories = List.of(seriesID, moviesID);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, actualGenre.getCategories().size());

        final var actualCreateAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.addCategories(expectedCategories);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreateAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithTwoCategories_whenCallRemoveCategory_shouldReceiveOk() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var seriesID = CategoryID.from("123");
        final var moviesID = CategoryID.from("456");
        final var expectedCategories = List.of(moviesID);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        actualGenre.update(expectedName,expectedIsActive, List.of(seriesID, moviesID));

        Assertions.assertEquals(2, actualGenre.getCategories().size());

        final var actualCreateAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.removeCategory(seriesID);

        Assertions.assertEquals(1, actualGenre.getCategories().size());
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreateAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAInvalidNullAsCategoryID_whenCallAddCategory_shouldReceiveOk() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = new ArrayList<CategoryID>();

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, actualGenre.getCategories().size());

        final var actualCreateAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.addCategory(null);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreateAt, actualGenre.getCreatedAt());
        Assertions.assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAInvalidNullAsCategoryID_whenCallRemoveCategory_shouldReceiveOk() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var seriesID = CategoryID.from("123");
        final var moviesID = CategoryID.from("456");
        final var expectedCategories = List.of(seriesID, moviesID);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, actualGenre.getCategories().size());

        actualGenre.update(expectedName, expectedIsActive, expectedCategories);

        Assertions.assertEquals(2, actualGenre.getCategories().size());

        final var actualCreateAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.removeCategory(null);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreateAt, actualGenre.getCreatedAt());
        Assertions.assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidEmptyCategoriesGenre_whenCallAddCategoriesWithEmptyList_shouldReceiveOk() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, actualGenre.getCategories().size());

        final var actualCreateAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.addCategories(expectedCategories);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreateAt, actualGenre.getCreatedAt());
        Assertions.assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidEmptyCategoriesGenre_whenCallAddCategoriesWithNullList_shouldReceiveOk() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, actualGenre.getCategories().size());

        final var actualCreateAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.addCategories(null);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreateAt, actualGenre.getCreatedAt());
        Assertions.assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }
}
