package com.yourname.advpoints;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class CustomAdvancementListener implements Listener {

    private final JavaPlugin plugin;

    public CustomAdvancementListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private final Map<UUID, Set<Material>> woodTracker = new HashMap<>();

    private final Set<Material> REQUIRED_WOODS = new HashSet<>(Arrays.asList(
            Material.OAK_LOG,
            Material.SPRUCE_LOG,
            Material.BIRCH_LOG,
            Material.JUNGLE_LOG,
            Material.ACACIA_LOG,
            Material.DARK_OAK_LOG,
            Material.MANGROVE_LOG,
            Material.CHERRY_LOG,
            Material.CRIMSON_STEM,
            Material.WARPED_STEM,
            Material.BAMBOO_BLOCK
    ));

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {

        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        UUID id = player.getUniqueId();

        Material type = event.getItem().getItemStack().getType();

        // 🔱 TRIDENT
        String tridentPath = "advancements.trident." + id.toString();

        if (type == Material.TRIDENT && !plugin.getConfig().getBoolean(tridentPath)) {

            plugin.getConfig().set(tridentPath, true);
            plugin.saveConfig();

            PlayerData.addPoints(player, 15);
            Bukkit.broadcastMessage("§6Custom Advancement! §e" + player.getName() + " got Poseidon's Gift §a(+15)");
        }

        // 🔨 MACE
        String macePath = "advancements.mace." + id.toString();

        if (type == Material.MACE && !plugin.getConfig().getBoolean(macePath)) {

            plugin.getConfig().set(macePath, true);
            plugin.saveConfig();

            PlayerData.addPoints(player, 15);
            Bukkit.broadcastMessage("§6Custom Advancement! §e" + player.getName() + " got Heavy Hitter §a(+15)");
        }

        // 🌳 WOOD TRACKING
        if (REQUIRED_WOODS.contains(type)) {

            woodTracker.putIfAbsent(id, new HashSet<>());
            woodTracker.get(id).add(type);

            String woodPath = "advancements.wood." + id.toString();

            if (!plugin.getConfig().getBoolean(woodPath)
                    && woodTracker.get(id).containsAll(REQUIRED_WOODS)) {

                plugin.getConfig().set(woodPath, true);
                plugin.saveConfig();

                PlayerData.addPoints(player, 35);
                Bukkit.broadcastMessage("§5Challenge Complete! §d" + player.getName() + " mastered all woods §a(+35)");
            }
        }
    }
}