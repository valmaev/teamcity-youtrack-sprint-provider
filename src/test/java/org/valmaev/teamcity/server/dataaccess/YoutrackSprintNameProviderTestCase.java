package org.valmaev.teamcity.server.dataaccess;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.valmaev.teamcity.server.domain.IssueTrackerConnection;
import org.valmaev.teamcity.server.domain.Sprint;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.glassfish.jersey.client.authentication.HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD;
import static org.glassfish.jersey.client.authentication.HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.valmaev.teamcity.server.domain.SprintFactory.createSprint;


public class YoutrackSprintNameProviderTestCase {

    @SuppressWarnings("unchecked")
    private static YoutrackSprintNameProvider createSystemUnderTest(
            Client restClient,
            IssueTrackerConnection connection,
            Comparator<Sprint> sprintComparator)
            throws URISyntaxException {
        return new YoutrackSprintNameProvider(
                restClient == null ? mock(Client.class) : restClient,
                connection == null ? mock(IssueTrackerConnection.class) : connection,
                sprintComparator == null
                        ? (Comparator<Sprint>) mock(Comparator.class)
                        : sprintComparator);
    }

    private static Client createClientStub(List<Sprint> responseContent) {
        Client result = mock(Client.class);
        WebTarget targetStub = mock(WebTarget.class);
        Builder builderStub = mock(Builder.class);

        when(result.target(any(URI.class)))
                .thenReturn(targetStub);
        when(targetStub.path(any(String.class)))
                .thenReturn(targetStub);
        when(targetStub.request(any(String.class)))
                .thenReturn(builderStub);
        when(builderStub.property(any(String.class), any(Object.class)))
                .thenReturn(builderStub);
        when(builderStub.get(new GenericType<List<Sprint>>() {}))
                .thenReturn(responseContent);

        return result;
    }

    private static Builder createBuilderTestDouble(
            Client clientTestDouble,
            IssueTrackerConnection connection,
            String projectId,
            List<Sprint> responseContent) {
        WebTarget targetStub = mock(WebTarget.class);
        Builder result = mock(Builder.class);

        when(clientTestDouble.target(connection.getAddress()))
                .thenReturn(targetStub);
        when(targetStub.path("/rest/admin/project/" + projectId + "/version"))
                .thenReturn(targetStub);
        when(targetStub.request(MediaType.APPLICATION_JSON))
                .thenReturn(result);
        when(result.property(HTTP_AUTHENTICATION_BASIC_USERNAME, connection.getLogin()))
                .thenReturn(result);
        when(result.property(HTTP_AUTHENTICATION_BASIC_PASSWORD, connection.getPassword()))
                .thenReturn(result);
        when(result.get(new GenericType<List<Sprint>>() {}))
                .thenReturn(responseContent);
        return result;
    }

    @Test
    public void getCurrentSprintName_always_shouldSendProperGetRequestThroughRestClient()
            throws Exception {
        // Fixture setup
        final IssueTrackerConnection connectionStub =
                mock(IssueTrackerConnection.class, RETURNS_DEEP_STUBS);
        final String inputProjectId = "foo";

        Client clientStub = mock(Client.class);
        Builder builderMock = createBuilderTestDouble(
                clientStub,
                connectionStub,
                inputProjectId,
                new ArrayList<Sprint>());
        YoutrackSprintNameProvider sut = createSystemUnderTest(
                clientStub,
                connectionStub,
                null);

        // Exercise
        sut.getCurrentSprintName(inputProjectId);

        // Verify
        verify(builderMock).get(new GenericType<List<Sprint>>() {});
    }

    @Test(dataProvider = "getCurrentSprintNameTestData")
    public void getCurrentSprintName_always_shouldReturnCorrectResult(
            List<Sprint> responseContent,
            Comparator<Sprint> sprintComparator,
            String expected)
            throws Exception {
        Client clientStub = createClientStub(responseContent);
        YoutrackSprintNameProvider sut = createSystemUnderTest(
                clientStub,
                null,
                sprintComparator);

        String actual = sut.getCurrentSprintName("foo");

        assertEquals(actual, expected);
    }

    @DataProvider
    @SuppressWarnings("unchecked")
    public Object[][] getCurrentSprintNameTestData() {
        Comparator<Sprint> dummyComparator = (Comparator<Sprint>) mock(Comparator.class);
        when(dummyComparator.compare(any(Sprint.class), any(Sprint.class))).thenReturn(-1);

        return new Object[][]{
                new Object[]{new ArrayList<Sprint>(), null, ""},
                new Object[]{Arrays.asList(createSprint("foo")), null, "foo"},
                new Object[]{
                        Arrays.asList(createSprint("foo"), createSprint("bar")),
                        dummyComparator,
                        "bar"
                }
        };
    }
}
