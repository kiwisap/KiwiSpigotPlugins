package nl.itz_kiwisap_.spigot.pergroupdrops.api;

import nl.itz_kiwisap_.spigot.pergroupdrops.api.provider.GlowProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.api.provider.GroupProvider;
import org.jetbrains.annotations.NotNull;

public interface KiwiPerGroupDrops {

    void setGroupProvider(@NotNull GroupProvider provider);

    void setGlowProvider(@NotNull GlowProvider provider);
}