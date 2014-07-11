package org.valmaev.teamcity.server;

import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.parameters.AbstractBuildParametersProvider;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class YoutrackSprintBuildParametersProvider extends AbstractBuildParametersProvider {

    public static String PARAMETER_NAME_FORMAT = "youtrack.%s.sprint";

    @NotNull
    private final String _projectId;
    @NotNull
    private final SprintNameProvider _sprintNameProvider;

    public YoutrackSprintBuildParametersProvider(
            @NotNull String projectId,
            @NotNull SprintNameProvider sprintNameProvider) {
        _projectId = projectId;
        _sprintNameProvider = sprintNameProvider;
    }

    @NotNull
    @Override
    public Map<String, String> getParameters(@NotNull SBuild build, boolean emulationMode) {
        String sprintName = _sprintNameProvider.getCurrentSprintName(_projectId);
        Map<String, String> result = new HashMap<>();
        result.put(String.format(PARAMETER_NAME_FORMAT, _projectId), sprintName);
        return result;
    }
}
