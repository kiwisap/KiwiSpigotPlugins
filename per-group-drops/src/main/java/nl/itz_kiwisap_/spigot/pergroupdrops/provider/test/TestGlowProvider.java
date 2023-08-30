package nl.itz_kiwisap_.spigot.pergroupdrops.provider.test;

import nl.itz_kiwisap_.spigot.pergroupdrops.provider.GlowColor;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GlowProvider;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestGlowProvider implements GlowProvider {
    @Override
    public @Nullable GlowColor getGlowColor(@NotNull String group, @NotNull ItemStack itemStack) {
        if (group.equalsIgnoreCase("itz_kiwisap_")) {
            return GlowColor.BLUE;
        }

        return null;
    }
}
