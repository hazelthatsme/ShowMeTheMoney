package com.rootnroute.rpg.showmethemoney;

import org.bukkit.Material;
import org.bukkit.Sound;

public class Utils {
    public static Material validateMaterialValueOf(String valueOf, Material fallback) {
        try {
            return Material.valueOf(valueOf);
        } catch (IllegalArgumentException ex) {
            Main.log("Invalid Material, falling back.");
            return fallback;
        }
    }

    public static Sound validateSoundValueOf(String valueOf, Sound fallback) {
        try {
            return Sound.valueOf(valueOf);
        } catch (IllegalArgumentException ex) {
            Main.log("Invalid Sound, falling back.");
            return fallback;
        }
    }
}
