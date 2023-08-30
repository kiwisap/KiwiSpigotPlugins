package nl.itz_kiwisap_.spigot.nms.v1_18_R2.network;

import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import nl.itz_kiwisap_.spigot.nms.network.KPacket;
import nl.itz_kiwisap_.spigot.nms.network.PacketTransformer;
import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketSpawnEntity;
import nl.itz_kiwisap_.spigot.nms.v1_18_R2.network.clientbound.KClientboundPacketSpawnEntity_v1_18_R2;

import java.util.Collection;
import java.util.List;

public final class PacketTransformer_v1_18_R2 implements PacketTransformer {

    @Override
    public Collection<KPacket> transformClientboundPacket(Object packetObject) {
        if (packetObject instanceof ClientboundAddEntityPacket clientboundAddEntityPacket) {
            return List.of(this.transformSpawnEntity(clientboundAddEntityPacket));
        }

        return null;
    }

    @Override
    public Collection<KPacket> transformServerboundPacket(Object packetObject) {
        return null;
    }

    @Override
    public KClientboundPacketSpawnEntity transformSpawnEntity(Object packetObject) {
        if (packetObject instanceof ClientboundAddEntityPacket packet) {
            return new KClientboundPacketSpawnEntity_v1_18_R2(packet);
        }

        return null;
    }
}