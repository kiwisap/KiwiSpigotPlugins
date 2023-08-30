package nl.itz_kiwisap_.spigot.pergroupdrops.settings;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public enum KiwiPerGroupDropsSetting {

    HIDE_ITEMS_FROM_OTHER_GROUPS("hide-items-from-other-groups", true);

    private final String key;
    private final Object defaultValue;

    KiwiPerGroupDropsSetting(String key, Object defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public boolean getBoolean(@NotNull KiwiPerGroupDropsSettingsProvider provider) {
        try {
            return (boolean) provider.get(this);
        } catch (Throwable throwable) {
            return (boolean) this.defaultValue;
        }
    }
}