package com.rifledluffy.containers.command.commands;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class HelpCommand extends SubCommand {
    @Override
    public void onCommand(Player player, String[] args) {
    	player.sendMessage(ChatColor.GRAY + "==============[Available Commands]==============");
    	player.sendMessage(ChatColor.GRAY + "/rfc or /rfcontainers is the main command");
    	player.sendMessage(ChatColor.GRAY + "/rfcontainers info | Shows start date of development");
    	player.sendMessage(ChatColor.GRAY + "/rfcontainers reload | Reloads the Config");
    	player.sendMessage(ChatColor.GRAY + "================================================");
    }

    @Override
    public String name() {
        return "help";
    }

    @Override
    public String info() {
        return null;
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}