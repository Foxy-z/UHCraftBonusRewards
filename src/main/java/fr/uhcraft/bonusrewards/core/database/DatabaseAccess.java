package fr.uhcraft.bonusrewards.core.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseAccess {
    private final DatabaseCredentials credentials;
    private HikariDataSource hikariDataSource;

    DatabaseAccess(DatabaseCredentials credentials) {
        this.credentials = credentials;
    }

    private void setUpHikariCP() {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(this.credentials.toURI());
        hikariConfig.setUsername(this.credentials.getUser());
        hikariConfig.setPassword(this.credentials.getPass());
        hikariConfig.setMaximumPoolSize(3);
        hikariConfig.setMaxLifetime(30000L);
        hikariConfig.setIdleTimeout(30000L);
        hikariConfig.setLeakDetectionThreshold(30000L);
        hikariConfig.setConnectionTimeout(10000L);
        hikariConfig.setPoolName(Bukkit.getServerName());
        hikariConfig.setMinimumIdle(2);
        hikariConfig.addDataSourceProperty("cachePrepStmts", true);
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", 256);
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        hikariConfig.addDataSourceProperty("useServerPrepStmts", true);
        this.hikariDataSource = new HikariDataSource(hikariConfig);
    }

    public void initPool() {
        this.setUpHikariCP();
    }

    public void closePool() {
        this.hikariDataSource.close();
    }

    public Connection getConnection() throws SQLException {
        return this.hikariDataSource.getConnection();
    }
}
