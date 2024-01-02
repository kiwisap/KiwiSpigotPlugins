package nl.itz_kiwisap_.spigot.customrecipes.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class InventoryUtils {

    private InventoryUtils() {
    }

    public static int getAmountOfFreeSlots(Player player) {
        ItemStack[] items = player.getInventory().getStorageContents();
        int freeSlots = 0;

        for (ItemStack item : items) {
            if (item == null || item.getType() == Material.AIR) {
                freeSlots++;
            }
        }

        return freeSlots;
    }
}