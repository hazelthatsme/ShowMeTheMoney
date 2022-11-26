package com.rootnroute.rpg.showmethemoney.commands;

import com.rootnroute.rpg.showmethemoney.Main;
import com.rootnroute.rpg.showmethemoney.MoneyItem;
import com.rootnroute.rpg.showmethemoney.economy.EconomyManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;

public class BalanceCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Double check if the internal economy system isn't being used.
        if (!Main.usingInternalEconomy()) return false;

        // Check if the sender is capable of holding persistent data, which is required for the internal economy system.
        if (sender instanceof PersistentDataHolder) {
            // Get the sender's balance.
            double balance = EconomyManager.getHoldingCurrency((PersistentDataHolder) sender);

            // Send balance message, with a rounded amount according to the economy manager's decimal format.
            sender.sendMessage(Component.text().append(
                    Component.text("Your balance: ").color(TextColor.color(Color.GREEN.asRGB())),
                    Component.text(EconomyManager.format(balance) + " ").color(MoneyItem.getColor()),
                    MoneyItem.getNameComponent(balance != 1.0)
            ));
        }
        return true;
    }
}
