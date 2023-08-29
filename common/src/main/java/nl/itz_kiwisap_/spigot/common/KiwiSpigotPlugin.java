package nl.itz_kiwisap_.spigot.common;

import lombok.Getter;
import nl.itz_kiwisap_.spigot.common.network.interceptor.PacketInterceptorHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@Getter
public abstract class KiwiSpigotPlugin extends JavaPlugin {

    private PacketInterceptorHandler packetInterceptorHandler;

    @Override
    public final void onLoad() {
        try {
            KiwiNMSInitializer.initialize();
        } catch (Exception exception) {
            Bukkit.shutdown();
            super.getLogger().log(Level.WARNING, "Failed to initialize KiwiNMS", exception);
            return;
        }

        this.packetInterceptorHandler = new PacketInterceptorHandler(this);

        this.load();
    }

    public void load() {
    }
}