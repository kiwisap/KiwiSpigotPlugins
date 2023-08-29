package nl.itz_kiwisap_.spigot.network.interceptor;

import nl.itz_kiwisap_.spigot.nms.network.KPacket;
import org.bukkit.entity.Player;

public interface PacketInterceptor<T extends KPacket> {

    boolean intercept(Player player, T packet);
}