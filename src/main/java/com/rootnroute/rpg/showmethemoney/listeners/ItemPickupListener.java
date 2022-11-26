package com.rootnroute.rpg.showmethemoney.listeners;

import com.rootnroute.rpg.showmethemoney.Main;
import com.rootnroute.rpg.showmethemoney.MoneyItem;
import com.rootnroute.rpg.showmethemoney.Utils;
import com.rootnroute.rpg.showmethemoney.economy.EconomyManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;

public class ItemPickupListener implements Listener {
    @EventHandler
    public void onPlayerPickup(PlayerAttemptPickupItemEvent e) {
        if (EconomyManager.getHoldingCurrency(e.getItem().getItemStack().getItemMeta()) > 0.0) {
            double value = EconomyManager.getHoldingCurrency(e.getItem().getItemStack().getItemMeta());

            // Determine how to round the picked up amount by checking if the internal economy system is being used.
            String amount;
            if (Main.usingInternalEconomy()) amount = EconomyManager.format(value);
            else amount = Main.getVaultEconomy().format(value);

            // Play the configured SFX.
            e.getPlayer().playSound(e.getPlayer().getLocation(), getSound(), getVolume(), getPitch());

            // Send a message telling the player they picked up the money.
            e.getPlayer().sendMessage(Component.text().append(
                    Component.text("You picked up ").color(TextColor.color(Color.GREEN.asRGB())),
                    Component.text(amount + " ").color(MoneyItem.getColor()),
                    MoneyItem.getNameComponent(value != 1.0),
                    Component.text("!")).color(TextColor.color(Color.GREEN.asRGB()))
            );

            // Add the funds to the player, depending on the economy system being used.
            if (Main.usingInternalEconomy()) EconomyManager.addFunds(value, e.getPlayer());
            else Main.getVaultEconomy().depositPlayer(e.getPlayer(), value);

            // Despawn the item entity so it isn't picked up again.
            e.getItem().remove();

            // Prevent the item pick up event.
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityPickup(EntityPickupItemEvent e) {
        // If the entity picking up the item is a player, don't check the rest.
        if (e.getEntity() instanceof Player) return;

        // If the entity is trying to pick up a money item, don't let them.
        if (EconomyManager.getHoldingCurrency(e.getItem().getItemStack().getItemMeta()) > 0.0) {
            e.setCancelled(true);
        }
    }

    public static Sound getSound() {
        // Using the Utils provided, validate the provided sound, or fall back to a Note Block Pling sound.
        return Utils.validateSoundValueOf(Main.getInstanceConfig().getString("sfx.sound"), Sound.BLOCK_NOTE_BLOCK_PLING);
    }

    public static float getVolume() {
        // Set default volume.
        float volume = 1.0f;

        // Try to read the volume from the config, otherwise use the fallback value.
        try {
            volume = (float) Main.getInstanceConfig().getDouble("sfx.volume");
        } catch (Exception ex) { Main.log("SFX volume couldn't be acquired from the config. Please check if your configuration is valid!"); }

        return volume;
    }

    public static float getPitch() {
        // Set fallback pitch.
        float pitch = 1.0f;

        // Try to read the pitch from the config, otherwise use the fallback value.
        try {
            pitch = (float) Main.getInstanceConfig().getDouble("sfx.pitch");
        } catch (Exception ex) { Main.log("SFX pitch couldn't be acquired from the config. Please check if your configuration is valid!"); }

        return pitch;
    }
}
