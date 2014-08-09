package org.valmaev.teamcity.server.domain;

import com.google.common.collect.ObjectArrays;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

public class SprintTestCase {

    private static Sprint createSystemUnderTest(String name, Date releaseDate) {
        return new Sprint(name, releaseDate);
    }

    @Test(dataProvider = "equalsTestData")
    public void equals_always_shouldReturnCorrectResult(Sprint sut, Object toCompare, boolean expected) {
        assertEquals(sut.equals(toCompare), expected);
    }

    @DataProvider
    public Object[][] equalsTestData() {
        Object[][] nullCase = {{createSystemUnderTest("1.0", new Date()), null, false}};
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
                new Object[]{createSystemUnderTest("1.0", new Date()), createSystemUnderTest("1.0", new Date()), true},
                new Object[]{createSystemUnderTest("1.0", new Date()), createSystemUnderTest("1.1", new Date()), false},
                new Object[]{createSystemUnderTest("1.0", new Date(1)), createSystemUnderTest("1.0", new Date(2)), false},
                new Object[]{createSystemUnderTest("1.0", new Date()), mock(Sprint.class), false},
        };
    }
}
