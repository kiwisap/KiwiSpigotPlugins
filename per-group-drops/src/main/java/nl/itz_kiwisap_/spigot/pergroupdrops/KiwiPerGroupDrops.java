package nl.itz_kiwisap_.spigot.pergroupdrops;

import nl.itz_kiwisap_.spigot.common.network.interceptor.PacketInterceptorHandler;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.GlowProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.GroupProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.KiwiPerGroupDropsProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.scoreboard.PerGroupDropsScoreboardHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface KiwiPerGroupDrops {

    static @NotNull KiwiPerGroupDrops create(@NotNull JavaPlugin plugin) {
        return new KiwiPerGroupDropsImpl(plugin);
    }

    static @Nullable KiwiPerGroupDrops getInstance(@NotNull JavaPlugin plugin) {
        return KiwiPerGroupDropsImpl.getInstance(plugin);
    }

    @NotNull JavaPlugin getPlugin();

    @ApiStatus.Internal
    @NotNull PacketInterceptorHandler getPacketInterceptorHandler();

    @NotNull KiwiPerGroupDropsProvider getProvider();

    @ApiStatus.Internal
    @NotNull PerGroupDropsScoreboardHandler getScoreboardHandler();

    void setGroupProvider(@NotNull GroupProvider groupProvider);

    void setGlowProvider(@NotNull GlowProvider glowProvider);
}