package nl.itz_kiwisap_.spigot.pergroupdrops;

import nl.itz_kiwisap_.spigot.common.network.interceptor.PacketInterceptorHandler;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GlowProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GroupProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.KiwiPerGroupDropsProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.scoreboard.PerGroupDropsScoreboardHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class KiwiPerGroupDropsPlugin extends JavaPlugin implements KiwiPerGroupDrops {

    private KiwiPerGroupDrops instance;

    @Override
    public void onEnable() {
        this.instance = KiwiPerGroupDrops.create(this);
    }

    @Override
    public @NotNull JavaPlugin getPlugin() {
        return this;
    }

    @Override
    public @NotNull PacketInterceptorHandler getPacketInterceptorHandler() {
        return this.instance.getPacketInterceptorHandler();
    }

    @Override
    public @NotNull KiwiPerGroupDropsProvider getProvider() {
        return this.instance.getProvider();
    }

    @Override
    public @NotNull PerGroupDropsScoreboardHandler getScoreboardHandler() {
        return this.instance.getScoreboardHandler();
    }

    @Override
    public void setGroupProvider(@NotNull GroupProvider groupProvider) {
        this.instance.setGroupProvider(groupProvider);
    }

    @Override
    public void setGlowProvider(@NotNull GlowProvider glowProvider) {
        this.instance.setGlowProvider(glowProvider);
    }
}