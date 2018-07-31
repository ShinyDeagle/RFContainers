package com.rifledluffy.containers.menu;

import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuOption {

	ItemStack icon;
	String identifier;
	String title;
	List<String> lore;
	Map<Enchantment, Integer> enchantments;
	
	MenuOption(ItemStack icon, String id, String title, List<String> lore, Map<Enchantment, Integer> enchantments) {
		this.icon = icon;
		this.identifier = id;
		this.title = title;
		this.lore = lore;
		this.enchantments = enchantments;
	}
	
	/*
	 * Getters
	 */
	
	String getID() {
		return this.identifier;
	}
	
	ItemStack getItem() {
		return this.icon;
	}
	
	String getTitle() {
		return this.title;
	}
	
	List<String> getLore() {
		return this.lore;
	}
	
	Map<Enchantment, Integer> getEnchantments() {
		return this.enchantments;
	}
	
	/*
	 * Setters
	 */
	
	void setID(String id) {
		this.identifier = id;
	}
	
	void setItem(ItemStack item) {
		this.icon = item;
	}
	
	void setItem(Material material) {
		this.icon = new ItemStack(material, 1);
	}
	
	void setItem(Material material, int amount) {
		if (amount < 0) amount = 1;
		this.icon = new ItemStack(material, amount);
	}
	
	void setTitle(String title) {
		this.title = title;
	}
	
	void setLore(List<String> lore) {
		this.lore = lore;
	}
	
	void setEnchantments(Map<Enchantment, Integer> enchantments) {
		this.enchantments = enchantments;
	}
	
	/*
	 * Functions
	 */
	
	ItemStack generate() {
		ItemStack item = icon.clone();
		if (item.getType() == Material.AIR) item.setType(Material.DIRT);
		ItemMeta meta = item.getItemMeta();
		if (this.title != null) meta.setDisplayName(this.title);
		if (this.lore != null) meta.setLore(this.lore);
		if (this.enchantments != null) item.addEnchantments(this.enchantments);
		item.setItemMeta(meta);
		return item;
	}
	
	
}
