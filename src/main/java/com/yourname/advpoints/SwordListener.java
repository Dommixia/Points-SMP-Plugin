package com.yourname.advpoints;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.event.inventory.CraftItemEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SwordListener implements Listener {

    private final HashMap<String, Long> cooldowns = new HashMap<>();
    private final Set<UUID> completed = new HashSet<>();

    private final long DASH_COOLDOWN = 1000;
    private final long LIGHTNING_COOLDOWN = 3000;

    private boolean isOnCooldown(Player player, String ability, long cooldown) {
        String key = player.getUniqueId() + ":" + ability;
        long now = System.currentTimeMillis();

        if (!cooldowns.containsKey(key)) return false;

        long last = cooldowns.get(key);
        return (now - last) < cooldown;
    }

    private long getRemaining(Player player, String ability, long cooldown) {
        String key = player.getUniqueId() + ":" + ability;
        long now = System.currentTimeMillis();

        long last = cooldowns.getOrDefault(key, 0L);
        return (cooldown - (now - last)) / 1000;
    }

    private void setCooldown(Player player, String ability) {
        String key = player.getUniqueId() + ":" + ability;
        cooldowns.put(key, System.currentTimeMillis());
    }

    // 🚀 DASH ABILITY
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!CustomSword.isCustomSword(item)) return;

        switch (event.getAction()) {
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
                break;
            default:
                return;
        }

        if (isOnCooldown(player, "dash", DASH_COOLDOWN)) {
            player.sendActionBar("§cDash cooldown: " + getRemaining(player, "dash", DASH_COOLDOWN) + "s");
            return;
        }

        setCooldown(player, "dash");

        Vector direction = player.getLocation().getDirection().normalize().multiply(1.5);
        player.setVelocity(direction);

        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 20);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
    }

    // ⚡ LIGHTNING ON HIT
    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getDamager();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!CustomSword.isCustomSword(item)) return;

        // Extra damage
        event.setDamage(event.getDamage() + 5);

        if (isOnCooldown(player, "lightning", LIGHTNING_COOLDOWN)) return;

        setCooldown(player, "lightning");

        Location loc = event.getEntity().getLocation();
        loc.getWorld().strikeLightning(loc);

        player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
    }
}