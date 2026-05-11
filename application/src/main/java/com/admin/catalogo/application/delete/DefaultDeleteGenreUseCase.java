package com.admin.catalogo.application.delete;

import com.admin.catalogo.domain.genre.GenreGateway;
import com.admin.catalogo.domain.genre.GenreID;

import java.util.Objects;

public class DefaultDeleteGenreUseCase extends DeleteGenreUseCase{

    private final GenreGateway genreGateway;

    public DefaultDeleteGenreUseCase(GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public void execute(final String aIn) {
        this.genreGateway.deleteById(GenreID.from(aIn));
    }
}
