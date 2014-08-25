package org.valmaev.teamcity.server.domain;

import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Sets;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.URI;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.valmaev.teamcity.server.domain.IssueTrackerConnectionFactory.createConnection;

public class IssueTrackerConnectionTestCase {
    @Test(dataProvider = "equalsTestData")
    public void equals_always_shouldReturnCorrectResult(
            IssueTrackerConnection sut,
            Object toCompare,
            boolean expected) {
        assertEquals(sut.equals(toCompare), expected);
    }

    @DataProvider
    public Object[][] equalsTestData() {
        Object[][] nullCase = {{createConnection(), null, false}};
        return ObjectArrays.concat(nullCase, hashCodeTestData(), Object[].class);
    }

    @Test(dataProvider = "hashCodeTestData")
    public void hashCode_forEqualObjects_shouldBeEqual(
            IssueTrackerConnection sut,
            Object toCompare,
            boolean expected) {
        boolean actual = sut.hashCode() == toCompare.hashCode();
        assertEquals(actual, expected);
    }

    @DataProvider
    public Object[][] hashCodeTestData() {
        return new Object[][]{
                new Object[]{createConnection(), createConnection(), true},
                new Object[]{
                        createConnection(URI.create("http://foo.com")),
                        createConnection(URI.create("http://bar.com")),
                        false},
                new Object[]{
                        createConnection("foo", "bar"),
                        createConnection("baz", "qux"),
                        false},
                new Object[]{
                        createConnection(),
                        mock(IssueTrackerConnection.class),
                        false},
                new Object[]{
                        createConnection(Sets.newHashSet("FOO", "BAR")),
                        createConnection(Sets.newHashSet("FOO", "BAR", "BAZ")),
                        false},
        };
    }
}
