package com.rootnroute.rpg.showmethemoney;

import com.rootnroute.rpg.showmethemoney.commands.BalanceCommand;
import com.rootnroute.rpg.showmethemoney.commands.ReloadCommand;
import com.rootnroute.rpg.showmethemoney.listeners.ItemPickupListener;
import com.rootnroute.rpg.showmethemoney.listeners.EntityDeathListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private static Main instance;

    private static boolean usingInternalEconomySystem = false;

    private static Economy vaultEconomy;

    @Override
    public void onEnable() {
        // Set the plugin instance.
        instance = this;

        // Load the plugin config.
        loadConfig();

        // Try to load a vault economy system.
        if (!setupEconomy()) {
            // If the plugin could not find Vault's economy provider, use the internal economy system instead.
            log("Vault not found, using internal economy system.");

            usingInternalEconomySystem = true;

            // Register the plugin's own balance command.
            getCommand("balance").setExecutor(new BalanceCommand());
        }

        // Register events for entities dying and players attempting to pick up items.
        registerEvents(new EntityDeathListener(), new ItemPickupListener());

        // Register the plugin's reload command.
        getCommand("showmethereload").setExecutor(new ReloadCommand());

        getLogger().info("Plugin loaded.");
    }

    // Economy setup taken from https://github.com/MilkBowl/VaultAPI/blob/master/README.md
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;

        vaultEconomy = rsp.getProvider();
        return vaultEconomy != null;
    }

    // Register all listeners to this instance.
    private void registerEvents(Listener... listeners) {
        for (Listener l : listeners)
            Bukkit.getPluginManager().registerEvents(l, instance);
    }

    // Load the default config if it is not present.
    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // To prevent potential memory leaks, nullify the instance.
        instance = null;

        getLogger().info("Plugin unloaded.");
    }

    // Get if the internal economy system is being used.
    public static boolean usingInternalEconomy() { return usingInternalEconomySystem; }

    // Get the current plugin instance.
    public static Main getInstance() { return instance; }

    // Get the instance's Vault economy provider.
    public static Economy getVaultEconomy() { return vaultEconomy; }

    // Reload the instance's config.
    public static void reloadInstanceConfig() { getInstance().reloadConfig(); }

    // Get the instance's loaded config.
    public static FileConfiguration getInstanceConfig() { return getInstance().getConfig(); }

    // Log an object.
    public static void log(Object message) { log(message.toString()); }

    // Log a string message.
    public static void log(String message) { instance.getLogger().info(message); }
}
