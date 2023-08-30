package nl.itz_kiwisap_.spigot.nms.network;

import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketEntityMetadata;
import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketSpawnEntity;

import java.util.Collection;

public interface PacketTransformer {

    Collection<KPacket> transformClientboundPacket(Object packetObject);

    Collection<KPacket> transformServerboundPacket(Object packetObject);

    KClientboundPacketSpawnEntity transformSpawnEntity(Object packetObject);

    KClientboundPacketEntityMetadata transformEntityMetadata(Object packetObject);
}