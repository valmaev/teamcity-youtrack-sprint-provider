package org.valmaev.teamcity.server;

import jetbrains.buildServer.serverSide.SBuild;
import org.testng.annotations.Test;
import org.valmaev.teamcity.server.domain.SprintNameProvider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.valmaev.teamcity.server.YoutrackSprintBuildParametersProvider.PARAMETER_NAME_FORMAT;

public class YoutrackSprintBuildParametersProviderTestCase {

    private static YoutrackSprintBuildParametersProvider createSystemUnderTest(
            Iterable<String> projectIds,
            SprintNameProvider sprintNameProvider) {
        return new YoutrackSprintBuildParametersProvider(
                projectIds == null ? new HashSet<String>() : projectIds,
                sprintNameProvider == null ? mock(SprintNameProvider.class) : sprintNameProvider
        );
    }

    @Test
    public void getParameters_always_shouldGetCurrentSprintNamesFromProvider() {
        final String projectId = "foo";
        final String sprintName = "bar";
        SprintNameProvider sprintNameProviderStub = mock(SprintNameProvider.class);
        when(sprintNameProviderStub.getCurrentSprintName(projectId)).thenReturn(sprintName);
        YoutrackSprintBuildParametersProvider sut =
                createSystemUnderTest(Arrays.asList(projectId), sprintNameProviderStub);
        final SBuild buildDummy = mock(SBuild.class);
        final boolean modeDummy = false;

        Map<String, String> actual = sut.getParameters(buildDummy, modeDummy);

        assertThat(actual, hasEntry(String.format(PARAMETER_NAME_FORMAT, projectId), sprintName));
    }
}
