package com.rifledluffy.containers.command.commands;

import com.rifledluffy.containers.RFContainers;
import org.bukkit.entity.Player;

public class InfoCommand extends SubCommand {
	
    private RFContainers plugin = RFContainers.getInstance();

    @Override
    public void onCommand(Player player, String[] args) {
        player.sendMessage("This plugin started development on 7/12/2018!");
    }

    @Override
    public String name() {
        return plugin.commandManager.info;
    }

    @Override
    public String info() {
        return "";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}