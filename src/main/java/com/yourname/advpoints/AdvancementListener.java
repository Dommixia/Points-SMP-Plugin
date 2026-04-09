package com.yourname.advpoints;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.entity.Player;
import org.bukkit.NamespacedKey;
import org.bukkit.Bukkit;

public class AdvancementListener implements Listener {

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();

        String advancement = event.getAdvancement().getKey().toString();

        int basePoints = getPointsForAdvancement(advancement);

        if (basePoints <= 0) return;

        // ✅ Everyone gets base points
        PlayerData.addPoints(player, basePoints);
        player.sendMessage("§a+" + basePoints + " points for advancement!");

        if (PlayerData.plugin.getConfig().getBoolean("claimed-advancements." + advancement, false)) {
            return;
        }

        int bonus = getBonusForAdvancement(advancement);

        if (bonus > 0) {
            PlayerData.addPoints(player, bonus);

            Bukkit.broadcastMessage("§6" + player.getName()
                    + " was the FIRST to complete §e" + advancement
                    + " §6and earned §a+" + bonus + " BONUS points!");
        }

        PlayerData.plugin.getConfig().set("claimed-advancements." + advancement, true);
        PlayerData.plugin.saveConfig();

        //Allocating rewards
        if (advancement.equals("minecraft:adventure/adventuring_time")) {
            PlayerData.giveRewards(player);
        }
        if (advancement.equals("minecraft:husbandry/balanced_diet")){
            PlayerData.balancedDietReward(player);
        }
        if (advancement.equals("minecraft:nether/create_full_beacon")){
            PlayerData.BeaconRewards(player);
        }
        if (advancement.equals("minecraft:nether/all_effects")){
            PlayerData.giveHowDIDWEGETHEReRewards(player);
        }
    }

    private int getPointsForAdvancement(String adv) {

        if (adv.equals("minecraft:end/dragon_egg")){
            return 10;
        }

        if (adv.equals("minecraft:end/kill_dragon")){
            return 10;
        }

        if (adv.equals("minecraft:nether/summon_wither")){
            return 20;
        }

        if (adv.equals("minecraft:end/elytra")){

            return 20;
        }

        if (adv.equals("minecraft:nether/find_fortress")){
            return 10;
        }

        if (adv.equals("minecraft:adventure/adventuring_time")) {
            return 20;
        }

        if (adv.startsWith("minecraft:husbandry/")){
            return 2;
        }

        if (adv.startsWith("minecraft:story/")){
            return 1;
        }

        if (adv.startsWith("minecraft:adventure/")){
            return 2;
        }

        if(adv.startsWith("minecraft:nether/")) return 2;

        if (adv.equals("minecraft:husbandry/balanced_diet")) return 10;

        if (adv.equals("minecraft:adventure/totem_of_undying")) return 5;

        if (adv.equals("minecraft:nether/all_effects")) return 50;

        return 0;
    }

    private int getBonusForAdvancement(String adv) {

        if (adv.equals("minecraft:end/kill_dragon")) return 20;
        if (adv.equals("minecraft:end/dragon_egg")) return 15;
        if (adv.equals("minecraft:end/elytra")) return 15;
        if (adv.equals("minecraft:nether/summon_wither")) return 15;
        if (adv.equals("minecraft:adventure/adventuring_time")) return 10;
        if (adv.equals("minecraft:nether/find_fortress")) return 5;

        return 0;
    }
}