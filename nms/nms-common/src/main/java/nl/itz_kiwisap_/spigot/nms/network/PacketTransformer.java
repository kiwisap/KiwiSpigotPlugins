package nl.itz_kiwisap_.spigot.nms.network;

import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketEntityMetadata;
import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketSpawnEntity;

import java.util.Collection;

public interface PacketTransformer {

    Collection<KPacket> transformClientboundPacket(Object bundlePacketObject, Object packetObject);

    Collection<KPacket> transformServerboundPacket(Object packetObject);

    boolean isBundlePacket(Object packetObject);

    boolean isBundleEmpty(Object bundlePacket);

    void addPacketToBundle(Object bundlePacket, Object packetObject);

    void removePacketFromBundle(Object bundlePacket, Object packetObject);

    KClientboundPacketSpawnEntity transformSpawnEntity(Object bundlePacketObject, Object packetObject);

    KClientboundPacketEntityMetadata transformEntityMetadata(Object bundlePacketObject, Object packetObject);
}