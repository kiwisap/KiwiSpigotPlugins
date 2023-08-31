package nl.itz_kiwisap_.spigot.common;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class KiwiSpigotLibraryGetter {

    public static Object get(@NotNull JavaPlugin plugin) {
        for (Class<?> knownService : Bukkit.getServicesManager().getKnownServices()) {
            if (knownService.getSimpleName().equals("KiwiSpigotLibrary")) {
                return Bukkit.getServicesManager().load(knownService);
            }
        }

        KiwiSpigotLibrary kiwiSpigotLibrary = new KiwiSpigotLibrary(plugin);
        Bukkit.getServicesManager().register(KiwiSpigotLibrary.class, kiwiSpigotLibrary, plugin, ServicePriority.Normal);
        return kiwiSpigotLibrary;
    }
}