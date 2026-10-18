package com.unstoppapoenguyen.mvttt;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;

public class DatabaseConfigTest extends TestCase {
    private Map<String, String> example() {
        return Map.of("MVTTT_DB_URL", "jdbc:mysql://127.0.0.1:3306/example",
                "MVTTT_DB_USER", "example", "MVTTT_DB_PASSWORD", "synthetic-test-password");
    }

    public void testRequiresEachSettingWithoutPrintingValues() {
        for (String name : example().keySet()) {
            Map<String, String> values = new HashMap<>(example());
            values.remove(name);
            try {
                DatabaseConfig.fromEnvironment(values);
                fail("Missing setting accepted: " + name);
            } catch (IllegalStateException expected) {
                assertTrue(expected.getMessage().contains(name));
                assertFalse(expected.getMessage().contains("synthetic-test-password"));
            }
        }
    }

    public void testPreservesExplicitConfiguration() {
        DatabaseConfig config = DatabaseConfig.fromEnvironment(example());
        assertEquals(example().get("MVTTT_DB_URL"), config.url);
        assertEquals(example().get("MVTTT_DB_USER"), config.user);
        assertEquals(example().get("MVTTT_DB_PASSWORD"), config.password);
    }

    public void testRejectsBlankValuesAndWrongDriver() {
        for (String name : example().keySet()) {
            Map<String, String> values = new HashMap<>(example());
            values.put(name, " ");
            try {
                DatabaseConfig.fromEnvironment(values);
                fail("Blank setting accepted");
            } catch (IllegalStateException expected) {
                assertTrue(expected.getMessage().contains(name));
            }
        }
        Map<String, String> values = new HashMap<>(example());
        values.put("MVTTT_DB_URL", "jdbc:other:synthetic");
        try {
            DatabaseConfig.fromEnvironment(values);
            fail("Wrong JDBC driver accepted");
        } catch (IllegalStateException expected) {
            assertTrue(expected.getMessage().contains("MVTTT_DB_URL"));
            assertFalse(expected.getMessage().contains("synthetic"));
        }
    }
}
