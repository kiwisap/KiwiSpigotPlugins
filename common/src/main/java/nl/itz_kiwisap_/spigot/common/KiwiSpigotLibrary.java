package nl.itz_kiwisap_.spigot.common;

import nl.itz_kiwisap_.spigot.common.network.interceptor.PacketInterceptorHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public final class KiwiSpigotLibrary {

    private final PacketInterceptorHandler packetInterceptorHandler;

    KiwiSpigotLibrary(JavaPlugin plugin) {
        try {
            KiwiNMSInitializer.initialize();
        } catch (Exception exception) {
            Bukkit.shutdown();
            plugin.getLogger().log(Level.WARNING, "Failed to initialize KiwiNMS");
            throw new RuntimeException(exception);
        }

        this.packetInterceptorHandler = new PacketInterceptorHandler(plugin);
    }

    public @NotNull PacketInterceptorHandler getPacketInterceptorHandler() {
        return this.packetInterceptorHandler;
    }
}