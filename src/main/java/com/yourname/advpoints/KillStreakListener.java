package com.yourname.advpoints;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.UUID;

public class KillStreakListener implements Listener {

    private final HashMap<UUID, Integer> killStreaks = new HashMap<>();
    private final HashMap<String, Long> killCooldown = new HashMap<>();

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        UUID victimId = victim.getUniqueId();

        killStreaks.put(victimId, 0);
        victim.sendMessage("💀 Your kill streak has been reset!");

        if (killer == null) return;

        UUID killerId = killer.getUniqueId();

        // 🔥 Anti-farming cooldown
        String key = killerId.toString() + "-" + victimId.toString();
        long now = System.currentTimeMillis();

        if (killCooldown.containsKey(key)) {
            long lastTime = killCooldown.get(key);

            if (now - lastTime < 300000) {
                killer.sendMessage("§cYou must wait before killing this player again!");
                return;
            }
        }

        killCooldown.put(key, now);

        int streak = killStreaks.getOrDefault(killerId, 0) + 1;
        killStreaks.put(killerId, streak);

        PlayerData.addPoints(killer, 1);
        killer.sendMessage("§a +2 points for killing §e" + victim.getName());

        if (streak % 3 == 0) {
            PlayerData.addPoints(killer, 5);
            killer.sendMessage("🔥 Kill streak " + streak + "! +5 bonus points");
        }
    }}