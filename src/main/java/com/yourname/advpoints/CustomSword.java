package com.yourname.advpoints;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class CustomSword {

    public static final int MODEL_DATA = 123456;

    public static ItemStack getSword() {
        ItemStack item = new ItemStack(Material.GOLDEN_SWORD);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§6Oathbreaker");
        meta.setCustomModelData(MODEL_DATA);
        meta.setUnbreakable(true);

        meta.setLore(Arrays.asList(
                "§7Right-click to dash",
                "§7Hit enemies to summon lightning"
        ));

        meta.addEnchant(Enchantment.SHARPNESS, 6, true);
        meta.addEnchant(Enchantment.UNBREAKING, 9, true);
        meta.addEnchant(Enchantment.FIRE_ASPECT, 3, true);

        meta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES
        );
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isCustomSword(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        return meta.hasCustomModelData() && meta.getCustomModelData() == MODEL_DATA;
    }

    public static void registerRecipe(JavaPlugin plugin) {
        NamespacedKey key = new NamespacedKey(plugin, "blade_of_legends");

        ShapedRecipe recipe = new ShapedRecipe(key, getSword());

        recipe.shape(" N ", " N ", " S ");
        recipe.setIngredient('N', Material.NETHER_STAR);
        recipe.setIngredient('S', Material.BLAZE_ROD);

        plugin.getServer().addRecipe(recipe);
    }
}