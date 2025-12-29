package ru.javaroot;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class DragonListener implements Listener {
    
    private final glavnoe plugin;
    private final ConfigManager configManager;
    
    public DragonListener(glavnoe plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }
    
    @EventHandler
    public void onDragonDeath(EntityDeathEvent event) {
        if (event.getEntity().getType() != EntityType.ENDER_DRAGON) {
            return;
        }
        
        EnderDragon dragon = (EnderDragon) event.getEntity();
        World world = dragon.getWorld();
        
        if (!world.getEnvironment().equals(World.Environment.THE_END)) {
            return;
        }
        
        int delay = configManager.getRespawnDelayTicks();
        
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!configManager.isFirstKill()) {
                    configManager.setFirstKill(true);
                    return;
                }
                
                spawnEgg(world);
            }
        }.runTaskLater(plugin, delay);
    }
    
    private void spawnEgg(World world) {
        Location eggLocation;
        
        if (configManager.isCustomCoordsEnabled()) {
            int x = configManager.getCustomCoordX();
            int y = configManager.getCustomCoordY();
            int z = configManager.getCustomCoordZ();
            eggLocation = new Location(world, x, y, z);
        } else {
            if (configManager.isUsePortalTop()) {
                Block highestBlock = world.getHighestBlockAt(0, 0);
                
                if (highestBlock.getType() == Material.BEDROCK) {
                    eggLocation = highestBlock.getLocation().add(0, 1, 0);
                } else {
                    eggLocation = new Location(world, 0, highestBlock.getY() + 1, 0);
                }
            } else {
                Block blockAt00 = world.getBlockAt(0, 0, 0);
                eggLocation = new Location(world, 0, blockAt00.getY() + 1, 0);
            }
        }
        
        if (configManager.isCheckForFreeSpace()) {
            if (eggLocation.getBlock().getType() != Material.AIR) {
                int maxHeight = Math.min(configManager.getMaxSearchHeight(), world.getMaxHeight());
                int minHeight = Math.max(configManager.getMinSearchHeight(), world.getMinHeight());
                
                while (eggLocation.getBlock().getType() != Material.AIR && eggLocation.getY() < maxHeight) {
                    eggLocation = eggLocation.add(0, 1, 0);
                }
                
                if (eggLocation.getBlock().getType() != Material.AIR) {
                    eggLocation = eggLocation.subtract(0, 1, 0);
                    while (eggLocation.getBlock().getType() != Material.AIR && eggLocation.getY() > minHeight) {
                        eggLocation = eggLocation.subtract(0, 1, 0);
                    }
                }
            }
        }
        
        String spawnMethod = configManager.getSpawnMethod().toUpperCase();
        
        if (spawnMethod.equals("BLOCK")) {
            if (eggLocation.getBlock().getType() == Material.AIR) {
                eggLocation.getBlock().setType(Material.DRAGON_EGG);
                plugin.getLogger().info("Яйцо Дракона появилось как блок на координатах: " +
                    eggLocation.getBlockX() + ", " + eggLocation.getBlockY() + ", " + eggLocation.getBlockZ());
            }
        } else if (spawnMethod.equals("ITEM")) {
            ItemStack dragonEgg = new ItemStack(Material.DRAGON_EGG);
            Item item = world.dropItem(eggLocation, dragonEgg);
            item.setVelocity(new org.bukkit.util.Vector(0, 0, 0));
            plugin.getLogger().info("Яйцо Дракона появилось как предмет на координатах: " +
                eggLocation.getBlockX() + ", " + eggLocation.getBlockY() + ", " + eggLocation.getBlockZ());
        } else {
            if (eggLocation.getBlock().getType() == Material.AIR) {
                eggLocation.getBlock().setType(Material.DRAGON_EGG);
                plugin.getLogger().info("Яйцо Дракона появилось как блок (метод по умолчанию) на координатах: " +
                    eggLocation.getBlockX() + ", " + eggLocation.getBlockY() + ", " + eggLocation.getBlockZ());
            }
        }
        
        if (configManager.isBroadcastEnabled()) {
            String message = configManager.getAnnouncementMessage();
            Bukkit.broadcastMessage(message);
        }
    }
}