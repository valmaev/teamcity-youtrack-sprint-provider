package org.valmaev.teamcity.server.domain;

import java.net.URI;
import java.net.URISyntaxException;

public class IssueTrackerConnectionFactory {

    public static IssueTrackerConnection createConnection()
            throws URISyntaxException {
        return new IssueTrackerConnection(new URI("http://foo.com"), "bar", "baz");
    }

    public static IssueTrackerConnection createConnection(URI address)
            throws URISyntaxException {
        return new IssueTrackerConnection(address, "bar", "baz");
    }

    public static IssueTrackerConnection createConnection(String login, String password)
            throws URISyntaxException {
        return new IssueTrackerConnection(new URI("http://foo.com"), login, password);
    }
}
