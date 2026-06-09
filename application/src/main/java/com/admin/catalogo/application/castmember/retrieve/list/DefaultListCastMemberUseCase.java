package com.admin.catalogo.application.castmember.retrieve.list;

import com.admin.catalogo.domain.castmember.CastMemberGateway;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.domain.pagination.SearchQuery;

import java.util.Objects;

public final class DefaultListCastMemberUseCase extends ListCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultListCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public Pagination<CastMemberListOutput> execute(final SearchQuery aQuery) {
        return this.castMemberGateway.findAll(aQuery)
                .map(CastMemberListOutput::from);
    }
}
