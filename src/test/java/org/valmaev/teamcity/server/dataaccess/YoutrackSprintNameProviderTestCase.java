package org.valmaev.teamcity.server.dataaccess;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.valmaev.teamcity.server.domain.Sprint;
import org.valmaev.teamcity.server.domain.SprintReleaseDateComparator;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class YoutrackSprintNameProviderTestCase {

    @SuppressWarnings("unchecked")
    private static YoutrackSprintNameProvider createSystemUnderTest(
            Client restClient,
            URI youtrackUri,
            Comparator<Sprint> sprintComparator)
            throws URISyntaxException {
        return new YoutrackSprintNameProvider(
                restClient == null ? mock(Client.class) : restClient,
                youtrackUri == null ? createUriDummy() : youtrackUri,
                sprintComparator == null
                        ? (Comparator<Sprint>) mock(Comparator.class)
                        : sprintComparator);
    }

    private static URI createUriDummy()
            throws URISyntaxException {
        return new URI("http://example.com");
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
        when(builderStub.get(new GenericType<List<Sprint>>() {}))
                .thenReturn(responseContent);
        return result;
    }

    private static Builder createBuilderTestDouble(
            Client clientTestDouble,
            URI youtrackUri,
            String projectId,
            List<Sprint> responseContent) {
        WebTarget targetStub = mock(WebTarget.class);
        Builder result = mock(Builder.class);

        when(clientTestDouble.target(youtrackUri))
                .thenReturn(targetStub);
        when(targetStub.path("/rest/admin/project/" + projectId + "/version"))
                .thenReturn(targetStub);
        when(targetStub.request(MediaType.APPLICATION_JSON))
                .thenReturn(result);
        when(result.get(new GenericType<List<Sprint>>() {}))
                .thenReturn(responseContent);
        return result;
    }

    @Test
    public void getCurrentSprintName_always_shouldSendProperGetRequestThroughRestClient()
            throws Exception {
        // Fixture setup
        final URI youtrackUriDummy = createUriDummy();
        final String inputProjectId = "foo";

        Client clientStub = mock(Client.class);
        Builder builderMock =
                createBuilderTestDouble(clientStub, youtrackUriDummy, inputProjectId, new ArrayList<Sprint>());
        YoutrackSprintNameProvider sut = createSystemUnderTest(clientStub, youtrackUriDummy, null);

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
        YoutrackSprintNameProvider sut = createSystemUnderTest(clientStub, null, sprintComparator);

        String actual = sut.getCurrentSprintName("foo");

        assertEquals(actual, expected);
    }

    @DataProvider
    public Object[][] getCurrentSprintNameTestData() {
        return new Object[][]{
                new Object[]{new ArrayList<Sprint>(), null, ""},
                new Object[]{Arrays.asList(new Sprint("foo", new Date())), null, "foo"},
                new Object[]{
                        Arrays.asList(
                                new Sprint("foo", new Date(1)),
                                new Sprint("bar", new Date(2))),
                        new SprintReleaseDateComparator(),
                        "foo"
                }
        };
    }
}
