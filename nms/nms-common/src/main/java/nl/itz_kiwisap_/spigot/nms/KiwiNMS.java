package nl.itz_kiwisap_.spigot.nms;

import io.netty.channel.Channel;
import nl.itz_kiwisap_.spigot.nms.network.KPacket;
import nl.itz_kiwisap_.spigot.nms.network.KiwiPacketWrapper;
import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketEntityMetadata;
import nl.itz_kiwisap_.spigot.nms.scoreboard.KScoreboardTeam;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public interface KiwiNMS {

    static KiwiNMS getInstance() {
        return KiwiNMSInstance.INSTANCE;
    }

    void runSync(Runnable runnable);

    void sendPacket(Player player, KiwiPacketWrapper packetWrapper);

    void sendPackets(Player player, List<KiwiPacketWrapper> packetWrappers);

    Channel getPacketChannel(Player player);

    Object createChatBaseComponent(String text);

    Entity getEntityById(World world, int entityId);

    void markEntityFlagsMetadataDirty(Entity entity);

    KScoreboardTeam createScoreboardTeam(String name);

    Collection<KPacket> transformClientboundPacket(Object packetObject);

    Collection<KPacket> transformServerboundPacket(Object packetObject);

    boolean isBundlePacket(Object packetObject);

    boolean isBundleEmpty(Object bundlePacket);

    void addPacketToBundle(Object bundlePacket, Object packetObject);

    void removePacketFromBundle(Object bundlePacket, Object packetObject);

    KiwiPacketWrapper createPacketEntityMetadata(int entityId, List<KClientboundPacketEntityMetadata.Entry<?>> entries);

    KiwiPacketWrapper createPacketScoreboardTeamInitialize(KScoreboardTeam team);

    KiwiPacketWrapper createPacketScoreboardTeamEntityAdd(KScoreboardTeam team, String entityName);
}