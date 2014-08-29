package org.valmaev.teamcity.server;

import org.valmaev.teamcity.server.dataaccess.YoutrackSprintNameProvider;
import org.valmaev.teamcity.server.domain.IssueTrackerConnection;
import org.valmaev.teamcity.server.domain.Sprint;
import org.valmaev.teamcity.server.domain.SprintNameProvider;

import javax.ws.rs.client.Client;
import java.util.Comparator;

public class SprintNameProviderFactory {

    private final Client _client;
    private final Comparator<Sprint> _comparator;

    public SprintNameProviderFactory(Client client, Comparator<Sprint> comparator) {
        if (client == null)
            throw new IllegalArgumentException("Parameter 'client' can't be null");
        if (comparator == null)
            throw new IllegalArgumentException("Parameter 'comparator' can't be null");

        _client = client;
        _comparator = comparator;
    }

    public SprintNameProvider create(IssueTrackerConnection connection) {
        if (connection == null)
            throw new IllegalArgumentException("Parameter 'connection' can't be null");

        return new YoutrackSprintNameProvider(_client, connection, _comparator);
    }
}
