package nl.itz_kiwisap_.spigot.nms.v1_20_R1.network;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import nl.itz_kiwisap_.spigot.nms.network.KPacket;
import nl.itz_kiwisap_.spigot.nms.network.PacketTransformer;
import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketSpawnEntity;
import nl.itz_kiwisap_.spigot.nms.v1_20_R1.network.clientbound.KClientboundPacketSpawnEntity_v1_20_R1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class PacketTransformer_v1_20_R1 implements PacketTransformer {

    @Override
    public Collection<KPacket> transformClientboundPacket(Object packetObject) {
        if (packetObject instanceof ClientboundBundlePacket clientboundBundlePacket) {
            List<KPacket> packets = new ArrayList<>();

            for (Packet<ClientGamePacketListener> subPacket : clientboundBundlePacket.subPackets()) {
                Collection<KPacket> kPackets = this.transformClientboundPacket(subPacket);
                if (kPackets != null && !kPackets.isEmpty()) {
                    packets.addAll(kPackets);
                }
            }

            return (packets.isEmpty()) ? null : packets;
        }

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
            return new KClientboundPacketSpawnEntity_v1_20_R1(packet);
        }

        return null;
    }
}