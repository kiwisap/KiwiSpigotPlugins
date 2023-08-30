package nl.itz_kiwisap_.spigot.common;

import lombok.Getter;
import nl.itz_kiwisap_.spigot.common.network.interceptor.PacketInterceptorHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

@Getter
public final class KiwiSpigotLibrary {

    private static KiwiSpigotLibrary INSTANCE = null;

    public static @NotNull KiwiSpigotLibrary register(JavaPlugin plugin) {
        if (INSTANCE == null) {
            RegisteredServiceProvider<KiwiSpigotLibrary> registration = Bukkit.getServicesManager().getRegistration(KiwiSpigotLibrary.class);
            if (registration == null) {
                INSTANCE = new KiwiSpigotLibrary(plugin);
                Bukkit.getServicesManager().register(KiwiSpigotLibrary.class, INSTANCE, plugin, ServicePriority.Normal);
            } else {
                INSTANCE = registration.getProvider();
            }
        }

        return INSTANCE;
    }

    private final PacketInterceptorHandler packetInterceptorHandler;

    private KiwiSpigotLibrary(JavaPlugin plugin) {
        try {
            KiwiNMSInitializer.initialize();
        } catch (Exception exception) {
            Bukkit.shutdown();
            plugin.getLogger().log(Level.WARNING, "Failed to initialize KiwiNMS");
            throw new RuntimeException(exception);
        }

        this.packetInterceptorHandler = new PacketInterceptorHandler(plugin);
    }
}