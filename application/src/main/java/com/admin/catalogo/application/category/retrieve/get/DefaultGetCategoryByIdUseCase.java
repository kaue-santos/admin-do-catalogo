package com.admin.catalogo.application.category.retrieve.get;

import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.DomainException;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CategoryOutput execute(final String anId) {
        CategoryID anCatgeoryID = CategoryID.from(anId);
        return this.categoryGateway.findById(anCatgeoryID)
                .map(CategoryOutput::from)
                .orElseThrow(notFound(anCatgeoryID));
    }

    private Supplier<DomainException> notFound(final CategoryID anId) {
        return () ->DomainException.with(
                new com.admin.catalogo.domain.validation.Error("Category with %s was not found".formatted(anId.getValue()))
        );
    }
}
