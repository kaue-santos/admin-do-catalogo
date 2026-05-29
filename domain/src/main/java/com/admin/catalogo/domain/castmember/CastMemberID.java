package com.admin.catalogo.domain.castmember;

import com.admin.catalogo.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CastMemberID extends Identifier {

    private final String value;

    private CastMemberID(final String anId) {
        Objects.requireNonNull(anId);
        this.value = anId;
    }

    public static CastMemberID unique(){
        return CastMemberID.from(UUID.randomUUID());
    }
    public static CastMemberID from (final String anId){
        return new CastMemberID(anId);
    }
    public static CastMemberID from (final UUID anId){
        return new CastMemberID(anId.toString().toLowerCase());
    }


    @Override
    public String getValue() {
        return this.value;
    }

    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CastMemberID that = (CastMemberID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }

}
