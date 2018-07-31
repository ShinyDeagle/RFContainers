package com.rifledluffy.containers;

import org.bukkit.plugin.java.JavaPlugin;

import com.rifledluffy.containers.command.CommandManager;
import com.rifledluffy.containers.menu.ContainerMenu;

public class RFContainers extends JavaPlugin {
	
	private static RFContainers instance;
	public CommandManager commandManager;
	public ConfigManager cfgManager;
	public ContainerHandler containerHandler;
	public ContainerMenu containerMenu;
	
	@Override
	public void onEnable() {
		
		MetricsLite metrics = new MetricsLite(this);
		
		//Load all the containers
		setInstance(this);
        commandManager = new CommandManager();
        commandManager.setup();
        
		loadConfigManager();
		
		containerHandler = new ContainerHandler();
		containerHandler.loadContainers();
		
		containerMenu = new ContainerMenu();
		getServer().getPluginManager().registerEvents(containerHandler, this);
		getServer().getPluginManager().registerEvents(containerMenu, this);
		
		getLogger().info("Rifle's Containers has been enabled!");
	}
	
	@Override
	public void onDisable() {
		//Store all the containers
		containerHandler.storeContainers();
		
		getLogger().info("Saving Configuration Files!");
		cfgManager.saveData();
		
		getLogger().info("Rifle's Containers has been disabled!");
	}
	
	public void loadConfigManager() {
		cfgManager = new ConfigManager();
		cfgManager.setup();
		cfgManager.reloadConfig();
	}
	
	public ConfigManager getConfigManager() {
		return cfgManager;
	}
	
	public static RFContainers getInstance() {
        return instance;
    }

    private static void setInstance(RFContainers instance) {
    	RFContainers.instance = instance;
    }

}
