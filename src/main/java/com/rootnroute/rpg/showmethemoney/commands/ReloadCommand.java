package com.rootnroute.rpg.showmethemoney.commands;

import com.rootnroute.rpg.showmethemoney.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Double check if the sender has the proper permissions to reload the plugin.
        if (sender.hasPermission("showmethemoney.reload")) {
            Main.reloadInstanceConfig();

            // Send a confirmation message to the sender.
            sender.sendMessage(Component.text("ShowMeTheMoney was reloaded.").color(TextColor.color(Color.GREEN.asRGB())));

            // If the console itself didn't reload the plugin, tell it who did.
            if (!(sender instanceof ConsoleCommandSender)) Main.log("ShowMeTheMoney was reloaded by " + sender.getName());
        }
        return true;
    }
}
