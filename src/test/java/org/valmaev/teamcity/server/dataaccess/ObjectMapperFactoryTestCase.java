package org.valmaev.teamcity.server.dataaccess;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.valmaev.teamcity.server.domain.Sprint;

import java.util.Date;

import static org.testng.Assert.assertEquals;
import static org.valmaev.teamcity.server.domain.SprintFactory.createSprint;

public class ObjectMapperFactoryTestCase {

    @Test(dataProvider = "readValueTestData")
    public void readValue_always_shouldReturnExpectedResult(
            String input,
            TypeReference type,
            Object expected)
            throws Exception {
        ObjectMapper sut = ObjectMapperFactory.create();
        Object actual = sut.readValue(input, type);
        assertEquals(actual, expected);
    }

    @DataProvider
    public Object[][] readValueTestData() {
        return new Object[][]{
                new Object[]{
                        "{\"name\": \"\", \"releaseDate\": 0}",
                        new TypeReference<Sprint>() {},
                        createSprint("", new Date(0))
                },
                new Object[]{
                        "{\"name\": null, \"releaseDate\": null}",
                        new TypeReference<Sprint>() {},
                        createSprint("", new Date(0))
                },
                new Object[]{
                        "{\"name\": \"foo\", \"releaseDate\": null}",
                        new TypeReference<Sprint>() {},
                        createSprint("foo", new Date(0))
                },
                new Object[]{
                        "{\"name\": \"foo\", \"releaseDate\": 123}",
                        new TypeReference<Sprint>() {},
                        createSprint("foo", new Date(123))
                },
                new Object[]{
                        "{\"name\": \"foo\", \"releaseDate\": 123, \"unknownProperty\": null}",
                        new TypeReference<Sprint>() {},
                        createSprint("foo", new Date(123))
                }
        };
    }
}
