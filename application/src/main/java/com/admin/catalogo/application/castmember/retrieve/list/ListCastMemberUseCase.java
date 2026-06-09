package com.admin.catalogo.application.castmember.retrieve.list;

import com.admin.catalogo.application.UseCase;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.domain.pagination.SearchQuery;

public sealed abstract class ListCastMemberUseCase
        extends UseCase<SearchQuery, Pagination<CastMemberListOutput>>
        permits DefaultListCastMemberUseCase {
}
