package nl.itz_kiwisap_.spigot.customrecipes.recipe;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class Recipe {

    private final NamespacedKey key;

    protected Recipe(@NotNull NamespacedKey key) {
        this.key = key;
    }

    public abstract @NotNull ItemStack result();

    public final @NotNull NamespacedKey key() {
        return this.key;
    }
}