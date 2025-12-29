package ru.javaroot;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {
    
    private final JavaPlugin plugin;
    private FileConfiguration config;
    private File configFile;
    
    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
    }
    
    public void loadConfig() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        
        config = YamlConfiguration.loadConfiguration(configFile);
        
        setDefaultConfig();
        
        saveConfig();
    }
    
    private void setDefaultConfig() {
        if (!config.contains("spawn-method")) {
            config.set("spawn-method", "BLOCK");
        }
        
        if (!config.contains("custom-coords")) {
            config.createSection("custom-coords");
        }
        
        if (!config.contains("custom-coords.enabled")) {
            config.set("custom-coords.enabled", false);
        }
        
        if (!config.contains("custom-coords.x")) {
            config.set("custom-coords.x", 0);
        }
        
        if (!config.contains("custom-coords.y")) {
            config.set("custom-coords.y", 0);
        }
        
        if (!config.contains("custom-coords.z")) {
            config.set("custom-coords.z", 0);
        }
        
        if (!config.contains("announcement-message")) {
            config.set("announcement-message", "§aЯйцо Дракона появилось после убийства Эндер-дракона!");
        }
        
        if (!config.contains("enable-broadcast")) {
            config.set("enable-broadcast", true);
        }
        
        if (!config.contains("respawn-delay-ticks")) {
            config.set("respawn-delay-ticks", 20);
        }
        
        if (!config.contains("spawn-settings")) {
            config.createSection("spawn-settings");
        }
        
        if (!config.contains("spawn-settings.check-for-free-space")) {
            config.set("spawn-settings.check-for-free-space", true);
        }
        
        if (!config.contains("spawn-settings.max-search-height")) {
            config.set("spawn-settings.max-search-height", 256);
        }
        
        if (!config.contains("spawn-settings.min-search-height")) {
            config.set("spawn-settings.min-search-height", 0);
        }
        
        if (!config.contains("spawn-settings.use-portal-top")) {
            config.set("spawn-settings.use-portal-top", true);
        }
        
        if (!config.contains("firstKill")) {
            config.set("firstKill", false);
        }
        
        addComments();
    }
    
    private void addComments() {
        String configContent =
            "spawn-method: " + config.getString("spawn-method") + "\n" +
            "\n" +
            "custom-coords:\n" +
            "  enabled: " + config.getBoolean("custom-coords.enabled") + "\n" +
            "  x: " + config.getInt("custom-coords.x") + "\n" +
            "  y: " + config.getInt("custom-coords.y") + "\n" +
            "  z: " + config.getInt("custom-coords.z") + "\n" +
            "\n" +
            "announcement-message: \"" + config.getString("announcement-message") + "\"\n" +
            "\n" +
            "enable-broadcast: " + config.getBoolean("enable-broadcast") + "\n" +
            "\n" +
            "respawn-delay-ticks: " + config.getInt("respawn-delay-ticks") + "\n" +
            "\n" +
            "spawn-settings:\n" +
            "  check-for-free-space: " + config.getBoolean("spawn-settings.check-for-free-space") + "\n" +
            "  max-search-height: " + config.getInt("spawn-settings.max-search-height") + "\n" +
            "  min-search-height: " + config.getInt("spawn-settings.min-search-height") + "\n" +
            "  use-portal-top: " + config.getBoolean("spawn-settings.use-portal-top") + "\n" +
            "\n" +
            "firstKill: " + config.getBoolean("firstKill") + "\n";
        
        try {
            java.nio.file.Files.write(configFile.toPath(), configContent.getBytes());
        } catch (IOException e) {
            plugin.getLogger().severe("Could not write config file: " + e.getMessage());
        }
    }
    
    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Не удалось сохранить конфигурационный файл: " + e.getMessage());
        }
    }
    
    public FileConfiguration getConfig() {
        return config;
    }
    
    public String getSpawnMethod() {
        return config.getString("spawn-method", "BLOCK");
    }
    
    public boolean isCustomCoordsEnabled() {
        return config.getBoolean("custom-coords.enabled", false);
    }
    
    public int getCustomCoordX() {
        return config.getInt("custom-coords.x", 0);
    }
    
    public int getCustomCoordY() {
        return config.getInt("custom-coords.y", 0);
    }
    
    public int getCustomCoordZ() {
        return config.getInt("custom-coords.z", 0);
    }
    
    public String getAnnouncementMessage() {
        return config.getString("announcement-message", "§aЯйцо Дракона появилось после убийства Эндер-дракона!");
    }
    
    public boolean isBroadcastEnabled() {
        return config.getBoolean("enable-broadcast", true);
    }
    
    public int getRespawnDelayTicks() {
        return config.getInt("respawn-delay-ticks", 20);
    }
    
    public boolean isCheckForFreeSpace() {
        return config.getBoolean("spawn-settings.check-for-free-space", true);
    }
    
    public int getMaxSearchHeight() {
        return config.getInt("spawn-settings.max-search-height", 256);
    }
    
    public int getMinSearchHeight() {
        return config.getInt("spawn-settings.min-search-height", 0);
    }
    
    public boolean isUsePortalTop() {
        return config.getBoolean("spawn-settings.use-portal-top", true);
    }
    
    public boolean isFirstKill() {
        return config.getBoolean("firstKill", false);
    }
    
    public void setFirstKill(boolean firstKill) {
        config.set("firstKill", firstKill);
        saveConfig();
    }
}