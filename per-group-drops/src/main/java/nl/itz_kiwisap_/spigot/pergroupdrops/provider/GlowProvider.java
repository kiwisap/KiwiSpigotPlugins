package nl.itz_kiwisap_.spigot.pergroupdrops.provider;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GlowProvider {

    @Nullable ChatColor getGlowColor(@NotNull String group, @NotNull ItemStack itemStack);
}