# MVTTT — Multiverse Tic-Tac-Toe

A historical multiplayer game prototype: a mobile-oriented JavaScript interface, a Java servlet API, and MySQL persistence for players, matches and branching game boards. The client manages board interaction and game state; the server stores matches and exposes update tokens for polling.

This cleanup makes the source build reproducibly, moves database settings to the environment, and packages the web assets with the application. It removes exported player data, compiled artifacts, machine-specific deployment scripts and editor reports. The original gameplay and authentication design remain a prototype.

## Build and check

Requires **Java 21**, **Maven 3.9+**, and Python 3 for the package check.

```sh
mvn -B -f Server/pom.xml verify
python3 tools/verify_package.py
```

The build produces `Server/target/MVTTT.war`. Unit tests exercise required configuration and the historical salted-hash format without connecting to a database. The package check verifies that all four web assets are byte-identical to source and that the servlet API is supplied by the container, not bundled. CI runs the same checks.

## Local deployment

Use a local MySQL 8 database and a Jakarta Servlet 6 container such as Tomcat 10.1. Create a **disposable development database** with `Server/MVTTT_Schema.sql`. This initialization script drops and recreates its tables; it contains no player records. Configure a dedicated database account with only the necessary access.

Set `MVTTT_DB_URL`, `MVTTT_DB_USER` and `MVTTT_DB_PASSWORD` in the servlet container's environment. `.env.example` contains synthetic local examples; it is not loaded automatically. Missing or blank settings fail initialization rather than falling back to a built-in account.

Deploy the WAR with context path `/MVTTT` and open `http://127.0.0.1:8080/MVTTT/webMobile`. Keep the servlet container bound to loopback while evaluating it. The fixed context path is also used by the existing browser API calls. Assets are loaded from the WAR and need no external working directory.

## Source map and limits

- `UI/web_mobile/src/mvttt_web.js`: board interaction, branch creation, match UI and server polling. `src/archive/` preserves an earlier Canvas experiment and is not packaged into the WAR.
- `Server/src/main/java/.../WebController.java`: servlet routes and request handling.
- `DataController.java`: prepared-statement persistence and match operations.
- `Server/MVTTT_Schema.sql`: empty schema and expired-match cleanup procedure.

The checks above cover compilation, configuration, hashing compatibility and packaging. They do **not** establish end-to-end game correctness or a working database deployment. Existing limitations include a shared JDBC connection, incomplete server-side authorization/input validation, client-supplied game state, and a fast salted SHA-512 password format that is unsuitable for a production account system. The historical winner/loser record update also needs a gameplay regression test and correction. Use synthetic local accounts only; this is not an Internet-ready multiplayer service.

On 2026-10-18, public branch history was rewritten to exclude identified historical sensitive data. Re-clone if you have an older checkout; merging its history back can reintroduce removed data. GitHub cached views or retained references, if present, need separate cleanup; external copies are not erased.

MIT License — Derek Poe.
