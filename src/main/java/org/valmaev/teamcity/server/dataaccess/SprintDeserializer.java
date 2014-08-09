package org.valmaev.teamcity.server.dataaccess;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.valmaev.teamcity.server.domain.Sprint;

import java.io.IOException;
import java.util.Date;

public class SprintDeserializer extends JsonDeserializer<Sprint> {
    @Override
    public Sprint deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException {
        JsonNode sprintNode = jsonParser.readValueAsTree();
        JsonNode nameNode = sprintNode.get("name");
        String name = nameNode.isNull() ? "" : nameNode.asText();
        Date releaseDate = new Date(sprintNode.get("releaseDate").asLong(0));
        return new Sprint(name, releaseDate);
    }
}
