package com.admin.catalogo.application.castmember.retrieve.get;

import com.admin.catalogo.application.Fixture;
import com.admin.catalogo.application.UseCaseTest;
import com.admin.catalogo.domain.castmember.CastMember;
import com.admin.catalogo.domain.castmember.CastMemberGateway;
import com.admin.catalogo.domain.castmember.CastMemberID;
import com.admin.catalogo.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

public class GetCastMemberByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetCastMemberByIdUseCase defaultGetCastMemberByIdUseCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetCastMember_shouldReturnIt() {
        //given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var aMember = CastMember.newMember(expectedName, expectedType);

        final var expectedId = aMember.getId();

        Mockito.when(castMemberGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aMember));

        //when
        final var actualOutput = defaultGetCastMemberByIdUseCase.execute(expectedId.getValue());

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());
        Assertions.assertEquals(expectedName, actualOutput.name());
        Assertions.assertEquals(expectedType, actualOutput.type());
        Assertions.assertEquals(aMember.getCreatedAt(), actualOutput.createdAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), actualOutput.updatedAt());

        Mockito.verify(castMemberGateway).findById(Mockito.eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallsGetCastMemberAndDoesNotExists_shouldReturnNotFoundException() {
        //given
        final var expectedId = CastMemberID.from("123");

        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        Mockito.when(castMemberGateway.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        //when
        final var actualOutput = Assertions.assertThrows(NotFoundException.class, () ->
                defaultGetCastMemberByIdUseCase.execute(expectedId.getValue())
        );

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedErrorMessage, actualOutput.getMessage());

        Mockito.verify(castMemberGateway).findById(Mockito.eq(expectedId));
    }
}
