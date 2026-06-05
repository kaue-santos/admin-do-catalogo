package com.admin.catalogo.application.castmember.delete;

import com.admin.catalogo.application.Fixture;
import com.admin.catalogo.application.UseCaseTest;
import com.admin.catalogo.domain.castmember.CastMember;
import com.admin.catalogo.domain.castmember.CastMemberGateway;
import com.admin.catalogo.domain.castmember.CastMemberID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class DeleteCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteCastMemberUseCase defaultDeleteCastMemberUseCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValidId_thenCallsDeleteCastMember_shouldDeleteIt(){
        //given
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());

        final var expectedId = aMember.getId();

        Mockito.doNothing().when(castMemberGateway).deleteById(Mockito.any());

        //when
        Assertions.assertDoesNotThrow(() -> defaultDeleteCastMemberUseCase.execute(expectedId.getValue()));

        //then
        Mockito.verify(castMemberGateway).deleteById(Mockito.eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_thenCallsDeleteCastMember_shouldBeOk(){
        //given
        final var expectedId = CastMemberID.from("123");

        Mockito.doNothing().when(castMemberGateway).deleteById(Mockito.any());

        //when
        Assertions.assertDoesNotThrow(() -> defaultDeleteCastMemberUseCase.execute(expectedId.getValue()));

        //then
        Mockito.verify(castMemberGateway).deleteById(Mockito.eq(expectedId));
    }

    @Test
    public void givenAValidId_thenCallsDeleteCastMemberAndGatewayThrowsException_shouldReceiveException(){
        //given
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());

        final var expectedId = aMember.getId();

        Mockito.doThrow(new IllegalStateException("Gateway Error"))
                .when(castMemberGateway).deleteById(Mockito.any());

        //when
        Assertions.assertThrows(IllegalStateException.class, () -> defaultDeleteCastMemberUseCase.execute(expectedId.getValue()));

        //then
        Mockito.verify(castMemberGateway).deleteById(Mockito.eq(expectedId));
    }
}
