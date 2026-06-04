package com.admin.catalogo.application.castmember.update;

import com.admin.catalogo.application.Fixture;
import com.admin.catalogo.application.UseCaseTest;
import com.admin.catalogo.domain.castmember.CastMember;
import com.admin.catalogo.domain.castmember.CastMemberGateway;
import com.admin.catalogo.domain.castmember.CastMemberID;
import com.admin.catalogo.domain.castmember.CastMemberType;
import com.admin.catalogo.domain.exceptions.NotFoundException;
import com.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

public class UpdateCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateCastMemberUseCase updateGenreUseCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() {
        //given
        final var aMember = CastMember.newMember("vin diesel", CastMemberType.ACTOR);

        final var expectedId = aMember.getId();
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.DIRECTOR;

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        Mockito.when(castMemberGateway.findById(Mockito.any())).
                thenReturn(Optional.of(CastMember.with(aMember)));

        Mockito.when(castMemberGateway.update(Mockito.any())).
                thenAnswer(returnsFirstArg());

        //when
        final var actualOutput = updateGenreUseCase.execute(aCommand);

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito.verify(castMemberGateway).findById(Mockito.eq(expectedId));

        Mockito.verify(castMemberGateway).update(Mockito.argThat(aUpdatedMember ->
                Objects.equals(expectedId, aUpdatedMember.getId())
                        && Objects.equals(expectedName, aUpdatedMember.getName())
                        && Objects.equals(expectedType, aUpdatedMember.getType())
                        && Objects.equals(aMember.getCreatedAt(), aUpdatedMember.getCreatedAt())
                        && aMember.getUpdatedAt().isBefore(aUpdatedMember.getUpdatedAt())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        //given
        final var aMember = CastMember.newMember("vin diesel", CastMemberType.ACTOR);

        final var expectedId = aMember.getId();
        final String expectedName = null;
        final var expectedType = CastMemberType.DIRECTOR;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        Mockito.when(castMemberGateway.findById(Mockito.any())).
                thenReturn(Optional.of(CastMember.with(aMember)));

        //when
        final var actualException = Assertions.assertThrows(NotificationException.class, () ->
                updateGenreUseCase.execute(aCommand));

        //then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(castMemberGateway).findById(Mockito.eq(expectedId));

        Mockito.verify(castMemberGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenAInvalidType_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        //given
        final var aMember = CastMember.newMember("vin diesel", CastMemberType.ACTOR);

        final var expectedId = aMember.getId();
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        Mockito.when(castMemberGateway.findById(Mockito.any())).
                thenReturn(Optional.of(aMember));

        //when
        final var actualException = Assertions.assertThrows(NotificationException.class, () ->
                updateGenreUseCase.execute(aCommand));

        //then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(castMemberGateway).findById(Mockito.eq(expectedId));

        Mockito.verify(castMemberGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenAInvalidId_whenCallsUpdateCastMember_shouldThrowsNotFoundException() {
        //given
        final var aMember = CastMember.newMember("vin diesel", CastMemberType.ACTOR);

        final var expectedId = CastMemberID.from("123");
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        Mockito.when(castMemberGateway.findById(Mockito.any())).
                thenReturn(Optional.empty());

        //when
        final var actualException = Assertions.assertThrows(NotFoundException.class, () ->
                updateGenreUseCase.execute(aCommand));

        //then
        Assertions.assertNotNull(actualException);

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(castMemberGateway).findById(Mockito.eq(expectedId));

        Mockito.verify(castMemberGateway, Mockito.times(0)).update(Mockito.any());
    }
}
