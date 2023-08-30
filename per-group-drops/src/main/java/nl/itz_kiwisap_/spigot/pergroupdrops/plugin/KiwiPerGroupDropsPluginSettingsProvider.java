package nl.itz_kiwisap_.spigot.pergroupdrops.plugin;

import nl.itz_kiwisap_.spigot.pergroupdrops.settings.KiwiPerGroupDropsSetting;
import nl.itz_kiwisap_.spigot.pergroupdrops.settings.KiwiPerGroupDropsSettingsProvider;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

final class KiwiPerGroupDropsPluginSettingsProvider implements KiwiPerGroupDropsSettingsProvider {

    private static final String SETTINGS_PREFIX = "settings.";

    private final Map<KiwiPerGroupDropsSetting, Object> settings = new HashMap<>();

    private final KiwiPerGroupDropsPlugin plugin;
    private final FileConfiguration configuration;

    KiwiPerGroupDropsPluginSettingsProvider(KiwiPerGroupDropsPlugin plugin, FileConfiguration configuration) {
        this.plugin = plugin;
        this.configuration = configuration;
    }

    @Override
    public @NotNull Map<KiwiPerGroupDropsSetting, Object> getSettings() {
        return this.settings;
    }

    @Override
    public Object get(@NotNull KiwiPerGroupDropsSetting setting) {
        return this.settings.get(setting);
    }

    String getGroupProviderHook() {
        return this.configuration.getString("hooks.group-provider");
    }

    String getGlowProviderHook() {
        return this.configuration.getString("hooks.glow-provider");
    }

    @Override
    public void load() {
        this.settings.clear();

        this.plugin.reloadConfig();

        for (KiwiPerGroupDropsSetting setting : KiwiPerGroupDropsSetting.values()) {
            Object value = this.configuration.get(SETTINGS_PREFIX + setting.getKey());
            if (value == null) continue;

            this.settings.put(setting, value);
        }
    }
}