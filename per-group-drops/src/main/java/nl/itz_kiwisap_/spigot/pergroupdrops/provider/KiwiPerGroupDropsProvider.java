package nl.itz_kiwisap_.spigot.pergroupdrops.provider;

import lombok.Getter;
import lombok.Setter;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GlowProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GroupProvider;

@Getter
@Setter
public final class KiwiPerGroupDropsProvider {

    private GroupProvider groupProvider;
    private GlowProvider glowProvider;
}