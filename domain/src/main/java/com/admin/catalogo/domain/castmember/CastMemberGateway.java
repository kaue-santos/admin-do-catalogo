package com.admin.catalogo.domain.castmember;

import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.domain.pagination.SearchQuery;

import java.util.Optional;

public interface CastMemberGateway
{
    CastMember create(CastMember aCastMember);

    void deleteById(CastMemberID memberID);

    Optional<CastMember> findById(CastMemberID memberID);

    CastMember update(CastMember aCastMember);

    Pagination<CastMember> findAll(SearchQuery aQuery);
}
