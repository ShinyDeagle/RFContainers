package com.rifledluffy.chairs;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public class Chair {
	
	private Block chair;
	private ArmorStand fakeSeat = null;
	private Player seated;
	
	Chair(Player player, Block block) {
		chair = block;
		seated = player;
	}
	
	/*
	 * Getters
	 */
	
	ArmorStand getFakeSeat() {
		return fakeSeat;
	}
	
	Player getSeated() {
		return seated;
	}
	
	Block getBlock() {
		return chair;
	}
	
	Location getLocation() {
		return chair.getLocation();
	}
	
	boolean isOccupied() {
		return !(fakeSeat == null) && !fakeSeat.isEmpty();
	}
	
	/*
	 * Methods
	 */
	
	void setFakeSeat(ArmorStand armorStand) {
		fakeSeat = armorStand;
	}
	
	void clear() {
		if (fakeSeat != null) fakeSeat.remove();
		chair = null;
		fakeSeat = null;
		seated = null;
	}
	
}
