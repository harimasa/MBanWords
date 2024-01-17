package me.matt.mbanwords;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public class MBanWords extends JavaPlugin implements CommandExecutor {

    private List<String> bannedWords;

    @Override
    public void onEnable() {
        createConfig();
        loadConfig();
        getCommand("mbw").setExecutor(this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mbw")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("mbw.reload")) {
                    reloadConfig();
                    loadConfig();
                    sender.sendMessage(ChatColor.GREEN + "Конфиг перезагружен!");
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "У вас нет разрешения на выполнение этой команды.");
                    return true;
                }
            }
        }
        return false;
    }

    private void loadConfig() {
        FileConfiguration config = getConfig();
        config.options().copyDefaults(true);
        bannedWords = config.getStringList("words");
    }

    private void createConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
    }

    public boolean isBanned(String message, CommandSender sender) {
        if (sender.hasPermission("mbw.bypass")) {
            return false;
        }
        for (String word : bannedWords) {
            if (message.toLowerCase().contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public String censorMessage(String message) {
        for (String word : bannedWords) {
            message = message.replaceAll("(?i)" + word, "#".repeat(word.length()));
        }
        return message;
    }
}