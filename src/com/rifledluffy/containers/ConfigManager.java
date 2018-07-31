package com.rifledluffy.containers;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

	private JavaPlugin plugin = RFContainers.getPlugin(RFContainers.class);
	
	/*
	 * Configuration Files
	 */
	
	FileConfiguration config;
    File configFile;
   
    FileConfiguration containers;
    File containersFile;
   
    public void setup() {
           	configFile = new File(plugin.getDataFolder(), "config.yml");
            config = plugin.getConfig();
            config.options().copyDefaults(true);
            saveConfig();
           
            if (!plugin.getDataFolder().exists()) {
                    plugin.getDataFolder().mkdir();
            }
            
            containersFile = new File(plugin.getDataFolder(), "containers.yml");
           
            if (!containersFile.exists()) {
                    try {
                            containersFile.createNewFile();
                    }
                    catch (IOException e) {
                            Bukkit.getServer().getLogger().info("[Rifle's Containers] Could not create containers.yml!");
                    }
            }
           
            containers = YamlConfiguration.loadConfiguration(containersFile);
    }
   
    public FileConfiguration getData() {
            return containers;
    }
   
    public void saveData() {
            try {
                    containers.save(containersFile);
            }
            catch (IOException e) {
                    Bukkit.getServer().getLogger().info("[Rifle's Containers] Could not save containers.yml!");
            }
    }
   
    public void reloadData() {
            containers = YamlConfiguration.loadConfiguration(containersFile);
    }
   
    public FileConfiguration getConfig() {
            return config;
    }
   
    public void saveConfig() {
            try {
                    config.save(configFile);
            }
            catch (IOException e) {
                    Bukkit.getServer().getLogger().info("[Rifle's Containers] Could not save config.yml!");
            }
    }
   
    public void reloadConfig() {
            config = YamlConfiguration.loadConfiguration(configFile);
    }
   
    public PluginDescriptionFile getDesc() {
            return plugin.getDescription();
    }
}