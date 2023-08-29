package nl.itz_kiwisap_.spigot.v1_17_R1.network;

import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import nl.itz_kiwisap_.spigot.nms.network.KPacket;
import nl.itz_kiwisap_.spigot.nms.network.PacketTransformer;
import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketSpawnEntity;
import nl.itz_kiwisap_.spigot.v1_17_R1.network.clientbound.KClientboundPacketSpawnEntity_v1_17_R1;

public final class PacketTransformer_v1_17_R1 implements PacketTransformer {

    @Override
    public KPacket transformClientboundPacket(Object packetObject) {
        if (packetObject instanceof ClientboundAddEntityPacket clientboundAddEntityPacket) {
            return this.transformSpawnEntity(clientboundAddEntityPacket);
        }

        return null;
    }

    @Override
    public KPacket transformServerboundPacket(Object packetObject) {
        return null;
    }

    @Override
    public KClientboundPacketSpawnEntity transformSpawnEntity(Object packetObject) {
        if (packetObject instanceof ClientboundAddEntityPacket packet) {
            return new KClientboundPacketSpawnEntity_v1_17_R1(packet);
        }

        return null;
    }
}