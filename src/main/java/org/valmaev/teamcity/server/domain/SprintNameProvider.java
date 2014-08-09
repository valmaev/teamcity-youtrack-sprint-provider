package org.valmaev.teamcity.server.domain;

public interface SprintNameProvider {
    String getCurrentSprintName(String projectId);
}
