package org.valmaev.teamcity.server.domain;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

public class IssueTrackerConnection {
    private final URI _address;
    private final String _login;
    private final String _password;
    private final Set<String> _issueIds;

    public IssueTrackerConnection(URI address, String login, String password, Set<String> issueIds) {
        if (address == null)
            throw new IllegalArgumentException("Parameter 'address' can't be null");
        if (login == null)
            throw new IllegalArgumentException("Parameter 'login' can't be null");
        if (password == null)
            throw new IllegalArgumentException("Parameter 'password' can't be null");
        if (issueIds == null)
            throw new IllegalArgumentException("Parameter 'issueIds' can't be null");

        _address = address;
        _login = login;
        _password = password;
        _issueIds = Collections.unmodifiableSet(issueIds);
    }

    public URI getAddress() {
        return _address;
    }

    public String getLogin() {
        return _login;
    }

    public String getPassword() {
        return _password;
    }

    public Set<String> getIssueIds() {
        return _issueIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        IssueTrackerConnection that = (IssueTrackerConnection) o;

        return _address.equals(that._address)
                && _login.equals(that._login)
                && _password.equals(that._password)
                && _issueIds.equals(that._issueIds);
    }

    @Override
    public int hashCode() {
        int result = _address.hashCode();
        result = 31 * result + _login.hashCode();
        result = 31 * result + _password.hashCode();
        result = 31 * result + _issueIds.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "IssueTrackerConnection{" +
                "Address=" + _address +
                ", Login='" + _login + '\'' +
                ", IssueIds=" + _issueIds +
                '}';
    }
}
