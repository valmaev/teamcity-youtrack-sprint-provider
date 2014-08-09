package org.valmaev.teamcity.server.domain;

import java.util.Date;

public class Sprint {

    private final String _name;
    private final Date _releaseDate;

    public Sprint(String name, Date releaseDate) {
        if (name == null)
            throw new IllegalArgumentException("Parameter 'name' can't be null");
        if (releaseDate == null)
            throw new IllegalArgumentException("Parameter 'releaseDate' can't be null");

        _name = name;
        _releaseDate = releaseDate;
    }

    public String getName() {
        return _name;
    }

    public Date getReleaseDate() {
        return _releaseDate;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;

        Sprint sprint = (Sprint) other;
        return _name.equals(sprint._name) && _releaseDate.equals(sprint._releaseDate);
    }

    @Override
    public int hashCode() {
        int result = _name.hashCode();
        result = 31 * result + _releaseDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Sprint{" +
                "Name='" + _name + '\'' +
                ", ReleaseDate=" + _releaseDate +
                '}';
    }
}
