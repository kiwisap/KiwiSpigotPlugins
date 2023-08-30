package nl.itz_kiwisap_.spigot.pergroupdrops.provider;

import nl.itz_kiwisap_.spigot.pergroupdrops.KiwiPerGroupDrops;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.impl.DefaultGlowProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GlowProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GroupProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class KiwiPerGroupDropsProvider {

    private GroupProvider groupProvider;
    private GlowProvider glowProvider;

    public KiwiPerGroupDropsProvider(KiwiPerGroupDrops instance) {
        this.glowProvider = new DefaultGlowProvider(instance);
    }

    public void setGroupProvider(@NotNull GroupProvider groupProvider) {
        this.groupProvider = groupProvider;
    }

    public @Nullable GroupProvider getGroupProvider() {
        return this.groupProvider;
    }

    public void setGlowProvider(@NotNull GlowProvider glowProvider) {
        this.glowProvider = glowProvider;
    }

    public @Nullable GlowProvider getGlowProvider() {
        return this.glowProvider;
    }
}