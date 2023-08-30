package nl.itz_kiwisap_.spigot.pergroupdrops.plugin;

import nl.itz_kiwisap_.spigot.common.network.interceptor.PacketInterceptorHandler;
import nl.itz_kiwisap_.spigot.pergroupdrops.KiwiPerGroupDrops;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.KiwiPerGroupDropsProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GlowProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GroupProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.scoreboard.PerGroupDropsScoreboardHandler;
import nl.itz_kiwisap_.spigot.pergroupdrops.settings.KiwiPerGroupDropsSettingsProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class KiwiPerGroupDropsPlugin extends JavaPlugin implements KiwiPerGroupDrops {

    private KiwiPerGroupDrops instance;

    @Override
    public void onEnable() {
        super.saveDefaultConfig();

        KiwiPerGroupDropsPluginSettingsProvider settingsProvider = new KiwiPerGroupDropsPluginSettingsProvider(this, super.getConfig());
        this.instance = KiwiPerGroupDrops.create(this, settingsProvider);
    }

    @Override
    public @NotNull JavaPlugin getPlugin() {
        return this;
    }

    @Override
    public @NotNull KiwiPerGroupDropsSettingsProvider getSettingsProvider() {
        return this.instance.getSettingsProvider();
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