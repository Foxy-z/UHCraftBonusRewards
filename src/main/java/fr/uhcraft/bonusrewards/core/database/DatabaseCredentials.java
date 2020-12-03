package fr.uhcraft.bonusrewards.core.database;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

class DatabaseCredentials {
    private final String host;
    private final String pass;
    private final String dataBaseName;
    private final int port;
    private final String user;

    DatabaseCredentials(String configName) {
        final ConfigurationSection config = Bukkit.getServer().spigot().getConfig().getConfigurationSection(configName);
        this.host = config.getString("host");
        this.user = config.getString("user");
        this.pass = config.getString("pass");
        this.dataBaseName = config.getString("dbname");
        this.port = config.getInt("port");
    }

    String toURI() {
        return "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.dataBaseName + "?useSSL=false";
    }

    String getUser() {
        return this.user;
    }

    String getPass() {
        return this.pass;
    }
}
