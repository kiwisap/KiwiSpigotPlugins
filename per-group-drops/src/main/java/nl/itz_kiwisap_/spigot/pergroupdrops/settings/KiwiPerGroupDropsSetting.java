package nl.itz_kiwisap_.spigot.pergroupdrops.settings;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum KiwiPerGroupDropsSetting {

    HIDE_ITEMS_FROM_OTHER_GROUPS("hide-items-from-other-groups", true),
    DEFAULT_GROUP_GLOW_COLOR("default-group-glow-color", "WHITE");

    private final String key;
    private final Object defaultValue;

    KiwiPerGroupDropsSetting(String key, Object defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public @NotNull String getKey() {
        return this.key;
    }

    public @Nullable Object getDefaultValue() {
        return this.defaultValue;
    }

    public boolean getBoolean(@NotNull KiwiPerGroupDropsSettingsProvider provider) {
        try {
            Object value = provider.get(this);
            return value == null ? (boolean) this.defaultValue : (boolean) value;
        } catch (Throwable throwable) {
            return (boolean) this.defaultValue;
        }
    }

    public String getString(@NotNull KiwiPerGroupDropsSettingsProvider provider) {
        try {
            return (String) provider.get(this);
        } catch (Throwable throwable) {
            return (String) this.defaultValue;
        }
    }
}