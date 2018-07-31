package com.rifledluffy.containers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.rifledluffy.containers.menu.ContainerMenu;

import net.md_5.bungee.api.ChatColor;

public class ContainerHandler implements Listener {
	
	private RFContainers plugin = RFContainers.getPlugin(RFContainers.class);
	private ConfigManager configManager = plugin.getConfigManager();
	
	public List<Container> containers = new ArrayList<Container>();
	public List<UUID> identifiers = new ArrayList<UUID>();
	public ContainerMenu containerMenu = new ContainerMenu();
	
	/**
	 * Right Clicking with a Container
	 */
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		if (event.isCancelled()) return;
		Block block = event.getClickedBlock();
		Player player = event.getPlayer();
		ItemStack eventItem = event.getItem();
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (!player.hasPermission("rfcontainers.use")) return;
			if (!Util.isContainerMaterial(block.getType())) return;
			if (!isContainer(block)) return;
			Container container = getContainerFromBlock(block);
			accessContainer(player, container);
			event.setCancelled(true);
		} else if (event.getAction() == Action.LEFT_CLICK_BLOCK && player.isSneaking()) {
			if (eventItem == null) return;
			if (!player.hasPermission("rfcontainers.create")) return;
			if (!Util.isContainerMaterial(eventItem.getType())) return;
			event.setCancelled(true);
			eventItem.setAmount(0);
			player.openInventory(containerMenu.generateResult());
		}
	}
	
    /**
     * Cancel piston events with containers
     */

    @EventHandler
    public void pistonExtend(BlockPistonExtendEvent event) {
    	if (event.isCancelled()) return;
        for (Block block : event.getBlocks()) {
        	for (Container container: containers) {
    			if (samePosition(block, container.getBlock())) {
        			event.setCancelled(true);
        			break;
        		}
    		}
        }
    }

    @EventHandler
    public void pistonRetract(BlockPistonRetractEvent event) {
    	if (event.isCancelled()) return;
    	for (Block block : event.getBlocks()) {
    		for (Container container: containers) {
    			if (samePosition(block, container.getBlock())) {
        			event.setCancelled(true);
        			break;
        		}
    		}
        }
    }
    
    /*
     * Place Container
     */
    
	@EventHandler
	public void blockPlace(BlockPlaceEvent event) {
		if (event.isCancelled()) return;
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
		if (isContainer(item)) {
			Block placed = event.getBlockPlaced();
			UUID id = getIDFromItem(item);
			if (id == null) return;
			Container container = getContainerFromID(id);
			if (container == null) return;
			container.setLocation(placed.getLocation());
			event.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
		}
	}
	
	/*
     * Break Container
     */
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		Player player = event.getPlayer();
		if (player.isSneaking() && isContainer(block)) {
			Container container = getContainerFromBlock(block);
			ItemStack item = container.generateContainer();
			item.setType(block.getType());
			player.getInventory().addItem(item);
			container.setLocation(null);
			event.setDropItems(false);
		} else {
			if (containers.size() == 0) return;
			for (Container container: containers) {
				if (container == null) continue;
				if (container.location == null) continue;
				if (container.getBlock() == null) continue;
				if (samePosition(block, container.getBlock())) {
					event.setCancelled(true);
					break;
				}
			}
		}
	}
	
	/*
     * Open/Close checks on containers
     */
	
	@EventHandler
	public void onOpen(InventoryOpenEvent event) {
		if (!(event.getPlayer() instanceof Player)) return;
		Player player = (Player) event.getPlayer();
		if (containers.size() == 0) return;
		for (Container container: containers) {
			if (container == null) continue;
			if (container.getInventory() == null) continue;
			if (container.getInventory().equals(event.getView().getTopInventory())) container.addViewer(player);
		}
	}
    
    /*
     * Access Container
     */
    
    void accessContainer(Player player, Container container) {
    	Inventory inventory = generateInventory(container);
    	container.setInventory(inventory);
    	player.openInventory(inventory);
    }
    
    Inventory generateInventory(Container container) {
    	if (container.getInventory() != null) return container.getInventory();
    	List<ItemPile> contents = container.getContents();
    	Inventory inventory = Bukkit.createInventory(null, container.getMaxSize(), "Container's Contents");
    	if (contents != null) for (int i = 0; i < contents.size(); i++) inventory.setItem(i, contents.get(i).getDescribedItem());
    	return inventory;
    }
    
    @EventHandler 
    public void onDrag(InventoryDragEvent event) {
    	if (!(event.getWhoClicked() instanceof Player)) return;
    	if (event.getInventory() == null) return;
    	
    	Player player = (Player) event.getWhoClicked();
    	InventoryView view = event.getView();
    	if (view.getBottomInventory().equals(player.getInventory()) && view.getTopInventory().getName().equals("Container's Contents")) {
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
    	if (view.getBottomInventory().equals(player.getInventory()) && player.getInventory().equals(clickedInv)) {
    		if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
    			if (view.getTopInventory().getName().equals("Container's Contents")) event.setCancelled(true);
        	}
    	}
    	
    	Container container = getContainerFromInventory(clickedInv);
    	if (container == null) return;
    	if (container.getLocation() == null) player.closeInventory();
    	
    	InventoryAction action = event.getAction();

    	PlayerInventory inventory = player.getInventory();
    	ItemStack item = event.getCursor();
    	ItemStack clicked;
    	ItemStack finalItem;
    	if (fullInventory(inventory)) {
    		event.setCancelled(true);
    		return;
    	}
    	switch (action) {
			default:
				event.setCancelled(true);
				return;
			//Take a stack of items;
			case PICKUP_ALL:
				clicked = stripDetails(event.getCurrentItem());
				if (clicked == null) return;
				if (!container.itemExists(clicked)) {
					event.setCancelled(true);
					updateInventory(container);
					return;
				}
				finalItem = container.takeItem(clicked, clicked.getType().getMaxStackSize());
				if (finalItem == null) {
					item.setAmount(0);
					event.setCancelled(true);
					updateInventory(container);
					return;
				}
				inventory.addItem(finalItem);
				item.setAmount(0);
				updateInventory(container);
				event.setCancelled(true);
				break;
			case PICKUP_HALF:
				clicked = stripDetails(event.getCurrentItem());
				if (clicked == null) return;
				if (!container.itemExists(clicked)) {
					event.setCancelled(true);
					updateInventory(container);
					return;
				}
				finalItem = container.takeItem(clicked, 1);
				if (finalItem == null) {
					event.setCancelled(true);
					updateInventory(container);
					return;
				}
				inventory.addItem(finalItem);
				item.setAmount(0);
				updateInventory(container);
				event.setCancelled(true);
				break;
			//Add to a pile of items;
			case PLACE_ALL:
				if (item.getDurability() != 0) {
					event.setCancelled(true);
					return;
				}
				int left = container.addItem(item);
				if (left > 0) item.setAmount(left);
				else if (left == -1) item.setAmount(0);
				updateInventory(container);
				event.setCancelled(true);
				break;
			case SWAP_WITH_CURSOR:
				clicked = stripDetails(event.getCurrentItem());
				if (clicked == null) {
					event.setCancelled(true);
					return;
				}
				if (!container.itemExists(clicked)) {
					event.setCancelled(true);
					updateInventory(container);
					return;
				}
				ItemStack clone = item.clone();
				clone.setAmount(clicked.getAmount());
				if (!clicked.equals(clone)) {
					event.setCancelled(true);
					return;
				}
				int left1 = container.addItem(item);
				if (left1 > 0) item.setAmount(left1);
				else if (left1 == -1) item.setAmount(0);
				updateInventory(container);
				event.setCancelled(true);
				break;
    	}
    }
    
    void updateInventory(Container container) {
    	List<ItemPile> contents = container.getContents();
    	Inventory inventory = container.getInventory();
    	for (int i = 0; i < container.getMaxSize(); i++) inventory.setItem(i, new ItemStack(Material.AIR));
    	for (int i = 0; i < contents.size(); i++) inventory.setItem(i, contents.get(i).getDescribedItem());
    }
	
	/*
	 * Cleaning and Clearing
	 */
	
	void clearContainer(Container container) {
		container.clear();
	}
	
	/*
	 * Cleaning and Clearing
	 */
	
	void storeContainers() {
		FileConfiguration data = configManager.getData();
		List<String> ids = new ArrayList<String>();
		plugin.getLogger().info("Saving " + containers.size() + " Containers!");
		for (Container container : containers) {
			//Location info
			Location location = container.getLocation();
			UUID id = container.getID();
			UUID copy = id;
			ids.add(copy.toString());
			if (location != null) {
				int xBlock = location.getBlockX();
				int yBlock = location.getBlockY();
				int zBlock = location.getBlockZ();
				World world = location.getWorld();
				String worldName = world.getName();

				data.set("containers." + id +".location", worldName + "," + xBlock + "," + yBlock + "," + zBlock);
			} else {
				data.set("containers." + id +".location", "none");
			}
			data.set("containers." + id +".maxSize", container.getMaxSize());
			data.set("containers." + id +".maxPileSize", container.getMaxPileSize());

			if (container.getContents() == null) {
				data.set("containers." + id +".itemCount", "-1");
				continue;
			}
			if (container.getContents().size() >= 1) {
				int i = 0;
				for (ItemPile pile : container.getContents()) {
					ItemStack item = pile.getItem();
					item.setAmount(pile.getAmount());
					data.set("containers." + id +".contents." + i, item);
					i++;
				}
				data.set("containers." + id +".itemCount", i);
			}
		}
		data.set("UUIDs", ids);
		plugin.getLogger().info("All " + containers.size() + " Saved!");
	}
	
	/*
	 * Clear all Data [Very Destructive!]
	 */
	
	public void clearData() {
		FileConfiguration data = configManager.getData();
		List<String> emptyList = new ArrayList<String>();
		data.set("UUIDs", emptyList);
		data.set("containers", emptyList);
		if (containers.size() > 0) {
			for (Container container: containers) container.contents.clear();
		}
	}
	
	/*
	 * Loading
	 */
	
	public void loadContainers() {
		FileConfiguration data = configManager.getData();
		List<String> containerIDs = data.getStringList("UUIDs");
		List<String> emptyList = new ArrayList<String>();
		plugin.getLogger().info("Loading " + containerIDs.size() + " containers...");
		for (String id : containerIDs) {
			
			/*
			 * World Data | Container
			 */
			
			String locationData = data.getString("containers." + id +".location");
			Location containerLoc = null;
			if (locationData.equalsIgnoreCase("none")) {
				
			} else {
				String[] args = locationData.split(",");
				World world = Bukkit.getServer().getWorld(args[0]);
				int xPos = Integer.valueOf(args[1]);
				int yPos = Integer.valueOf(args[2]);
				int zPos = Integer.valueOf(args[3]);
				
				containerLoc = new Location(world, xPos, yPos, zPos);
			}
			
			/*
			 * Container Attributes
			 */
			
			int maxSize = data.getInt("containers." + id +".maxSize");
			int maxPileSize = data.getInt("containers." + id +".maxPileSize");
			
			/*
			 * Items
			 */
			
			List<ItemStack> items = new ArrayList<ItemStack>();
			int itemCount = data.getInt("containers." + id +".itemCount");
			if (itemCount != -1) {
				for (int i = 0; i < itemCount; i++) {
					ItemStack item = data.getItemStack("containers." + id +".contents." + i);
					items.add(item);
				}
			}
			
			/*
			 * Reconstruction
			 */
			Container container;
			String copy = id;
			UUID identification = UUID.fromString(copy);
			
			if (containerLoc == null) {
				container = new Container(identification, null, maxPileSize, maxSize, items);
				containers.add(container);
				identifiers.add(container.getID());
			} else if (Util.isContainerMaterial(containerLoc.getBlock().getType())) {
				container = new Container(identification, containerLoc, maxPileSize, maxSize, items);
				containers.add(container);
				identifiers.add(container.getID());
			}
		}
		data.set("containers", emptyList);
		data.set("UUIDs", emptyList);
		plugin.getLogger().info("All " + containerIDs.size() + " Loaded!");
	}
	
	/**
	 * Utilities
	 */
	
	boolean fullInventory(Inventory inventory) {
		for (int i = 0; i < inventory.getSize(); i++) {
			if (inventory.getItem(i) == null) return false;
			if (inventory.getItem(i).getType() == Material.AIR) return false;
		}
		return true;
	}
	
	boolean isContainer(ItemStack item) {
		if (!Util.isContainerMaterial(item.getType())) return false;
		if (!item.hasItemMeta()) return false;
		ItemMeta meta = item.getItemMeta();
		if (!meta.hasLore()) return false;
		List<String> lore = item.getItemMeta().getLore();
		int size = lore.size();
		String id = lore.get(size - 1);
		id = ChatColor.stripColor(id);
		
		if (identifiers.contains(UUID.fromString(id))) return true;
		return false;
	}
	
	boolean isContainer(Block block) {
		if (containers.size() == 0) return false;
		if (containers == null) return false;
		for (Container container : containers) {
			if (container == null) continue;
			if (container.location == null) continue;
			if (container.getBlock() == null) continue;
			if (samePosition(container.getBlock(), block)) return true;
		}
		return false;
	}
	
	UUID getIDFromItem(ItemStack item) {
		if (!item.hasItemMeta()) return null;
		if (!Util.isContainerMaterial(item.getType())) return null;
		
		List<String> lore = item.getItemMeta().getLore();
		int size = lore.size();
		String id = lore.get(size - 1);
		id = ChatColor.stripColor(id);
		
		if (identifiers.contains(UUID.fromString(id))) return UUID.fromString(id);
		return null;
	}
	
	ItemStack stripDetails(ItemStack item) {
		if (!item.hasItemMeta()) return null;
		ItemMeta meta = item.getItemMeta();
		if (!meta.hasLore()) return null;
		
		List<String> lore = meta.getLore();
		lore.remove(0);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	Container getContainerFromID(UUID id) {
		for (Container container : containers) {
			if (container == null) continue;
			if (container.getID().equals(id)) return container;
		}
		return null;
	}
	
	Container getContainerFromBlock(Block block) {
		for (Container container : containers) {
			if (container == null) continue;
			if (container.location == null) continue;
			if (container.getBlock() == null) continue;
			if (samePosition(container.getBlock(), block)) return container;
		}
		return null;
	}
	
	Container getContainerFromInventory(Inventory inventory) {
		for (Container container : containers) {
			if (container == null) continue;
			if (inventory.equals(container.getInventory())) return container;
		}
		return null;
	}
	
	boolean samePosition(Block block, Block target) {
		return block.getX() == target.getX() && block.getY() == target.getY() && block.getZ() == target.getZ();
	}

	String getCardinalDirection(Location loc) {
		float y = loc.getYaw();
        if (y < 0) y += 360;
        y %= 360;
        if (y <= 45 || y >= 315) return "south";
        if (y >= 45 && y <= 135) return "west";
        if (y >= 135 && y <= 225) return "north";
		return "east";
	}
	
	Block getBlockFromDirection(Block block, String direction) {
		if (direction.equalsIgnoreCase("north")) return block.getRelative(BlockFace.NORTH);
		else if (direction.equalsIgnoreCase("west")) return block.getRelative(BlockFace.WEST);
		else if (direction.equalsIgnoreCase("east")) return block.getRelative(BlockFace.EAST);
		else if (direction.equalsIgnoreCase("south")) return block.getRelative(BlockFace.SOUTH);
		else if (direction.equalsIgnoreCase("down")) return block.getRelative(BlockFace.DOWN);
		else if (direction.equalsIgnoreCase("up")) return block.getRelative(BlockFace.UP);
		return block;
	}
	
	boolean canFitPlayer(Block block) {
		return block.getType() == Material.AIR && block.getRelative(BlockFace.UP).getType() == Material.AIR;
	}
	
	boolean safePlace(Block block) {
		return block.getRelative(BlockFace.DOWN).getType() != Material.AIR;
	}
	
	Vector getVectorDir(Location caster, Location target) {
		return target.clone().subtract(caster.toVector()).toVector();
	}
	
	Vector getVectorFromFace(Block block, BlockFace face) {
		Location blockLoc = block.getLocation();
		Location faceLoc = block.getRelative(face).getLocation();
		return getVectorDir(blockLoc, faceLoc);
	}
}
