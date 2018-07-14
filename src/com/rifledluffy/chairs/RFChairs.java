package com.rifledluffy.chairs;

import org.bukkit.plugin.java.JavaPlugin;

import com.rifledluffy.chairs.command.commands.CommandManager;

public class RFChairs extends JavaPlugin {
	
	private static RFChairs instance;
	public CommandManager commandManager;
	public ConfigManager cfgManager;
	private ChairHandler chairHandler;
	
	@Override
	public void onEnable() {
		setInstance(this);
        commandManager = new CommandManager();
        commandManager.setup();
        
		loadConfigManager();
		
		chairHandler = new ChairHandler();
		chairHandler.clearFakeSeatsFromFile(this);
		getServer().getPluginManager().registerEvents(chairHandler, this);
		
		getLogger().info("Rifle's Chairs has been enabled!");
	}
	
	@Override
	public void onDisable() {
		//Clear all the fake seats that were spawned if players didn't leave their seat
		chairHandler.shutdown(this);
		
		getLogger().info("Saving Configuration Files!");
		cfgManager.saveData();
		
		getLogger().info("Rifle's Chairs has been disabled!");
	}
	
	public void loadConfigManager() {
		cfgManager = new ConfigManager();
		cfgManager.setup();
		cfgManager.reloadConfig();
	}
	
	public ConfigManager getConfigManager() {
		return cfgManager;
	}
	
	public static RFChairs getInstance() {
        return instance;
    }

    private static void setInstance(RFChairs instance) {
    	RFChairs.instance = instance;
    }

}
