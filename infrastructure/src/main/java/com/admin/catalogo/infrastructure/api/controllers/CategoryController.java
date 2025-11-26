package com.admin.catalogo.infrastructure.api.controllers;

import com.admin.catalogo.domain.Pagination.Pagination;
import com.admin.catalogo.infrastructure.api.CategoryAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController implements CategoryAPI {
    @Override
    public ResponseEntity<?> createCategory() {
        return null;
    }

    @Override
    public Pagination<?> listCategories(String search, int page, int perPage, int sort, int direction) {
        return null;
    }
}
