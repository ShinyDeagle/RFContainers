package com.rifledluffy.chairs.command.commands;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class HelpCommand extends SubCommand {
    @Override
    public void onCommand(Player player, String[] args) {
    	player.sendMessage(ChatColor.GOLD + "==============[Available Commands]==============");
    	player.sendMessage(ChatColor.GOLD + "/rfc or /rfchairs is the main command");
    	player.sendMessage(ChatColor.GOLD + "/rfchairs info | Shows start date of development");
    	player.sendMessage(ChatColor.GOLD + "=================================================");
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