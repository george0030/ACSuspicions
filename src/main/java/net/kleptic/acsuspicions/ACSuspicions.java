package net.kleptic.acsuspicions;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.*;

import java.io.*;
import java.nio.file.Files;
import java.sql.SQLException;

public final class ACSuspicions extends Plugin {
    
    private ACSCommand command;
    private ACSDatabase database;
    private Configuration config;
    
    @Override
    public void onEnable() {
        
        if (!getDataFolder().exists())
                getDataFolder().mkdir();

            File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            this.database = ACSSQL.connect(this, config.getString("mysql.host"), config.getInt("mysql.port"),
                                           config.getString("mysql.user"), config.getString("mysql.password"),
                                           config.getString("mysql.database"), -1,-1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return;
        }
        
        this.command = new ACSCommand(this, "acs");
        getProxy().getPluginManager().registerCommand(this, command);
    
    }
    
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    
    public ACSDatabase getDatabase() {
        return this.database;
    }
    
    public Configuration getConfig() {
        return this.config;
    }
    
    public void reload(){
        getProxy().getPluginManager().unregisterCommand(command);
        onEnable();
    }
}
