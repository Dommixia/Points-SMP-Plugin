package com.yourname.advpoints;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PlayerData {

    private static final HashMap<UUID, Integer> points = new HashMap<>();
    public static JavaPlugin plugin;

    public static void init(JavaPlugin pl) {
        plugin = pl;
    }

    public static void addPoints(Player player, int amount) {
        UUID id = player.getUniqueId();

        int oldPoints = points.getOrDefault(id, 0);
        int newPoints = oldPoints + amount;

        points.put(id, newPoints);

        savePlayer(player);

        checkMilestones(player, newPoints);
    }

    public static void giveRewards(Player player) {
        ItemStack reward = new ItemStack(Material.DIAMOND, 5);
        player.getInventory().addItem(reward);
        player.sendMessage("§bYou received 5 Diamonds!");
    }

    public static void giveHowDIDWEGETHEReRewards(Player player) {
        player.getInventory().addItem(new ItemStack(Material.TOTEM_OF_UNDYING,1),
                new ItemStack(Material.ELYTRA, 1),
                new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 4));

        player.sendMessage("§6You received Totems, Elytras, GOD apples!!!");
    }

    public static void BeaconRewards(Player player){
        ItemStack reward2 = new ItemStack(Material.NETHERITE_INGOT, 3);
        player.getInventory().addItem(reward2);
        player.sendMessage("Recieved 3 Netherite Ingots");
    }

    public static void balancedDietReward(Player player){
        ItemStack reward3 = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1);
        player.getInventory().addItem(reward3);
        player.sendMessage("Recieved A GOD Apple!!!");
    }

    public static void checkMilestones(Player player, int pts) {
        UUID id = player.getUniqueId();

        System.out.println("Points for " + player.getName() + ": " + pts);

        if (plugin.getConfig().getBoolean("rewards." + id + ".100", false)) {
            return;
        }
        if (pts >= 100) {
            giveGoldenApples(player);

            plugin.getConfig().set("rewards." + id + ".100", true);
            plugin.saveConfig();
        }
        if(pts>=150){
            giveTotems(player);
            plugin.getConfig().set("rewards." + id + ".100", true);
            plugin.saveConfig();
        }
    }
    private static void giveElytra(Player player){
        ItemStack elytra = new ItemStack(Material.ELYTRA);
        HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(elytra);

        if (!leftover.isEmpty()) {
            player.getWorld().dropItemNaturally(player.getLocation(), elytra);
        }
        player.sendMessage("§6🎉 You reached 500 points! You received 2 elytras!");
    }

    private static void giveTotems(Player player) {
        ItemStack totems = new ItemStack(Material.TOTEM_OF_UNDYING, 2);
        HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(totems);

        if (!leftover.isEmpty()) {
            player.getWorld().dropItemNaturally(player.getLocation(), totems);
        }
        player.sendMessage("§6🎉 You reached 150 points! You received 2 Totems!");
    }

    private static void giveGoldenApples(Player player) {
        ItemStack apples = new ItemStack(Material.GOLDEN_APPLE, 32);

        HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(apples);

        if (!leftover.isEmpty()) {
            player.getWorld().dropItemNaturally(player.getLocation(), apples);
        }

        player.sendMessage("§6🎉 You reached 100 points! You received 32 Golden Apples!");
    }

    public static int getPoints(Player player) {
        return points.getOrDefault(player.getUniqueId(), 0);
    }

    public static void loadPlayer(Player player) {
        FileConfiguration config = plugin.getConfig();
        UUID id = player.getUniqueId();

        int savedPoints = config.getInt("points." + id.toString(), 0);
        points.put(id, savedPoints);
    }

    public static void savePlayer(Player player) {
        FileConfiguration config = plugin.getConfig();
        UUID id = player.getUniqueId();

        config.set("points." + id.toString(), points.get(id));
        plugin.saveConfig();
    }

    public static List<Map.Entry<UUID, Integer>> getTopPlayers(int limit) {

        Map<UUID, Integer> allPoints = new HashMap<>();

        if (plugin.getConfig().getConfigurationSection("points") == null) {
            return new ArrayList<>();
        }

        for (String key : plugin.getConfig().getConfigurationSection("points").getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            int value = plugin.getConfig().getInt("points." + key);
            allPoints.put(uuid, value);
        }

        return allPoints.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .toList();
    }

    public static void addPoints(UUID uuid, int amount) {
        int newPoints = points.getOrDefault(uuid, 0) + amount;
        newPoints = Math.max(0, newPoints);
        points.put(uuid, newPoints);

        plugin.getConfig().set("points." + uuid.toString(), newPoints);
        plugin.saveConfig();
    }

    public static int getPoints(UUID uuid) {
        if (points.containsKey(uuid)) {
            return points.get(uuid);
        }

        return plugin.getConfig().getInt("points." + uuid.toString(), 0);
    }
}