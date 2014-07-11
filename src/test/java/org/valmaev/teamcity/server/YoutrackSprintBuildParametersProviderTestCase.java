package org.valmaev.teamcity.server;

import jetbrains.buildServer.serverSide.SBuild;
import org.testng.annotations.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class YoutrackSprintBuildParametersProviderTestCase {

    private static YoutrackSprintBuildParametersProvider createSystemUnderTest(
            String projectId,
            SprintNameProvider sprintNameProvider) {
        return new YoutrackSprintBuildParametersProvider(
                projectId == null ? "" : projectId,
                sprintNameProvider == null ? mock(SprintNameProvider.class) : sprintNameProvider
        );
    }

    @Test
    public void getParameters_always_shouldGetCurrentSprintNameFromProvider() {
        final String projectId = "foo";
        final String sprintName = "bar";
        SprintNameProvider sprintNameProviderStub = mock(SprintNameProvider.class);
        when(sprintNameProviderStub.getCurrentSprintName(projectId)).thenReturn(sprintName);
        YoutrackSprintBuildParametersProvider sut = createSystemUnderTest(projectId, sprintNameProviderStub);
        final SBuild buildDummy = mock(SBuild.class);
        final boolean modeDummy = false;

        Map<String, String> actual = sut.getParameters(buildDummy, modeDummy);

        assertThat(actual, hasEntry(
                String.format(YoutrackSprintBuildParametersProvider.PARAMETER_NAME_FORMAT, projectId),
                sprintName));
    }
}
