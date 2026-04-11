package com.yourname.advpoints;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class MobKillListener implements Listener {

    private static final Map<EntityType, Integer> MOB_POINTS = Map.of(
            EntityType.WARDEN, 20,
            EntityType.WITHER, 20,
            EntityType.ENDER_DRAGON, 10
    );

    @EventHandler
    public void onMobKill(EntityDeathEvent event) {

        Player player = event.getEntity().getKiller();
        if (player == null) return;

        EntityType type = event.getEntityType();

        Integer points = MOB_POINTS.get(type);
        if (points == null) return;

        if (type == EntityType.WARDEN){
            ItemStack reward = new ItemStack(Material.TRIDENT);
        }

        PlayerData.addPoints(player, points);

        player.sendMessage("§5[DEBUG] Boss kill detected!");
        player.sendMessage("§dKilled " + type.name() + "! +" + points + " points!");
    }
}