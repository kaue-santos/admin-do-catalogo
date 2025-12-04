package com.admin.catalogo.application.category.update;

public record UpdateCategoryCommand (
        String id,
        String name,
        String description,
        boolean isActive
)
{
    public static UpdateCategoryCommand with(
        final String anId,
        final String anName,
        final String anDescription,
        final boolean anIsActive
    ){
        return new UpdateCategoryCommand(anId, anName, anDescription, anIsActive);
    }
}
