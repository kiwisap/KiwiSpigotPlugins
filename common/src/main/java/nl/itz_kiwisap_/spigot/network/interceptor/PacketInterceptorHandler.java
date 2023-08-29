package nl.itz_kiwisap_.spigot.network.interceptor;

import io.netty.channel.Channel;
import nl.itz_kiwisap_.spigot.nms.KiwiNMS;
import nl.itz_kiwisap_.spigot.nms.network.KPacket;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public final class PacketInterceptorHandler implements Listener {

    private static final String NAME = "kiwi-packet-handler";

    final Map<Class<? extends KPacket>, Collection<PacketInterceptor<?>>> clientboundInterceptors = new HashMap<>();
    final Map<Class<? extends KPacket>, Collection<PacketInterceptor<?>>> serverboundInterceptors = new HashMap<>();

    public PacketInterceptorHandler(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public <T extends KPacket> void interceptClientbound(Class<T> packetClass, PacketInterceptor<T> interceptor) {
        this.clientboundInterceptors.compute(packetClass, (key, value) -> {
            if (value == null) value = new HashSet<>();
            value.add(interceptor);
            return value;
        });
    }

    public <T extends KPacket> void interceptServerbound(Class<T> packetClass, PacketInterceptor<T> interceptor) {
        this.serverboundInterceptors.compute(packetClass, (key, value) -> {
            if (value == null) value = new HashSet<>();
            value.add(interceptor);
            return value;
        });
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Channel channel = KiwiNMS.getInstance().getPacketChannel(player);
        if (channel == null) return;

        PacketInterceptorListener interceptor = new PacketInterceptorListener(this, player);
        channel.pipeline().addBefore("packet_handler", NAME, interceptor);
    }
}