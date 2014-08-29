package org.valmaev.teamcity.server;

import com.google.common.collect.Sets;
import jetbrains.buildServer.issueTracker.IssuePropertiesManager;
import jetbrains.buildServer.issueTracker.PropertyValue;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.parameters.AbstractBuildParametersProvider;
import org.jetbrains.annotations.NotNull;
import org.valmaev.teamcity.server.domain.IssueTrackerConnection;
import org.valmaev.teamcity.server.domain.SprintNameProvider;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class YoutrackSprintBuildParametersProvider extends AbstractBuildParametersProvider {

    public static final String PARAMETER_NAME_FORMAT = "youtrack.%s.sprint";
    public static final String YOUTRACK_CONNECTION_TYPE = "youtrack";

    @NotNull
    private final SprintNameProviderFactory _providerFactory;
    @NotNull
    private final IssuePropertiesManager _propertiesManager;

    public YoutrackSprintBuildParametersProvider(
            @NotNull SprintNameProviderFactory providerFactory,
            @NotNull IssuePropertiesManager propertiesManager) {
        _providerFactory = providerFactory;
        _propertiesManager = propertiesManager;
    }

    @NotNull
    @Override
    public Map<String, String> getParameters(@NotNull SBuild build, boolean emulationMode) {
        Iterable<PropertyValue> properties = getYoutrackConnections(
                _propertiesManager.getProperties().values());

        Set<SprintNameProvider> providers = new HashSet<>();
        for (PropertyValue property : properties) {
            IssueTrackerConnection connection = new IssueTrackerConnection(
                    URI.create(property.myProperties.get("host")),
                    property.myProperties.get("username"),
                    property.myProperties.get("secure:password"),
                    Sets.newHashSet(property.myProperties.get("idPrefix").split(" "))
            );
            providers.add(_providerFactory.create(connection));
        }

        Map<String, String> result = new HashMap<>();
        for (SprintNameProvider provider : providers) {
            Map<String, String> currentSprintNames = provider.getCurrentSprintNames();
            for (String projectId : currentSprintNames.keySet())
                result.put(String.format(PARAMETER_NAME_FORMAT, projectId),
                        currentSprintNames.get(projectId));
        }

        return result;
    }

    private Iterable<PropertyValue> getYoutrackConnections(
            Iterable<PropertyValue> properties) {
        Set<PropertyValue> result = new HashSet<>();
        for (PropertyValue property : properties)
            if (property.myType.equals(YOUTRACK_CONNECTION_TYPE))
                result.add(property);
        return result;
    }
}
