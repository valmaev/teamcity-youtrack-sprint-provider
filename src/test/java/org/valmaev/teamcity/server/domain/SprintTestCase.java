package org.valmaev.teamcity.server.domain;

import com.google.common.collect.ObjectArrays;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.valmaev.teamcity.server.domain.SprintFactory.createSprint;

public class SprintTestCase {

    @Test(dataProvider = "equalsTestData")
    public void equals_always_shouldReturnCorrectResult(Sprint sut, Object toCompare, boolean expected) {
        assertEquals(sut.equals(toCompare), expected);
    }

    @DataProvider
    public Object[][] equalsTestData() {
        Object[][] nullCase = {{createSprint(), null, false}};
        return ObjectArrays.concat(nullCase, hashCodeTestData(), Object[].class);
    }

    @Test(dataProvider = "hashCodeTestData")
    public void hashCode_forEqualObjects_shouldBeEqual(Sprint sut, Object toCompare, boolean expected) {
        boolean actual = sut.hashCode() == toCompare.hashCode();
        assertEquals(actual, expected);
    }

    @DataProvider
    public Object[][] hashCodeTestData() {
        return new Object[][]{
                new Object[]{createSprint(), createSprint(), true},
                new Object[]{createSprint("1.0"), createSprint("1.1"), false},
                new Object[]{createSprint(new Date(1)), createSprint(new Date(2)), false},
                new Object[]{createSprint("1.0", new Date()), mock(Sprint.class), false},
        };
    }
}
