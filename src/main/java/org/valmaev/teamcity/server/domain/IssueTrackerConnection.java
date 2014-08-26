package org.valmaev.teamcity.server.domain;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

public class IssueTrackerConnection {
    private final URI _host;
    private final String _login;
    private final String _password;
    private final Set<String> _projectIds;

    public IssueTrackerConnection(
            URI host,
            String login,
            String password,
            Set<String> projectIds) {
        if (host == null)
            throw new IllegalArgumentException("Parameter 'host' can't be null");
        if (login == null)
            throw new IllegalArgumentException("Parameter 'login' can't be null");
        if (password == null)
            throw new IllegalArgumentException("Parameter 'password' can't be null");
        if (projectIds == null)
            throw new IllegalArgumentException("Parameter 'projectIds' can't be null");

        _host = host;
        _login = login;
        _password = password;
        _projectIds = Collections.unmodifiableSet(projectIds);
    }

    public URI getHost() {
        return _host;
    }

    public String getLogin() {
        return _login;
    }

    public String getPassword() {
        return _password;
    }

    public Set<String> getProjectIds() {
        return _projectIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        IssueTrackerConnection that = (IssueTrackerConnection) o;

        return _host.equals(that._host)
                && _login.equals(that._login)
                && _password.equals(that._password)
                && _projectIds.equals(that._projectIds);
    }

    @Override
    public int hashCode() {
        int result = _host.hashCode();
        result = 31 * result + _login.hashCode();
        result = 31 * result + _password.hashCode();
        result = 31 * result + _projectIds.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "IssueTrackerConnection{" +
                "Host=" + _host +
                ", Login='" + _login + '\'' +
                ", ProjectIds=" + _projectIds +
                '}';
    }
}
