package com.yourname.advpoints;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LeaderboardCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        sender.sendMessage("§6=== Leaderboard ===");

        List<Map.Entry<UUID, Integer>> top = PlayerData.getTopPlayers(10);

        int rank = 1;

        for (Map.Entry<UUID, Integer> entry : top) {
            UUID uuid = entry.getKey();
            int points = entry.getValue();

            String name = Bukkit.getOfflinePlayer(uuid).getName();

            sender.sendMessage("§e" + rank + ". §f" + name + " - §a" + points);

            rank++;
        }

        return true;
    }
}