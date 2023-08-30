package nl.itz_kiwisap_.spigot.pergroupdrops.provider;

import lombok.Getter;
import lombok.Setter;
import nl.itz_kiwisap_.spigot.pergroupdrops.KiwiPerGroupDrops;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.impl.DefaultGlowProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GlowProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GroupProvider;

@Getter
@Setter
public final class KiwiPerGroupDropsProvider {

    private GroupProvider groupProvider;
    private GlowProvider glowProvider;

    public KiwiPerGroupDropsProvider(KiwiPerGroupDrops instance) {
        this.glowProvider = new DefaultGlowProvider(instance);
    }
}