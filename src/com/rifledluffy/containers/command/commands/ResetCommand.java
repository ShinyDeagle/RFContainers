package com.rifledluffy.containers.command.commands;

import org.bukkit.entity.Player;

import com.rifledluffy.containers.RFContainers;

import net.md_5.bungee.api.ChatColor;

public class ResetCommand extends SubCommand {
	
	private RFContainers plugin = RFContainers.getInstance();

	@Override
	public void onCommand(Player player, String[] args) {
		if (!player.hasPermission("rfcontainers.reset")) return;
		plugin.containerHandler.clearData();
		plugin.getLogger().info("Containers Reset!");
		player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Rifle's Containers" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + "Containers Reset");
	}

	@Override
	public String name() {
		return plugin.commandManager.reset;
	}

	@Override
	public String info() {
		return "Resets all chairs";
	}

	@Override
	public String[] aliases() {
		return new String[0];
	}

}