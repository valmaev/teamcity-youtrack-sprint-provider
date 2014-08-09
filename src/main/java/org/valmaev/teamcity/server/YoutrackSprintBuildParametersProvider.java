package org.valmaev.teamcity.server;

import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.parameters.AbstractBuildParametersProvider;
import org.jetbrains.annotations.NotNull;
import org.valmaev.teamcity.server.domain.SprintNameProvider;

import java.util.HashMap;
import java.util.Map;

public class YoutrackSprintBuildParametersProvider extends AbstractBuildParametersProvider {

    public static final String PARAMETER_NAME_FORMAT = "youtrack.%s.sprint";

    @NotNull
    private final Iterable<String> _projectIds;
    @NotNull
    private final SprintNameProvider _sprintNameProvider;

    public YoutrackSprintBuildParametersProvider(
             @NotNull Iterable<String> projectIds,
             @NotNull SprintNameProvider sprintNameProvider) {
        _projectIds = projectIds;
        _sprintNameProvider = sprintNameProvider;
    }

    @NotNull
    @Override
    public Map<String, String> getParameters(@NotNull SBuild build, boolean emulationMode) {
        Map<String, String> result = new HashMap<>();
        for (String projectId : _projectIds) {
            String sprintName = _sprintNameProvider.getCurrentSprintName(projectId);
            result.put(String.format(PARAMETER_NAME_FORMAT, projectId), sprintName);
        }
        return result;
    }
}
