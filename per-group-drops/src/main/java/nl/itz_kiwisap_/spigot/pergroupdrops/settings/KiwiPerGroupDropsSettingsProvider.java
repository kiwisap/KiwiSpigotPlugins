package nl.itz_kiwisap_.spigot.pergroupdrops.settings;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.HashMap;
import java.util.Map;

public interface KiwiPerGroupDropsSettingsProvider {

    @NotNull KiwiPerGroupDropsSettingsProvider DEFAULT = new KiwiPerGroupDropsSettingsProvider() {
        @Override
        public @NotNull Map<KiwiPerGroupDropsSetting, Object> getSettings() {
            return new HashMap<>();
        }

        @Override
        public @UnknownNullability Object get(@NotNull KiwiPerGroupDropsSetting setting) {
            return setting.getDefaultValue();
        }

        @Override
        public void load() {
        }
    };

    @NotNull Map<KiwiPerGroupDropsSetting, Object> getSettings();

    @UnknownNullability Object get(@NotNull KiwiPerGroupDropsSetting setting);

    void load();
}