package com.rootnroute.rpg.showmethemoney;

import com.rootnroute.rpg.showmethemoney.economy.EconomyManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class MoneyItem {
    // Get the configuration section where the item's attributes are defined.
    public static ConfigurationSection getConfig() { return Main.getInstanceConfig().getConfigurationSection("item"); }

    // Get the material of the coins.
    public static Material getMaterial() { return Utils.validateMaterialValueOf(getConfig().getString("material"), Material.SUNFLOWER); }

    // Get the currency's name from the config.
    public static String getName(boolean plural) {
        // If the internal economy system is being used, return the configured name in plural or singular forms.
        if (Main.usingInternalEconomy()) return getConfig().getString("name." + (plural ? "plural" : "singular"));

        // Otherwise, return the name configured by the Vault economy provider.
        return plural ? Main.getVaultEconomy().currencyNamePlural() : Main.getVaultEconomy().currencyNameSingular();
    }

    // Add colors to the attained name of the currency.
    public static Component getNameComponent(boolean plural) {
        return Component.text(getName(plural)).color(getColor());
    }

    // Return the color configured in the config.
    public static TextColor getColor() {
        // Establish default color as the gold color in Minecraft.
        TextColor color = TextColor.color(Color.ORANGE.asRGB());

        if (getConfig().getString("color") != null) {
            TextColor resolvedColor = TextColor.fromCSSHexString(getConfig().getString("color"));
            if (resolvedColor != null) {
                Main.log("Configured color couldn't be read as a CSS string, falling back to gold.");
                color = resolvedColor;
            }
        }

        // Return the established color.
        return color;
    }

    public static ItemStack getMoneyItem(double amount) {
        // If the amount requested is smaller or equal to zero, return nothing.
        if (amount <= 0.0) return null;

        // Create the item stack using configured material, and get item metadata.
        ItemStack item = new ItemStack(getMaterial(), 1);
        ItemMeta meta = item.getItemMeta();

        // Add the funds to the item meta, and change the display name to the configured name.
        EconomyManager.addFunds(amount, meta);

        // Get the display name component.
        Component displayName = getNameComponent(amount != 1.0);

        // Make sure the item name is not italicized.
        displayName.style().decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);

        // Apply the display name component.
        meta.displayName(getNameComponent(amount != 1.0));

        // Put item metadata back to the stack.
        item.setItemMeta(meta);

        return item;
    }
}
