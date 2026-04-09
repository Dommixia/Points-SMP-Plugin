package com.yourname.advpoints;

import org.bukkit.persistence.PersistentDataType;
import com.yourname.advpoints.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.NamespacedKey;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CustomTridentListener implements Listener {

    private final Set<UUID> completed = new HashSet<>();
    private final JavaPlugin plugin;

    public CustomTridentListener(JavaPlugin plugin) {
        this.plugin = plugin;
        loadData();
    }

    public static ItemStack createTrident(JavaPlugin plugin) {
        ItemStack item = new ItemStack(Material.TRIDENT);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§bArctic Spear");

        meta.setLore(Arrays.asList(
                "§7Freezes enemies on hit"
        ));

        meta.addEnchant(Enchantment.LOYALTY, 3, true);
        meta.addEnchant(Enchantment.UNBREAKING, 3, true);

        // 🔥 PDC TAG
        NamespacedKey key = new NamespacedKey(plugin, "custom_trident");
        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);

        item.setItemMeta(meta);
        return item;
    }

    public boolean isCustomTrident(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();

        NamespacedKey key = new NamespacedKey(plugin, "custom_trident");

        return meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER);
    }

    public static void registerRecipe(JavaPlugin plugin) {

        NamespacedKey key = new NamespacedKey(plugin, "artic_spear");

        ShapedRecipe recipe = new ShapedRecipe(key, createTrident(plugin));

        recipe.shape(
                " D ",
                "ITI",
                " S "
        );

        recipe.setIngredient('D', Material.HEAVY_CORE);
        recipe.setIngredient('I', Material.HEART_OF_THE_SEA);
        recipe.setIngredient('T', Material.TRIDENT);
        recipe.setIngredient('S', Material.BLAZE_ROD);

        plugin.getServer().addRecipe(recipe);
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack result = event.getRecipe().getResult();

        if (result == null) return;

        if (!isCustomTrident(result)) return;

        UUID id = player.getUniqueId();

        if (!player.isOp() && !player.hasPermission("advpoints.bypass.trident")) {

            int points = PlayerData.getPoints(player);

            if (points < 100) {
                event.setCancelled(true);
                player.sendMessage("§cYou need §e100 points §cto craft this trident!");
                return;
            }

            if (completed.contains(id)) {
                event.setCancelled(true);
                player.sendMessage("§cYou have already crafted this trident!");
                return;
            }
        }

        if (!completed.contains(id)) {
            completed.add(id);
            savePlayer(id);

            PlayerData.addPoints(player, 30);

            Bukkit.broadcastMessage("§b" + player.getName() + " has become the §3King of the Seas §a(+30)");
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof LivingEntity)) return;

        LivingEntity victim = (LivingEntity) event.getEntity();

        // 🔱 Melee
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();

            ItemStack item = player.getInventory().getItemInMainHand();

            if (isCustomTrident(item)) {
                applyEffects(victim);
            }
        }

        // 🔱 Thrown
        if (event.getDamager() instanceof Trident) {
            Trident trident = (Trident) event.getDamager();

            if (trident.getShooter() instanceof Player) {
                ItemStack item = trident.getItem();

                if (isCustomTrident(item)) {
                    applyEffects(victim);
                }
            }
        }
    }

    private void applyEffects(LivingEntity victim) {
        victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 1));
        victim.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1));
    }


    private void savePlayer(UUID uuid) {
        FileConfiguration config = plugin.getConfig();
        config.set("trident." + uuid.toString(), true);
        plugin.saveConfig();
    }
    private void loadData() {
        FileConfiguration config = plugin.getConfig();

        if (!config.contains("trident")) return;

        for (String key : config.getConfigurationSection("trident").getKeys(false)) {
            completed.add(UUID.fromString(key));
        }
    }
}