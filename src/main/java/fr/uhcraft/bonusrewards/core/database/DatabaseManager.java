package fr.uhcraft.bonusrewards.core.database;

import java.util.Arrays;

public enum DatabaseManager {
    SERVER(new DatabaseCredentials("server"));

    private final DatabaseAccess dataBaseAccess;

    DatabaseManager(DatabaseCredentials credentials) {
        this.dataBaseAccess = new DatabaseAccess(credentials);
    }

    public static void initAllDataBaseConnections() {
        Arrays.stream(values()).forEach((manager) -> {
            manager.dataBaseAccess.initPool();
        });
    }

    public static void closeAllDataBaseConnections() {
        Arrays.stream(values()).forEach((manager) -> {
            manager.dataBaseAccess.closePool();
        });
    }

    public DatabaseAccess getDataBaseAccess() {
        return this.dataBaseAccess;
    }
}
