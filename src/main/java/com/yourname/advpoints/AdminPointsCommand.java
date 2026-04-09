package com.yourname.advpoints;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AdminPointsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, String label, String[] args) {

        // ✅ Only OP
        if (!sender.isOp()) {
            sender.sendMessage("§cYou do not have permission!");
            return true;
        }

        // Usage check
        if (args.length != 3) {
            sender.sendMessage("§eUsage: /pointsadmin <add/remove> <player> <amount>");
            return true;
        }

        String action = args[0];
        String playerName = args[1];
        int amount;

        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid number!");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);
        UUID uuid = target.getUniqueId();

        if (action.equalsIgnoreCase("add")) {
            PlayerData.addPoints(uuid, amount);
            sender.sendMessage("§aAdded " + amount + " points to " + playerName);

        } else if (action.equalsIgnoreCase("remove")) {
            PlayerData.addPoints(uuid, -amount);
            sender.sendMessage("§cRemoved " + amount + " points from " + playerName);

        } else {
            sender.sendMessage("§cUse add or remove!");
        }

        return true;
    }
}