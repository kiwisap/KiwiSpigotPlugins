package nl.itz_kiwisap_.spigot.pergroupdrops.provider;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum GlowColor {
    BLACK(ChatColor.BLACK),
    DARK_BLUE(ChatColor.DARK_BLUE),
    DARK_GREEN(ChatColor.DARK_GREEN),
    DARK_AQUA(ChatColor.DARK_AQUA),
    DARK_RED(ChatColor.DARK_RED),
    DARK_PURPLE(ChatColor.DARK_PURPLE),
    GOLD(ChatColor.GOLD),
    GRAY(ChatColor.GRAY),
    DARK_GRAY(ChatColor.DARK_GRAY),
    BLUE(ChatColor.BLUE),
    GREEN(ChatColor.GREEN),
    AQUA(ChatColor.AQUA),
    RED(ChatColor.RED),
    LIGHT_PURPLE(ChatColor.LIGHT_PURPLE),
    YELLOW(ChatColor.YELLOW),
    WHITE(ChatColor.WHITE);

    private final ChatColor chatColor;

    GlowColor(ChatColor chatColor) {
        this.chatColor = chatColor;
    }

    public @NotNull ChatColor getChatColor() {
        return this.chatColor;
    }

    public static @Nullable GlowColor getByName(@NotNull String color) {
        for (GlowColor glowColor : values()) {
            if (glowColor.name().equalsIgnoreCase(color)) {
                return glowColor;
            }
        }

        return null;
    }
}