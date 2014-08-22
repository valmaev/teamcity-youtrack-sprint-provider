package org.valmaev.teamcity.server.dataaccess;

import org.valmaev.teamcity.server.domain.IssueTrackerConnection;
import org.valmaev.teamcity.server.domain.Sprint;
import org.valmaev.teamcity.server.domain.SprintNameProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.glassfish.jersey.client.authentication.HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD;
import static org.glassfish.jersey.client.authentication.HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME;

public class YoutrackSprintNameProvider implements SprintNameProvider {

    private final Client _restClient;
    private final IssueTrackerConnection _connection;
    private final Comparator<Sprint> _sprintComparator;

    public YoutrackSprintNameProvider(
            Client restClient,
            IssueTrackerConnection connection,
            Comparator<Sprint> sprintComparator) {
        if (restClient == null)
            throw new IllegalArgumentException("Parameter 'restClient' can't be null");
        if (connection == null)
            throw new IllegalArgumentException("Parameter 'connection' can't be null");
        if (sprintComparator == null)
            throw new IllegalArgumentException("Parameter 'sprintComparator' can't be null");

        _restClient = restClient;
        _connection = connection;
        _sprintComparator = sprintComparator;
    }

    @Override
    public String getCurrentSprintName(String projectId) {
        if (projectId == null)
            throw new IllegalArgumentException("Parameter 'projectId' can't be null");

        List<Sprint> sprints = _restClient.target(_connection.getAddress())
                .path("/rest/admin/project/" + projectId + "/version")
                .request(MediaType.APPLICATION_JSON)
                .property(HTTP_AUTHENTICATION_BASIC_USERNAME, _connection.getLogin())
                .property(HTTP_AUTHENTICATION_BASIC_PASSWORD, _connection.getPassword())
                .get(new GenericType<List<Sprint>>() {});
        Collections.sort(sprints, _sprintComparator);

        return sprints.isEmpty()
                ? ""
                : sprints.get(0).getName();
    }
}
