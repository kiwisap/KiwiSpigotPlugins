package nl.itz_kiwisap_.spigot.nms.network;

import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketSpawnEntity;

public interface PacketTransformer {

    KPacket transformClientboundPacket(Object packetObject);

    KPacket transformServerboundPacket(Object packetObject);

    KClientboundPacketSpawnEntity transformSpawnEntity(Object packetObject);
}