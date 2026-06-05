package com.admin.catalogo.application.castmember.retrieve.get;

import com.admin.catalogo.domain.castmember.CastMember;
import com.admin.catalogo.domain.castmember.CastMemberGateway;
import com.admin.catalogo.domain.castmember.CastMemberID;
import com.admin.catalogo.domain.exceptions.NotFoundException;

import java.util.Objects;

public final class DefaultGetCastMemberByIdUseCase extends GetCastMemberByIdUseCase{

    private final CastMemberGateway castMemberGateway;

    public DefaultGetCastMemberByIdUseCase(CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CastMemberOutput execute(String anId) {
        final var aMemberId = CastMemberID.from(anId);
        return this.castMemberGateway.findById(aMemberId)
                .map(CastMemberOutput::from)
                .orElseThrow(() -> NotFoundException.with(CastMember.class, aMemberId));
    }
}
