package org.valmaev.teamcity.server.dataaccess;

import org.valmaev.teamcity.server.domain.Sprint;
import org.valmaev.teamcity.server.domain.SprintNameProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class YoutrackSprintNameProvider implements SprintNameProvider {

    private final Client _restClient;
    private final URI _youtrackUri;
    private final Comparator<Sprint> _sprintComparator;

    public YoutrackSprintNameProvider(
            Client restClient,
            URI youtrackUri,
            Comparator<Sprint> sprintComparator) {
        if (restClient == null)
            throw new IllegalArgumentException("Parameter 'restClient' can't be null");
        if (youtrackUri == null)
            throw new IllegalArgumentException("Parameter 'youtrackUri' can't be null");
        if (sprintComparator == null)
            throw new IllegalArgumentException("Parameter 'sprintComparator' can't be null");

        _restClient = restClient;
        _youtrackUri = youtrackUri;
        _sprintComparator = sprintComparator;
    }

    @Override
    public String getCurrentSprintName(String projectId) {
        List<Sprint> sprints = _restClient.target(_youtrackUri)
                .path("/rest/admin/project/" + projectId + "/version")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Sprint>>() {});
        Collections.sort(sprints, _sprintComparator);

        return sprints.isEmpty()
                ? ""
                : sprints.get(0).getName();
    }
}
