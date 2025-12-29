package ru.javaroot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class glavnoe extends JavaPlugin {
    
    private DragonListener dragonListener;
    private ConfigManager configManager;
    
    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.loadConfig();
        
        dragonListener = new DragonListener(this, configManager);
        getServer().getPluginManager().registerEvents(dragonListener, this);
        
        getLogger().info("EnderEggRespawn plugin enabled!");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("EnderEggRespawn plugin disabled!");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("eer") || command.getName().equalsIgnoreCase("endereggrespawn")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("endereggrespawn.reload") || sender instanceof ConsoleCommandSender) {
                    configManager.loadConfig();
                    sender.sendMessage("§aКонфигурация плагина EnderEggRespawn перезагружена!");
                    return true;
                } else {
                    sender.sendMessage("§cУ вас нет прав на выполнение этой команды!");
                    return true;
                }
            }
        }
        return false;
    }
}