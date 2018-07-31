package com.rifledluffy.containers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class Container {

	Inventory physicalInventory;
	List<ItemPile> contents = new ArrayList<ItemPile>();
	ItemStack container;
	int maxSize;
	int maxPileSize;
	UUID identifier;
	Location location;
	List<Player> viewers = new ArrayList<Player>();
	
	/*
	 * Partial Construction; Create a new Container from scratch
	 */
	
	public Container(int itemSize, int size) {
		if (size > 54) size = 54;
		if (itemSize <= 0) itemSize = 256;
		if (itemSize >= 1000000000) itemSize = 1000000000;
		this.maxSize = size;
		this.maxPileSize = itemSize;
		this.identifier = UUID.randomUUID();
		setValidSize();
	}
	
	/*
	 * Complete Construction; Rebuilding the Container from existing ones.
	 */
	
	public Container(UUID id, Location location, int itemSize, int size, List<ItemStack> items) {
		this.location = location;
		if (itemSize <= 0) itemSize = 256;
		if (itemSize >= 1000000000) itemSize = 1000000000;
		this.maxPileSize = itemSize;
		this.maxSize = size;
		this.identifier = id;
		rebuildInventory(items);
		setValidSize();
	}
	
	/*
	 * Getters
	 */
	
	List<ItemPile> getContents() {
		return contents;
	}
	
	ItemStack getItem() {
		return container;
	}
	
	int getMaxSize() {
		return maxSize;
	}
	
	int getMaxPileSize() {
		return maxPileSize;
	}
	
	Block getBlock() {
		return location.getBlock();
	}
	
	Location getLocation() {
		return location;
	}
	
	public UUID getID() {
		return identifier;
	}
	
	Inventory getInventory() {
		return physicalInventory;
	}
	
	/*
	 * Setters
	 */
	
	void setMaxSize(int amount) {
		this.maxSize = amount;
		setValidSize();
	}
	
	void setMaxPileSize(int amount) {
		if (amount <= 0) amount = 256;
		this.maxPileSize = amount;
	}
	
	void setLocation(Location location) {
		this.location = location;
	}
	
	void setInventory(Inventory inventory) {
		this.physicalInventory = inventory;
	}
	
	void addViewer(Player player) {
		this.viewers.add(player);
	}
	
	/*
	 * Recreate the inventory
	 */
	
	void rebuildInventory(List<ItemStack> items) {
		for (ItemStack item : items) {
			if (item == null) continue;
			if (item.getType() == Material.AIR) continue;
			ItemPile pile;
			if (item.hasItemMeta()) pile = new ItemPile(item);
			else pile = new ItemPile(item.getType(), item.getAmount());
			contents.add(pile);
		}
	}
	
	/*
	 * Add/Remove Item
	 */
	
	//Used in conjunction with certain utility functions.
	int index = 0;
	
	int addItem(ItemStack item) {
		if (isFullContainer()) return 0;
		if (itemExists(item)) {
			ItemPile pile = getExistingPile(item);
			if (pile == null) return 0;
			if (pile.getAmount() >= maxPileSize) return 0;
			
			pile.addAmount(item.getAmount());
			int leftover = 0;
			if (pile.getAmount() > maxPileSize) {
				leftover = pile.getAmount() - maxPileSize;
				pile.setAmount(maxPileSize);
			}
			
			contents.set(index, pile);
			if (leftover > 0) return leftover;
			else return -1;
		} else {
			ItemPile pile = new ItemPile(item);
			contents.add(pile);
			return -1;
		}
	}
	
	ItemStack takeItem(ItemStack item, int amount) {
		if (!itemExists(item)) return null;
		ItemPile pile = getExistingPile(item);
		if (pile == null) return null;
		int actual = 0;
		ItemStack finalItem;
		
		if (amount > pile.getAmount()) actual = amount - pile.getAmount();
		pile.remAmount(amount);
		finalItem = pile.getItemWithAmount();
		
		if (pile.getAmount() == 0) contents.remove(pile);
		
		if (actual != 0) finalItem.setAmount(64 - actual);
		else finalItem.setAmount(amount);
		return finalItem;
	}
	
	void removeItem(ItemStack item) {
		if (itemExists(item)) {
			ItemPile pile = getExistingPile(item);
			if (pile == null) return;
			contents.remove(index);
		}
		else return;
	}
	
	/*
	 * Generate Container
	 */
	
	public ItemStack generateContainer() {
		ItemStack item = new ItemStack(Material.BROWN_SHULKER_BOX);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		UUID id = identifier;
		
		if (contents == null) {
			lore.add(ChatColor.BLACK + id.toString());
			meta.setLore(lore);
			meta.setDisplayName(ChatColor.GOLD + "Container");
			meta.addEnchant(Enchantment.DURABILITY, 1, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			item.setItemMeta(meta);
			return item;
		} else {
			for (ItemPile pile : contents) {
				if (pile == null) continue;
				if (pile.hasMeta() && pile.getMeta().hasDisplayName()) lore.add(ChatColor.GRAY + "x"+ pile.getAmount() + " - " + pile.getMeta().getDisplayName());
				else lore.add(ChatColor.GRAY + "x" + pile.getAmount() + " - " + pile.getMaterial().name());
			}
			lore.add(ChatColor.BLACK + id.toString());
			meta.setLore(lore);
			meta.setDisplayName(ChatColor.GOLD + "Container");
			meta.addEnchant(Enchantment.DURABILITY, 1, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			item.setItemMeta(meta);
			return item;
		}
	}
	
	/*
	 * Utilities
	 */
	
	boolean itemExists(ItemStack item) {
		if (item.hasItemMeta()) {
			if (contents == null) return false;
			for (ItemPile pile: contents) {
				ItemStack pileStack = pile.getItem();
				pileStack.setAmount(item.getAmount());
				if (pileStack.getItemMeta().equals(item.getItemMeta())) return true;
			}
			return false;
		}
		return itemExists(item.getType());
	}
	
	boolean itemExists(Material material, ItemMeta meta) {
		if (contents == null) return false;
		for (ItemPile pile: contents) {
			ItemMeta pileMeta = pile.getMeta();
			Material pileMat = pile.getMaterial();
			if (pileMeta.equals(meta) && pileMat == material) return true;
		}
		return false;
	}
	
	boolean itemExists(Material material) {
		if (contents == null) return false;
		for (ItemPile pile: contents) {
			if (pile.getMaterial().equals(material)) return true;
		}
		return false;
	}
	
	boolean isEmptyContainer() {
		return contents != null && contents.size() == 0;
	}
	
	boolean isFullContainer() {
		return contents != null && contents.size() == maxSize;
	}
	
	boolean isFullPile(ItemPile pile) {
		return pile.getAmount() == maxPileSize;
	}
	
	ItemPile getExistingPile(ItemStack item) {
		for (int i = 0; i < contents.size(); i++) {
			ItemPile pile = contents.get(i);
			if (item.hasItemMeta()) {
				if (pile.getMeta().equals(item.getItemMeta()) && pile.getMaterial().equals(item.getType())) {
					index = i;
					return pile;
				}
			} else {
				if (pile.getMaterial().equals(item.getType())) {
					index = i;
					return pile;	
				}
			}
		}
		return null;
	}
	
	void setValidSize() {
		int size = this.maxSize;
		if (size <= 0) this.maxSize = 9;
	    int quotient = (int)Math.ceil(size / 9.0);
	    this.maxSize = quotient > 5 ? 53: quotient * 9;
	}
	
	void clear() {
		contents = null;
		container = null;
		maxSize = 0;
		maxPileSize = 0;
		identifier = null;
		location = null;
	}
}
