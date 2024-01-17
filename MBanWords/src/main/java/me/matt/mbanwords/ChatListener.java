package me.matt.mbanwords;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final MBanWords plugin;

    public ChatListener(MBanWords plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();
        if (plugin.isBanned(message, player)) {
            event.setMessage(plugin.censorMessage(message));
        }
    }
}