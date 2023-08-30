package nl.itz_kiwisap_.spigot.nms.v1_19_R3.network;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import nl.itz_kiwisap_.spigot.nms.network.KPacket;
import nl.itz_kiwisap_.spigot.nms.network.PacketTransformer;
import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketEntityMetadata;
import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketSpawnEntity;
import nl.itz_kiwisap_.spigot.nms.v1_19_R3.network.clientbound.KClientboundPacketEntityMetadata_v1_19_R3;
import nl.itz_kiwisap_.spigot.nms.v1_19_R3.network.clientbound.KClientboundPacketSpawnEntity_v1_19_R3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class PacketTransformer_v1_19_R3 implements PacketTransformer {

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

            return packets;
        }

        if (packetObject instanceof ClientboundAddEntityPacket clientboundAddEntityPacket) {
            return List.of(this.transformSpawnEntity(clientboundAddEntityPacket));
        }

        if (packetObject instanceof ClientboundSetEntityDataPacket clientboundSetEntityDataPacket) {
            return List.of(this.transformEntityMetadata(clientboundSetEntityDataPacket));
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
            return new KClientboundPacketSpawnEntity_v1_19_R3(packet);
        }

        return null;
    }

    @Override
    public KClientboundPacketEntityMetadata transformEntityMetadata(Object packetObject) {
        if (packetObject instanceof ClientboundSetEntityDataPacket packet) {
            return new KClientboundPacketEntityMetadata_v1_19_R3(packet);
        }

        return null;
    }
}