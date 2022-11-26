package com.rootnroute.rpg.showmethemoney.economy;

import com.rootnroute.rpg.showmethemoney.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import java.text.DecimalFormat;

public class EconomyManager {
    public static final NamespacedKey holdingCurrency = new NamespacedKey(Main.getInstance(), "holding");
    public static final PersistentDataType holdingCurrencyType = PersistentDataType.DOUBLE;
    public static final DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    public static double getHoldingCurrency(PersistentDataHolder holder) {
        PersistentDataContainer container = holder.getPersistentDataContainer();

        double holding = 0.0;
        if (container.has(holdingCurrency)) {
            try {
                holding = (double) container.get(holdingCurrency, holdingCurrencyType);
            } catch (Exception ex) {
                Main.log("Could not attain money held by " + holder + " returning zero.");
            }
        }

        return holding;
    }

    public static void addFunds(double amount, PersistentDataHolder holder) {
        PersistentDataContainer container = holder.getPersistentDataContainer();

        double current = getHoldingCurrency(holder);
        container.set(holdingCurrency, holdingCurrencyType, current + amount);
    }

    public static String format(Number n) {
        return decimalFormat.format(n);
    }
}
