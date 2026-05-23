package com.admin.catalogo.infrastructure.api.controllers;

import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.infrastructure.api.GenreAPI;
import com.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import com.admin.catalogo.infrastructure.genre.models.GenreListResponse;
import com.admin.catalogo.infrastructure.genre.models.GenreResponse;
import com.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenreController implements GenreAPI {
    @Override
    public ResponseEntity<?> create(final CreateGenreRequest input) {
        return null;
    }

    @Override
    public Pagination<GenreListResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction) {
        return null;
    }

    @Override
    public GenreResponse getById(final String id) {
        return null;
    }

    @Override
    public ResponseEntity<?> updatedById(final String is, final UpdateGenreRequest body) {
        return null;
    }

    @Override
    public void deleteById(final String id) {

    }
}
