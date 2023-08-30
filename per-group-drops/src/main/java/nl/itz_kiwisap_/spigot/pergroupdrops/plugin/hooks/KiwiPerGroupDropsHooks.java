package nl.itz_kiwisap_.spigot.pergroupdrops.plugin.hooks;

import nl.itz_kiwisap_.spigot.pergroupdrops.plugin.KiwiPerGroupDropsPlugin;
import nl.itz_kiwisap_.spigot.pergroupdrops.plugin.hooks.bentobox.BentoBoxHook;
import nl.itz_kiwisap_.spigot.pergroupdrops.plugin.hooks.iridiumskyblock.IridiumSkyblockHook;
import nl.itz_kiwisap_.spigot.pergroupdrops.plugin.hooks.superiorskyblock2.SuperiorSkyblock2Hook;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GlowProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GroupProvider;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public final class KiwiPerGroupDropsHooks {

    private static final String CUSTOM = "Custom";

    private final Map<String, Class<? extends PluginHook>> hooks = new HashMap<>();

    private final KiwiPerGroupDropsPlugin plugin;

    public KiwiPerGroupDropsHooks(KiwiPerGroupDropsPlugin plugin) {
        this.plugin = plugin;

        this.hooks.put(BentoBoxHook.NAME.toLowerCase(), BentoBoxHook.class);
        this.hooks.put(IridiumSkyblockHook.NAME.toLowerCase(), IridiumSkyblockHook.class);
        this.hooks.put(SuperiorSkyblock2Hook.NAME.toLowerCase(), SuperiorSkyblock2Hook.class);
    }

    public void enableGroupProviderHook(String hookName) {
        if (hookName == null || hookName.trim().isEmpty() || hookName.equalsIgnoreCase(CUSTOM)) {
            this.plugin.getLogger().log(Level.INFO, "Using custom group provider hook... Feel free to use the API! :)");
            return;
        }

        Class<? extends PluginHook> pluginHook = this.hooks.get(hookName.toLowerCase());
        if (pluginHook == null) {
            this.plugin.getLogger().log(Level.WARNING, "Group provider hook '" + hookName + "' not found!");
            return;
        }

        try {
            PluginHook hook = pluginHook.getConstructor().newInstance();
            if (hook.isEnabled()) {
                GroupProvider groupProvider = hook.groupProvider();
                if (groupProvider != null) {
                    this.plugin.setGroupProvider(groupProvider);
                }

                this.plugin.getLogger().log(Level.INFO, "Group provider hook '" + hook.getName() + "' enabled!");
            } else {
                this.plugin.getLogger().log(Level.WARNING, "Plugin for group provider hook '" + hook.getName() + "' not enabled!");
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException exception) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to enable group provider hook '" + hookName + "'!", exception);
        }
    }

    public void enableGlowProviderHook(String hookName) {
        if (hookName == null || hookName.trim().isEmpty() || hookName.equalsIgnoreCase(CUSTOM)) {
            this.plugin.getLogger().log(Level.INFO, "Using custom glow provider hook... Feel free to use the API! :)");
            return;
        }

        Class<? extends PluginHook> pluginHook = this.hooks.get(hookName.toLowerCase());
        if (pluginHook == null) {
            this.plugin.getLogger().log(Level.WARNING, "Glow provider hook '" + hookName + "' not found!");
            return;
        }

        try {
            PluginHook hook = pluginHook.getConstructor().newInstance();
            if (hook.isEnabled()) {
                GlowProvider glowProvider = hook.glowProvider();
                if (glowProvider != null) {
                    this.plugin.setGlowProvider(glowProvider);
                }

                this.plugin.getLogger().log(Level.INFO, "Glow provider hook '" + hook.getName() + "' enabled!");
            } else {
                this.plugin.getLogger().log(Level.WARNING, "Plugin for glow provider hook '" + hook.getName() + "' not enabled!");
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException exception) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to enable glow provider hook '" + hookName + "'!", exception);
        }
    }
}