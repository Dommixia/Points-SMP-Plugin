package com.yourname.advpoints;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

public class KillStreakListener implements Listener {

    private final HashMap<UUID, Integer> killStreaks = new HashMap<>();
    private final HashMap<String, Long> killCooldown = new HashMap<>();

    private final HashMap<UUID, Integer> bounties = new HashMap<>();
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        UUID victimId = victim.getUniqueId();

        killStreaks.put(victimId, 0);
        victim.sendMessage("💀 Your kill streak has been reset!");

        if (bounties.containsKey(victimId) && killer != null) {
            int bounty = bounties.get(victimId);

            PlayerData.addPoints(killer, bounty);

            Bukkit.broadcastMessage("§6💰 " + killer.getName() +
                    " claimed a bounty of §e" + bounty +
                    "§6 by killing §c" + victim.getName());

            bounties.remove(victimId);
            victim.removePotionEffect(PotionEffectType.GLOWING);
            victim.removePotionEffect(PotionEffectType.STRENGTH);
        }

        if (killer == null) return;

        UUID killerId = killer.getUniqueId();

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
        Bukkit.broadcastMessage("§a +2 points for killing §e" + victim.getName());

        if (streak % 3 == 0) {
            PlayerData.addPoints(killer, 5);
            killer.sendMessage("🔥 Kill streak " + streak + "! +5 bonus points");
        }

        if (streak == 5) {
            int bounty = streak * 2;
            bounties.put(killerId, bounty);

            killer.addPotionEffect(new PotionEffect(
                    PotionEffectType.GLOWING,
                    Integer.MAX_VALUE,
                    0,
                    false,
                    false
            ));

            Bukkit.broadcastMessage("§c⚔ " + killer.getName() +
                    " is on a streak of §e" + streak +
                    "§c! Bounty: §6" + bounty + " points");
        }

        if (streak == 10) {
            int bounty = streak * 3;
            bounties.put(killerId, bounty);

            killer.addPotionEffect(new PotionEffect(
                    PotionEffectType.STRENGTH,
                    Integer.MAX_VALUE,
                    2,
                    false,
                    false
            ));

            killer.sendMessage("Recieved Strenght 2 for killing 10 players!!!!");

            Bukkit.broadcastMessage("§4☠ " + killer.getName() +
                    " is UNSTOPPABLE! §cBounty increased to §6" + bounty);
        }
    }
}