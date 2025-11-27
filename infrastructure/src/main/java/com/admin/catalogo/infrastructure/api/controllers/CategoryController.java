package com.admin.catalogo.infrastructure.api.controllers;

import com.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.admin.catalogo.domain.Pagination.Pagination;
import com.admin.catalogo.domain.validation.handler.Notification;
import com.admin.catalogo.infrastructure.api.CategoryAPI;
import com.admin.catalogo.infrastructure.category.models.CreateCategoryApiImput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    final CreateCategoryUseCase createCategoryUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase) {
        this.createCategoryUseCase = createCategoryUseCase;
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryApiImput imput) {
        final var aCommand = CreateCategoryCommand.with(
                imput.name(),
                imput.description(),
                imput.active() != null ? imput.active() : true
        );

        final Function<Notification, ResponseEntity<?>> anError = notification ->
            ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateCategoryOutput, ResponseEntity<?>> anSuccess = output ->
                ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);


        return this.createCategoryUseCase.execute(aCommand)
                .fold(anError, anSuccess);
    }

    @Override
    public Pagination<?> listCategories(String search, int page, int perPage, int sort, int direction) {
        return null;
    }
}
