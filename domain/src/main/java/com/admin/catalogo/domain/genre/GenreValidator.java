package com.admin.catalogo.domain.genre;

import com.admin.catalogo.domain.validation.Error;
import com.admin.catalogo.domain.validation.ValidationHandler;
import com.admin.catalogo.domain.validation.Validator;

public class GenreValidator extends Validator {

    public static final int NAME_MAX_LENGTH = 255;
    public static final int NAME_MIN_LENGTH = 1;
    private final Genre genre;

    protected GenreValidator(final Genre aGenre, ValidationHandler aHandler) {
        super(aHandler);
        this.genre = aGenre;
    }

    @Override
    public void validate() {
        CheckNameConstraints();
    }

    private void CheckNameConstraints() {
        final var name = this.genre.getName();
        if (name == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if (name.isBlank()) {
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }

        final var length = name.trim().length();
        if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
            this.validationHandler().append(new Error("'name' must be between 1 and 255 characters"));
        }
    }
}
