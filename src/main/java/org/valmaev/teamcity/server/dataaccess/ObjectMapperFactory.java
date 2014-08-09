package org.valmaev.teamcity.server.dataaccess;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.valmaev.teamcity.server.domain.Sprint;

public final class ObjectMapperFactory {

    private ObjectMapperFactory() {
        throw new UnsupportedOperationException();
    }

    public static ObjectMapper create() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new SimpleModule()
                        .addDeserializer(Sprint.class, new SprintDeserializer()));
    }
}
