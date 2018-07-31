package com.rifledluffy.containers.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import com.rifledluffy.containers.*;

import net.md_5.bungee.api.ChatColor;

public class ContainerMenu implements Listener {
	
	RFContainers plugin = RFContainers.getPlugin(RFContainers.class);
	ConfigManager configManager = plugin.getConfigManager();
	ContainerHandler containerHandler = plugin.containerHandler;

	/*
	 * Menu Options
	 */
	
	List<MenuOption> menuOptions = new ArrayList<MenuOption>();
	
	/*
	 * Configuration
	 */
	
	Inventory result = generateResult();
	int size = 45;
	int startPosition = 10;
	int skipInterval = 1;
	//Deprecated in 1.13
	ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
	
	/*
	 * External Data
	 */
	
	int maxPileSize = configManager.getConfig().getInt("max-pile-size");
	int maxSize = configManager.getConfig().getInt("max-size");
	
	/*
	 * Generate Menu Options
	 */
	
	public ContainerMenu() {
		MenuOption create1 = new MenuOption(new ItemStack(Material.BLACK_SHULKER_BOX, 1), "Generate Container", ChatColor.GOLD + "Make a Container!", null, null);
		MenuOption create2 = new MenuOption(new ItemStack(Material.BROWN_SHULKER_BOX, 1), "Generate Container", ChatColor.GOLD + "Make a Container!", null, null);
		MenuOption create3 = new MenuOption(new ItemStack(Material.BLUE_SHULKER_BOX, 1), "Generate Container", ChatColor.GOLD + "Make a Container!", null, null);
		MenuOption create4 = new MenuOption(new ItemStack(Material.CYAN_SHULKER_BOX, 1), "Generate Container", ChatColor.GOLD + "Make a Container!", null, null);
		MenuOption create5 = new MenuOption(new ItemStack(Material.GRAY_SHULKER_BOX, 1), "Generate Container", ChatColor.GOLD + "Make a Container!", null, null);
		MenuOption create6 = new MenuOption(new ItemStack(Material.GREEN_SHULKER_BOX, 1), "Generate Container", ChatColor.GOLD + "Make a Container!", null, null);
		MenuOption create7 = new MenuOption(new ItemStack(Material.LIGHT_BLUE_SHULKER_BOX, 1), "Generate Container", ChatColor.GOLD + "Make a Container!", null, null);
		MenuOption create8 = new MenuOption(new ItemStack(Material.LIME_SHULKER_BOX, 1), "Generate Container", ChatColor.GOLD + "Make a Container!", null, null);
		MenuOption create9 = new MenuOption(new ItemStack(Material.MAGENTA_SHULKER_BOX, 1), "Generate Container", ChatColor.GOLD + "Make a Container!", null, null);
		MenuOption create10 = new MenuOption(new ItemStack(Material.ORANGE_SHULKER_BOX, 1), "Generate Container", ChatColor.GOLD + "Make a Container!", null, null);
		MenuOption create11 = new MenuOption(new ItemStack(Material.PINK_SHULKER_BOX, 1), "Generate Container", ChatColor.GOLD + "Make a Container!", null, null);
		MenuOption create12 = new MenuOption(new ItemStack(Material.PURPLE_SHULKER_BOX, 1), "Generate Container", ChatColor.GOLD + "Make a Container!", null, null);
		MenuOption create13 = new MenuOption(new ItemStack(Material.RED_SHULKER_BOX, 1), "Generate Container", ChatColor.GOLD + "Make a Container!", null, null);
		MenuOption create14 = new MenuOption(new ItemStack(Material.WHITE_SHULKER_BOX, 1), "Generate Container", ChatColor.GOLD + "Make a Container!", null, null);
		MenuOption create15 = new MenuOption(new ItemStack(Material.YELLOW_SHULKER_BOX, 1), "Generate Container", ChatColor.GOLD + "Make a Container!", null, null);
		menuOptions.add(create1);
		menuOptions.add(create2);
		menuOptions.add(create3);
		menuOptions.add(create4);
		menuOptions.add(create5);
		menuOptions.add(create6);
		menuOptions.add(create7);
		menuOptions.add(create8);
		menuOptions.add(create9);
		menuOptions.add(create10);
		menuOptions.add(create11);
		menuOptions.add(create12);
		menuOptions.add(create13);
		menuOptions.add(create14);
		menuOptions.add(create15);
	}
	
	@EventHandler 
    public void onDrag(InventoryDragEvent event) {
    	if (!(event.getWhoClicked() instanceof Player)) return;
    	if (event.getInventory() == null) return;
    	
    	Player player = (Player) event.getWhoClicked();
    	InventoryView view = event.getView();
    	if (view.getBottomInventory().equals(player.getInventory()) && view.getTopInventory().getName().equals("Create a Container!")) {
    		event.setCancelled(true);
    		player.updateInventory();
    	}
    }
	
	@EventHandler
	public void onClickInv(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) return;
    	if (event.getClickedInventory() == null) return;
    	
    	Player player = (Player) event.getWhoClicked();
    	Inventory clickedInv = event.getClickedInventory();
    	InventoryView view = event.getView();
    	if (!view.getTopInventory().getName().equals("Create a Container!")) return;
    	if (view.getBottomInventory().equals(player.getInventory()) && player.getInventory().equals(clickedInv)) {
    		if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
    			if (view.getTopInventory().getName().equals("Create a Container!")) {
    				event.setCancelled(true);
    				return;
    			}
        	}
    	}
    	
    	if (event.getAction() != InventoryAction.PICKUP_ALL) {
    		event.setCancelled(true);
    		return;
    	}
    	
    	ItemStack clicked = event.getCurrentItem();
    	MenuOption option = getMenuOptionFromItemStack(clicked);
    	if (option == null) {
    		event.setCancelled(true);
    		return;
    	}
    	
    	switch (option.getID()) {
    		default:
    			break;
    		case "Generate Container":
    			Container container = new Container(maxPileSize, maxSize);
    			ItemStack item = container.generateContainer();
    			item.setType(event.getCurrentItem().getType());
    			ItemMeta meta = item.getItemMeta();
    			meta.setDisplayName(ChatColor.GOLD + "Container");
    			meta.addEnchant(Enchantment.DURABILITY, 1, true);
    			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    			item.setItemMeta(meta);
    			PlayerInventory inv = player.getInventory();
    			containerHandler.containers.add(container);
    			containerHandler.identifiers.add(container.getID());
    			inv.addItem(item);
    			event.setCancelled(true);
    			player.closeInventory();
    			break;
    	}
    	
	}
	
	public Inventory generateResult() {
		Inventory menu = Bukkit.createInventory(null, size,"Create a Container!");
		int itemIndex = 0;
		if (menuOptions != null && menuOptions.size() > 0) {
			for (int i = 0; itemIndex < menuOptions.size(); i += skipInterval) {
				if ((startPosition + i) % 9 == 8) i += 2;
				menu.setItem(startPosition + i, menuOptions.get(itemIndex).generate());
				itemIndex++;
			}
			for (int i = 0; i < size; i++) if (menu.getItem(i) == null) menu.setItem(i, filler);
		}
		return menu;
	}
	
	MenuOption getMenuOptionFromItemStack(ItemStack item) {
		if (menuOptions == null) return null;
		for (MenuOption option : menuOptions) {
			if (item.equals(option.generate())) return option;
		}
		return null;
	}
	
	int getValidSize() {
		int index = size;
		if (index <= 0) index = 9;
	    int quotient = (int)Math.ceil(index / 9.0);
	    return quotient > 5 ? 53: quotient * 9;
	}
}
