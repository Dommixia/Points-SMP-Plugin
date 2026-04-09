package com.yourname.advpoints;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CraftAdvancementListener implements Listener {

    private final Set<UUID> completed = new HashSet<>();
    private final JavaPlugin plugin;

    public CraftAdvancementListener(JavaPlugin plugin) {
        this.plugin = plugin;
        loadData();
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack result = event.getRecipe().getResult();

        if (result == null) return;

        if (!CustomSword.isCustomSword(result)) return;

        UUID id = player.getUniqueId();


        if (!player.hasPermission("advpoints.bypass.craft")) {
            int points = PlayerData.getPoints(player);
            if(points<100){
                event.setCancelled(true);
                player.sendMessage("You need to atleast have 50pts to craft the sword");
            }

            if (completed.contains(id)) {
                event.setCancelled(true);
                player.sendMessage("§cYou have already crafted this sword!");
                return;
            }

        }

        completed.add(id);

        PlayerData.addPoints(player, 20);

        Bukkit.broadcastMessage("§6" + player.getName() + " has crafted §eOathBreaker!! §a(+20)");
    }

    private void savePlayer(UUID uuid) {
        FileConfiguration config = plugin.getConfig();
        config.set("crafted." + uuid.toString(), true);
        plugin.saveConfig();
    }

    private void loadData() {
        FileConfiguration config = plugin.getConfig();

        if (!config.contains("crafted")) return;

        for (String key : config.getConfigurationSection("crafted").getKeys(false)) {
            completed.add(UUID.fromString(key));
        }
    }
}