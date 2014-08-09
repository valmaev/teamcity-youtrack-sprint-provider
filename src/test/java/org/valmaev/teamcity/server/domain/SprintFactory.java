package org.valmaev.teamcity.server.domain;

import java.util.Date;

public class SprintFactory {
    public static Sprint createSprint(){
        return createSprint("", new Date());
    }

    public static Sprint createSprint(String name) {
        return createSprint(name, new Date());
    }

    public static Sprint createSprint(Date releaseDate) {
        return createSprint("", releaseDate);
    }

    public static Sprint createSprint(String name, Date releaseDate) {
        return new Sprint(name, releaseDate);
    }
}
