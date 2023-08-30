package nl.itz_kiwisap_.spigot.pergroupdrops;

import lombok.Getter;
import nl.itz_kiwisap_.spigot.common.KiwiSpigotLibrary;
import nl.itz_kiwisap_.spigot.common.network.interceptor.PacketInterceptorHandler;
import nl.itz_kiwisap_.spigot.pergroupdrops.drop.ItemDropHandler;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GlowProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GroupProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.KiwiPerGroupDropsProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.scoreboard.PerGroupDropsScoreboardHandler;
import nl.itz_kiwisap_.spigot.pergroupdrops.settings.KiwiPerGroupDropsSettingsProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Getter
final class KiwiPerGroupDropsImpl implements KiwiPerGroupDrops, Listener {

    private static final Map<JavaPlugin, KiwiPerGroupDropsImpl> INSTANCES = new HashMap<>();

    public static KiwiPerGroupDrops getInstance(JavaPlugin plugin) {
        return INSTANCES.get(plugin);
    }

    // Make scoreboard handler static so the color teams are only created once
    private static PerGroupDropsScoreboardHandler scoreboardHandler;

    private final JavaPlugin plugin;
    private final KiwiPerGroupDropsSettingsProvider settingsProvider;
    private final PacketInterceptorHandler packetInterceptorHandler;
    private final KiwiPerGroupDropsProvider provider;

    KiwiPerGroupDropsImpl(JavaPlugin plugin, KiwiPerGroupDropsSettingsProvider settingsProvider) {
        KiwiSpigotLibrary library = KiwiSpigotLibrary.register(plugin);

        this.plugin = plugin;
        this.settingsProvider = settingsProvider;
        this.settingsProvider.load();

        this.packetInterceptorHandler = library.getPacketInterceptorHandler();
        this.provider = new KiwiPerGroupDropsProvider();

        // Only register the scoreboard when there are no instances yet
        if (scoreboardHandler == null) {
            scoreboardHandler = new PerGroupDropsScoreboardHandler();
            plugin.getServer().getPluginManager().registerEvents(scoreboardHandler, plugin);
        }

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getServer().getPluginManager().registerEvents(new ItemDropHandler(this), plugin);

        INSTANCES.put(plugin, this);
    }

    @Override
    public @NotNull PerGroupDropsScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }

    @Override
    public void setGroupProvider(@NotNull GroupProvider groupProvider) {
        this.provider.setGroupProvider(groupProvider);
    }

    @Override
    public void setGlowProvider(@NotNull GlowProvider glowProvider) {
        this.provider.setGlowProvider(glowProvider);
    }

    @EventHandler
    private void onPluginDisable(PluginDisableEvent event) {
        if (!(event.getPlugin() instanceof JavaPlugin javaPlugin)) return;

        INSTANCES.remove(javaPlugin);
        HandlerList.unregisterAll(javaPlugin);

        // If there are still instances left, register scoreboard handler to the first instance
        if (!INSTANCES.isEmpty() && scoreboardHandler != null) {
            for (JavaPlugin plugin : INSTANCES.keySet()) {
                plugin.getServer().getPluginManager().registerEvents(scoreboardHandler, plugin);
                break;
            }
        }
    }
}