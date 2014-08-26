package org.valmaev.teamcity.server.domain;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

public class IssueTrackerConnectionFactory {

    public static IssueTrackerConnection createConnection() {
        return new IssueTrackerConnection(URI.create("http://foo.com"), "bar", "baz", new HashSet<String>());
    }

    public static IssueTrackerConnection createConnection(URI host) {
        return new IssueTrackerConnection(host, "bar", "baz", new HashSet<String>());
    }

    public static IssueTrackerConnection createConnection(String login, String password) {
        return new IssueTrackerConnection(URI.create("http://foo.com"), login, password, new HashSet<String>());
    }

    public static IssueTrackerConnection createConnection(Set<String> projectIds) {
        return new IssueTrackerConnection(URI.create("http://foo.com"), "bar", "baz", projectIds);
    }
}
