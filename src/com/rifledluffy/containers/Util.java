package com.rifledluffy.containers;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Util {
	
	private static RFContainers plugin = RFContainers.getPlugin(RFContainers.class);
	private static ConfigManager configManager = plugin.getConfigManager();

	public static ConfigManager getConfigManager() {
		return configManager;
	}
	
	public static void debug(String message) {
		for (Player player : plugin.getServer().getOnlinePlayers()) player.sendMessage(message);
	}
	
	public static void debug(int message) {
		for (Player player : plugin.getServer().getOnlinePlayers()) player.sendMessage(Integer.toString(message));
	}
	
	public static void debug(Double message) {
		for (Player player : plugin.getServer().getOnlinePlayers()) player.sendMessage(Double.toString(message));
	}
	
	public static void debug(Boolean message) {
		for (Player player : plugin.getServer().getOnlinePlayers()) player.sendMessage(Boolean.toString(message));
	}
	
	static boolean isContainerMaterial(Material material) {
		if (material == Material.BLACK_SHULKER_BOX
			|| material == Material.BLUE_SHULKER_BOX
			|| material == Material.BROWN_SHULKER_BOX
			|| material == Material.CYAN_SHULKER_BOX
			|| material == Material.GRAY_SHULKER_BOX
			|| material == Material.GREEN_SHULKER_BOX
			|| material == Material.LIGHT_BLUE_SHULKER_BOX
			|| material == Material.LIME_SHULKER_BOX
			|| material == Material.MAGENTA_SHULKER_BOX
			|| material == Material.PURPLE_SHULKER_BOX
			|| material == Material.ORANGE_SHULKER_BOX
			|| material == Material.PINK_SHULKER_BOX
			|| material == Material.PURPLE_SHULKER_BOX
			|| material == Material.RED_SHULKER_BOX
			|| material == Material.WHITE_SHULKER_BOX
			|| material == Material.YELLOW_SHULKER_BOX)
			return true;
		return false;
	}
}
