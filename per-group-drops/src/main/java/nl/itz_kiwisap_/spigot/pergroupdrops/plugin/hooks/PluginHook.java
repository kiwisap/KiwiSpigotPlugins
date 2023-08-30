package nl.itz_kiwisap_.spigot.pergroupdrops.plugin.hooks;

import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GlowProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GroupProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PluginHook {

    boolean isEnabled();

    @NotNull String getName();

    @Nullable GroupProvider groupProvider();

    @Nullable GlowProvider glowProvider();
}