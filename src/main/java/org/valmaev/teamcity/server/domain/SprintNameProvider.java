package org.valmaev.teamcity.server.domain;

import java.util.Map;

public interface SprintNameProvider {
    Map<String, String> getCurrentSprintNames();
}
