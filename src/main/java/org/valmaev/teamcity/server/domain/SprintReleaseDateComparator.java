package org.valmaev.teamcity.server.domain;

import java.util.Comparator;

public class SprintReleaseDateComparator implements Comparator<Sprint> {
    @Override
    public int compare(Sprint first, Sprint second) {
        if (first == second)
            return 0;
        if (first == null)
            return -1;
        if (second == null)
            return 1;

        return first.getReleaseDate().equals(second.getReleaseDate())
                ? 0
                : first.getReleaseDate().compareTo(second.getReleaseDate());
    }
}
