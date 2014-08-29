package org.valmaev.teamcity.server;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import jetbrains.buildServer.issueTracker.IssuePropertiesManager;
import jetbrains.buildServer.issueTracker.PropertyValue;
import jetbrains.buildServer.serverSide.SBuild;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.valmaev.teamcity.server.domain.IssueTrackerConnection;
import org.valmaev.teamcity.server.domain.SprintNameProvider;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isIn;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.valmaev.teamcity.server.YoutrackSprintBuildParametersProvider.PARAMETER_NAME_FORMAT;
import static org.valmaev.teamcity.server.YoutrackSprintBuildParametersProvider.YOUTRACK_CONNECTION_TYPE;

public class YoutrackSprintBuildParametersProviderTestCase {

    private static YoutrackSprintBuildParametersProvider createSystemUnderTest(
            SprintNameProviderFactory providerFactory,
            IssuePropertiesManager propertiesManager) {
        return new YoutrackSprintBuildParametersProvider(
                providerFactory == null
                        ? mock(SprintNameProviderFactory.class, RETURNS_DEEP_STUBS)
                        : providerFactory,
                propertiesManager == null
                        ? mock(IssuePropertiesManager.class, RETURNS_DEEP_STUBS)
                        : propertiesManager
        );
    }

    private static IssuePropertiesManager createIssuePropertiesManagerTestDouble(
            Map<String, PropertyValue> issueProperties) {
        IssuePropertiesManager propertiesManagerStub = mock(IssuePropertiesManager.class);
        when(propertiesManagerStub.getProperties()).thenReturn(issueProperties);
        return propertiesManagerStub;
    }

    private static Map<String, String> createProperties(String idPrefix) {
        return new ImmutableMap.Builder<String, String>()
                .put("host", "http://example.com")
                .put("username", "foo")
                .put("secure:password", "bar")
                .put("idPrefix", idPrefix)
                .build();
    }

    @Test(dataProvider = "getParametersFactoryUsageTestData")
    public void getParameters_always_shouldCreateProviderForEachYouTrackConnection(
            Map<String, PropertyValue> issueProperties,
            SBuild buildDummy,
            boolean modeDummy,
            Iterable<IssueTrackerConnection> expected) {
        IssuePropertiesManager propertiesManagerStub =
                createIssuePropertiesManagerTestDouble(issueProperties);
        SprintNameProviderFactory factoryMock =
                mock(SprintNameProviderFactory.class, RETURNS_DEEP_STUBS);
        YoutrackSprintBuildParametersProvider sut =
                createSystemUnderTest(factoryMock, propertiesManagerStub);

        sut.getParameters(buildDummy, modeDummy);

        for (IssueTrackerConnection connection : expected)
            verify(factoryMock).create(connection);
    }

    @DataProvider
    public Object[][] getParametersFactoryUsageTestData() {
        return new Object[][]{
                new Object[]{
                        new ImmutableMap.Builder<String, PropertyValue>()
                                .put("youtrack1",
                                        new PropertyValue(YOUTRACK_CONNECTION_TYPE,
                                                new ImmutableMap.Builder<String, String>()
                                                        .put("host", "http://example.com")
                                                        .put("username", "foo")
                                                        .put("secure:password", "bar")
                                                        .put("idPrefix", "BAZ QUX")
                                                        .build()))
                                .put("jira1",
                                        new PropertyValue("jira",
                                                new HashMap<String, String>()))
                                .build(),
                        mock(SBuild.class),
                        false,
                        Lists.newArrayList(
                                new IssueTrackerConnection(
                                        URI.create("http://example.com"),
                                        "foo",
                                        "bar",
                                        Sets.newHashSet("BAZ", "QUX")
                                )
                        )
                }
        };
    }

    @Test(dataProvider = "getParameterProperFormatTestData")
    public void getParameters_always_shouldReturnCurrentSprintNamesInProperFormat(
            Map<String, PropertyValue> issueProperties,
            Map<String, String> sprintNames,
            SBuild buildDummy,
            boolean modeDummy,
            Map<String, String> expected) {
        // Fixture setup
        SprintNameProvider providerStub = mock(SprintNameProvider.class);
        when(providerStub.getCurrentSprintNames()).thenReturn(sprintNames);

        SprintNameProviderFactory factoryStub = mock(SprintNameProviderFactory.class);
        when(factoryStub.create(any(IssueTrackerConnection.class))).thenReturn(providerStub);

        IssuePropertiesManager managerStub = createIssuePropertiesManagerTestDouble(issueProperties);

        YoutrackSprintBuildParametersProvider sut = createSystemUnderTest(factoryStub,
                managerStub);

        // Exercise
        Map<String, String> actual = sut.getParameters(buildDummy, modeDummy);

        // Verify
        assertThat(expected.entrySet(), everyItem(isIn(actual.entrySet())));
    }

    @DataProvider
    public Object[][] getParameterProperFormatTestData() {
        return new Object[][]{
                new Object[]{
                        new ImmutableMap.Builder<String, PropertyValue>()
                                .put("youtrack1",
                                        new PropertyValue(YOUTRACK_CONNECTION_TYPE,
                                                new ImmutableMap.Builder<String, String>()
                                                        .putAll(createProperties("BAZ QUX"))
                                                        .build()))
                                .build(),
                        new ImmutableMap.Builder<String, String>()
                                .put("BAZ", "0.1")
                                .put("QUX", "0.2.3")
                                .build(),
                        mock(SBuild.class),
                        false,
                        new ImmutableMap.Builder<String, String>()
                                .put(String.format(PARAMETER_NAME_FORMAT, "BAZ"), "0.1")
                                .put(String.format(PARAMETER_NAME_FORMAT, "QUX"), "0.2.3")
                                .build()
                }
        };
    }
}
