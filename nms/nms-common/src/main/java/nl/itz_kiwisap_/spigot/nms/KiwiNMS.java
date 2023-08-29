package nl.itz_kiwisap_.spigot.nms;

import io.netty.channel.Channel;
import nl.itz_kiwisap_.spigot.nms.network.KPacket;
import nl.itz_kiwisap_.spigot.nms.network.KiwiPacketWrapper;
import nl.itz_kiwisap_.spigot.nms.scoreboard.KScoreboardTeam;
import org.bukkit.entity.Player;

import java.util.List;

public interface KiwiNMS {

    static KiwiNMS getInstance() {
        return KiwiNMSInstance.INSTANCE;
    }

    void sendPacket(Player player, KiwiPacketWrapper packetWrapper);

    void sendPackets(Player player, List<KiwiPacketWrapper> packetWrappers);

    Channel getPacketChannel(Player player);

    Object createChatBaseComponent(String text);

    KScoreboardTeam createScoreboardTeam(String name);

    KPacket transformClientboundPacket(Object packetObject);

    KPacket transformServerboundPacket(Object packetObject);

    KiwiPacketWrapper createPacketScoreboardTeamInitialize(KScoreboardTeam team);

    KiwiPacketWrapper createPacketScoreboardTeamEntityAdd(KScoreboardTeam team, String entityName);
}