package com.rifledluffy.containers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class ItemPile {
	
	private RFContainers plugin = RFContainers.getPlugin(RFContainers.class);
	
	Material material;
	ItemMeta meta;
	int amount;
	
	ItemPile(Material item, int amount) {
		this.material = item;
		this.amount = amount;
		this.meta = plugin.getServer().getItemFactory().getItemMeta(material);
	}
	
	ItemPile(ItemStack item) {
		this.material = item.getType();
		this.meta = item.getItemMeta();
		if (this.meta == null) meta = plugin.getServer().getItemFactory().getItemMeta(material);
		this.amount = item.getAmount();
	}
	
	/*
	 * Getters
	 */
	
	Material getMaterial() {
		return material;
	}
	
	ItemMeta getMeta() {
		return meta;
	}
	
	ItemStack getItem() {
		ItemStack item = new ItemStack(material, 1);
		item.setItemMeta(meta);
		return item;
	}
	
	ItemStack getItemWithAmount() {
		ItemStack item = new ItemStack(material, amount);
		ItemMeta details = meta;
		List<String> lore = details.getLore();
		if (containsDetails(lore)) lore.remove(0);
		details.setLore(lore);
		item.setItemMeta(details);
		return item;
	}
	
	ItemStack getDescribedItem() {
		ItemStack item = new ItemStack(material, 1);
		List<String> lore;
		ItemMeta data = meta.clone();
		
		if (data.hasLore()) {
			lore = data.getLore();
			if (containsDetails(lore)) lore.remove(0);
			if (data.hasDisplayName()) lore.add(0, ChatColor.GRAY + "x" + amount + " - " + data.getDisplayName() + ChatColor.BLACK + " IPLE");
			else lore.add(0, ChatColor.GRAY + "x" + amount + " - " + item.getType().name() + ChatColor.BLACK + " IPLE");
		}
		else {
			lore = new ArrayList<String>();
			if (data.hasDisplayName()) lore.add(0, ChatColor.GRAY + "x" + amount + " - " + data.getDisplayName() + ChatColor.BLACK + " IPLE");
			else lore.add(0, ChatColor.GRAY + "x" + amount + " - " + item.getType().name() + ChatColor.BLACK + " IPLE");
		}
		data.setLore(lore);
		
		item.setItemMeta(data);
		return item;
	}
	
	boolean containsDetails(List<String> lore) {
		if (lore == null) return false;
		if (lore.get(0).contains(" IPLE")) return true;
		return false;
	}
	
	boolean hasMeta() {
		return meta != null;
	}
	
	int getAmount() {
		return amount;
	}
	
	/*
	 * Setters
	 */
	
	void setItem(Material material) {
		this.material = material;
	}
	
	void setAmount(int amount) {
		this.amount = amount;
	}
	
	void addAmount(int amount) {
		this.amount += amount;
	}
	
	void remAmount(int amount) {
		if (this.amount <= amount) amount = this.amount;
		this.amount -= amount;
	}
	
	void setMeta(ItemMeta meta) {
		this.meta = meta;
	}
}
