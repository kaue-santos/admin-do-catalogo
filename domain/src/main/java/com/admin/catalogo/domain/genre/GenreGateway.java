package com.admin.catalogo.domain.genre;

import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.domain.pagination.SearchQuery;

import java.util.Optional;

public interface GenreGateway
{
    Genre create(Genre aGenre);

    void deleteById(GenreID genreID);

    Optional<Genre> findById(GenreID genreID);

    Genre update(Genre aGenre);

    Pagination<Genre> findAll(SearchQuery aQuery);
}
