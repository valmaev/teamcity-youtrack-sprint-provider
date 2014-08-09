package org.valmaev.teamcity.server.domain;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Date;

import static org.testng.Assert.assertEquals;

public class SprintReleaseDateComparatorTestCase {

    private static Sprint createSprint() {return createSprint(new Date());}

    private static Sprint createSprint(Date releaseDate) {
        return new Sprint("anyName", releaseDate);
    }

    @Test(dataProvider = "compareTestData")
    public void compare_always_shouldReturnCorrectResult(Sprint first, Sprint second, int expected) {
        SprintReleaseDateComparator sut = new SprintReleaseDateComparator();
        int actual = sut.compare(first, second);
        assertEquals(actual, expected);
    }

    @DataProvider
    public Object[][] compareTestData() {
        Sprint sprint = createSprint();
        return new Object[][]{
                new Object[]{sprint, sprint, 0},
                new Object[]{null, null, 0},
                new Object[]{createSprint(), null, 1},
                new Object[]{null, createSprint(), -1},
                new Object[]{createSprint(new Date()), createSprint(new Date()), 0},
                new Object[]{createSprint(new Date(1)), createSprint(new Date(2)), -1},
                new Object[]{createSprint(new Date(2)), createSprint(new Date(1)), 1},
        };
    }
}
