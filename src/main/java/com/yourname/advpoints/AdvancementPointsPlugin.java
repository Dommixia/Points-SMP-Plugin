package com.yourname.advpoints;

import org.bukkit.plugin.java.JavaPlugin;

public class AdvancementPointsPlugin extends JavaPlugin {

    private static AdvancementPointsPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new AdvancementListener(), this);
        getCommand("points").setExecutor(new PointsCommand());
        getLogger().info("Advancement Points Plugin Enabled!");
        getServer().getPluginManager().registerEvents(new MobKillListener(), this);
        getServer().getPluginManager().registerEvents(new KillStreakListener(), this);
        getServer().getPluginManager().registerEvents(new CustomAdvancementListener(this), this);

        PlayerData.init(this);

        getServer().getPluginManager().registerEvents(new KillStreakListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(), this);

        getCommand("leaderboard").setExecutor(new LeaderboardCommand());
        getCommand("pointsadmin").setExecutor(new AdminPointsCommand());
        PlayerData.init(this);
        PlayerData.init(this);

        CustomSword.registerRecipe(this);
        getServer().getPluginManager().registerEvents(new SwordListener(), this);

        getServer().getPluginManager().registerEvents(new CraftAdvancementListener(this ), this);

        getServer().getPluginManager().registerEvents(new CustomTridentListener(this), this);

        CustomTridentListener.registerRecipe(this);

        saveDefaultConfig();
    }

    public static AdvancementPointsPlugin getInstance() {
        return instance;
    }
}
