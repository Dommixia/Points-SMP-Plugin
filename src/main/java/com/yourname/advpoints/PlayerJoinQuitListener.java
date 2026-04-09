package com.yourname.advpoints;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;

public class PlayerJoinQuitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PlayerData.loadPlayer(player);

        // ✅ FIXED CALL
        PlayerData.checkMilestones(player, PlayerData.getPoints(player));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        PlayerData.savePlayer(event.getPlayer());
    }
}