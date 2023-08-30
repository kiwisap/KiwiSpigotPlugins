package nl.itz_kiwisap_.spigot.pergroupdrops.provider.impl;

import nl.itz_kiwisap_.spigot.pergroupdrops.KiwiPerGroupDrops;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.GlowColor;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GlowProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.settings.KiwiPerGroupDropsSetting;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DefaultGlowProvider implements GlowProvider {

    private final KiwiPerGroupDrops instance;

    public DefaultGlowProvider(KiwiPerGroupDrops instance) {
        this.instance = instance;
    }

    @Override
    public @Nullable GlowColor getGlowColor(@NotNull String group, @NotNull ItemStack itemStack) {
        String glowColorString = KiwiPerGroupDropsSetting.DEFAULT_GROUP_GLOW_COLOR.getString(this.instance.getSettingsProvider());
        return glowColorString == null ? null : GlowColor.getByName(glowColorString);
    }
}