package nl.itz_kiwisap_.spigot.nms.network.clientbound;

import nl.itz_kiwisap_.spigot.nms.network.KPacket;

public interface KClientboundPacketSpawnEntity extends KPacket {

    int entityId();
}