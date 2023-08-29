package nl.itz_kiwisap_.spigot.pergroupdrops.api;

import nl.itz_kiwisap_.spigot.pergroupdrops.api.provider.GlowProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.api.provider.GroupProvider;
import org.jetbrains.annotations.NotNull;

public final class KiwiPerGroupDropsAPI {

    private static KiwiPerGroupDrops INSTANCE;

    public static void setGroupProvider(@NotNull GroupProvider provider) {
        INSTANCE.setGroupProvider(provider);
    }

    public static void setGlowProvider(@NotNull GlowProvider provider) {
        INSTANCE.setGlowProvider(provider);
    }
}