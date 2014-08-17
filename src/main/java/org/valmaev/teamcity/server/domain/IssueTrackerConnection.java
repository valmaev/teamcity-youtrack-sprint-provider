package org.valmaev.teamcity.server.domain;

import java.net.URI;

public class IssueTrackerConnection {
    private final URI _address;
    private final String _login;
    private final String _password;

    public IssueTrackerConnection(URI address, String login, String password) {

        if (address == null)
            throw new IllegalArgumentException("Parameter 'address' can't be null");
        if (login == null)
            throw new IllegalArgumentException("Parameter 'login' can't be null");
        if (password == null)
            throw new IllegalArgumentException("Parameter 'password' can't be null");

        _address = address;
        _login = login;
        _password = password;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        IssueTrackerConnection that = (IssueTrackerConnection) o;

        return _address.equals(that._address)
                && _login.equals(that._login)
                && _password.equals(that._password);
    }

    @Override
    public int hashCode() {
        int result = _address.hashCode();
        result = 31 * result + _login.hashCode();
        result = 31 * result + _password.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "IssueTrackerConnection{" +
                "_address=" + _address +
                ", _login='" + _login + '\'' +
                '}';
    }
}
