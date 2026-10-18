package com.unstoppapoenguyen.mvttt;

import java.util.Map;

/** Required deployment configuration; never contains a built-in account. */
final class DatabaseConfig {
    final String url;
    final String user;
    final String password;

    private DatabaseConfig(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    static DatabaseConfig fromEnvironment(Map<String, String> environment) {
        String url = required(environment, "MVTTT_DB_URL");
        if (!url.startsWith("jdbc:mysql:")) {
            throw new IllegalStateException("MVTTT_DB_URL must be a MySQL JDBC URL.");
        }
        return new DatabaseConfig(url, required(environment, "MVTTT_DB_USER"),
                required(environment, "MVTTT_DB_PASSWORD"));
    }

    private static String required(Map<String, String> environment, String name) {
        String value = environment.get(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Set " + name + " in the servlet container environment.");
        }
        return value;
    }
}
