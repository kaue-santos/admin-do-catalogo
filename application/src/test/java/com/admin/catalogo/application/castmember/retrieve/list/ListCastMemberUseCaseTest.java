package com.admin.catalogo.application.castmember.retrieve.list;

import com.admin.catalogo.application.Fixture;
import com.admin.catalogo.application.UseCaseTest;
import com.admin.catalogo.domain.castmember.CastMember;
import com.admin.catalogo.domain.castmember.CastMemberGateway;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class ListCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListCastMemberUseCase defaultListCastMemberUseCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMembers_shouldReturnAll() {
        //given
        final var members = List.of(
                CastMember.newMember(Fixture.name(), Fixture.CastMember.type()),
                CastMember.newMember(Fixture.name(), Fixture.CastMember.type())
        );

        final var expectedPage = 0;
        final var expectedPerPage = 0;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = members.stream()
                .map(CastMemberListOutput::from)
                .toList();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPage,
                expectedTotal,
                members
        );

        Mockito.when(castMemberGateway.findAll(Mockito.any()))
                .thenReturn(expectedPagination);

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final var actualOutput = defaultListCastMemberUseCase.execute(aQuery);

        //then
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        Mockito.verify(castMemberGateway).findAll(Mockito.eq(aQuery));
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMembersAndIsEmpty_shouldReturn() {
        //given
        final var expectedPage = 0;
        final var expectedPerPage = 0;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var members = List.<CastMember>of();
        final var expectedItems = List.<CastMemberListOutput>of();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPage,
                expectedTotal,
                members
        );

        Mockito.when(castMemberGateway.findAll(Mockito.any()))
                .thenReturn(expectedPagination);

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final var actualOutput = defaultListCastMemberUseCase.execute(aQuery);

        //then
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        Mockito.verify(castMemberGateway).findAll(Mockito.eq(aQuery));
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMembersAndGatewayThrowsRadomException_shouldReturnException() {
        //given
        final var expectedPage = 0;
        final var expectedPerPage = 100;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedErrorMessage = "Gateway error";

        Mockito.when(castMemberGateway.findAll(Mockito.any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final var actualException = Assertions.assertThrows(IllegalStateException.class, () ->
                defaultListCastMemberUseCase.execute(aQuery));

        //then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(castMemberGateway).findAll(Mockito.eq(aQuery));
    }
}
