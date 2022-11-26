package com.rootnroute.rpg.showmethemoney.listeners;

import com.rootnroute.rpg.showmethemoney.Main;
import com.rootnroute.rpg.showmethemoney.MoneyItem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Random;

public class EntityDeathListener implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        // Get the entity's last damage cause.
        @Nullable EntityDamageEvent cause = e.getEntity().getLastDamageCause();

        // Determine if the cause was damage by another entity, if not, stop here.
        if (!(cause instanceof EntityDamageByEntityEvent)) return;
        EntityDamageByEntityEvent resolvedCause = (EntityDamageByEntityEvent) cause;

        // If the cause's damager isn't a player, stop here.
        if (!(resolvedCause.getDamager() instanceof Player)) return;

        // Get the value of the entity.
        double amount = getMoneyValue(e.getEntityType());

        // If the amount is greater than zero, add a money item to the entity's drops.
        if (amount > 0) e.getDrops().add(MoneyItem.getMoneyItem(amount));
    }

    private static final Random rng = new Random();

    public static double getMoneyValue(EntityType entityType) {
        // Lowercase name of the entity type.
        String lowercaseEntity = entityType.toString().toLowerCase(Locale.ROOT);

        // Get the section of the configuration where entities' values are established.
        ConfigurationSection entities = Main.getInstanceConfig().getConfigurationSection("entities");

        // If the entities section doesn't contain a definition for the entity, return a zero value.
        if (!entities.contains(lowercaseEntity)) return 0;

        // Get the section where this entity's minimum and maximum values are defined.
        ConfigurationSection thisEntity = Main.getInstanceConfig().getConfigurationSection("entities." + lowercaseEntity);

        // The eventual value of the entity.
        double value;

        // If this entity inherits from another entity type, return that inheritance's values.
        if (thisEntity.contains("inherit")) {
            // Get a money value from the inherited entity.
            double inheritedValue = getMoneyValue(EntityType.valueOf(thisEntity.getString("inherit").toUpperCase(Locale.ROOT)));

            // If the inheriting mob adds a multiplier, multiply the initial value by that multiplier.
            inheritedValue *= thisEntity.contains("multiplier") ? thisEntity.getDouble("multiplier") : 1.0;

            // Set the entity's value to the now determined, inherited value.
            return inheritedValue;
        } else {
            // Establish minimum and maximum values.
            double min = thisEntity.getDouble("min");
            double max = thisEntity.getDouble("max");

            // Get a random double value between the two bounds.
            value = rng.nextDouble(min, max);
        }

        // Return the determined value rounded to two decimal places.
        return Math.round(value * 100.0) / 100.0;
    }
}
