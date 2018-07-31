package com.rifledluffy.containers.command.commands;

import org.bukkit.entity.Player;

import com.rifledluffy.containers.RFContainers;

import net.md_5.bungee.api.ChatColor;

public class ReloadCommand extends SubCommand {
	
	private RFContainers plugin = RFContainers.getInstance();

	@Override
	public void onCommand(Player player, String[] args) {
		if (!player.hasPermission("rfcontainers.reload")) return;
		plugin.reloadConfig();
		player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Rifle's Containers" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + "Config Reloaded");
	}

	@Override
	public String name() {
		return plugin.commandManager.reload;
	}

	@Override
	public String info() {
		return "Reloads the config";
	}

	@Override
	public String[] aliases() {
		return new String[0];
	}

}
