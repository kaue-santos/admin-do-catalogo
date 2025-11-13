package com.admin.catalogo.application.category.retrieve.list;

import com.admin.catalogo.application.UseCase;
import com.admin.catalogo.domain.Pagination.Pagination;
import com.admin.catalogo.domain.category.CategorySearchQuery;

public abstract class ListCategoriesUseCase extends UseCase <CategorySearchQuery, Pagination<CategoryListOutput>>{
}
