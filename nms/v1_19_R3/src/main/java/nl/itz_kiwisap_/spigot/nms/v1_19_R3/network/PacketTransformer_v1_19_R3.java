package nl.itz_kiwisap_.spigot.nms.v1_19_R3.network;

import com.google.common.collect.Iterables;
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
    public Collection<KPacket> transformClientboundPacket(Object bundlePacketObject, Object packetObject) {
        if (packetObject instanceof ClientboundBundlePacket clientboundBundlePacket) {
            List<KPacket> packets = new ArrayList<>();

            for (Packet<ClientGamePacketListener> subPacket : clientboundBundlePacket.subPackets()) {
                Collection<KPacket> kPackets = this.transformClientboundPacket(clientboundBundlePacket, subPacket);
                if (kPackets != null && !kPackets.isEmpty()) {
                    packets.addAll(kPackets);
                }
            }

            return packets;
        }

        if (packetObject instanceof ClientboundAddEntityPacket clientboundAddEntityPacket) {
            return List.of(this.transformSpawnEntity(bundlePacketObject, clientboundAddEntityPacket));
        }

        if (packetObject instanceof ClientboundSetEntityDataPacket clientboundSetEntityDataPacket) {
            return List.of(this.transformEntityMetadata(bundlePacketObject, clientboundSetEntityDataPacket));
        }

        return null;
    }

    @Override
    public Collection<KPacket> transformServerboundPacket(Object packetObject) {
        return null;
    }

    @Override
    public boolean isBundlePacket(Object packetObject) {
        return packetObject instanceof ClientboundBundlePacket;
    }

    @Override
    public boolean isBundleEmpty(Object bundlePacket) {
        return bundlePacket instanceof ClientboundBundlePacket bundle && Iterables.isEmpty(bundle.subPackets());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addPacketToBundle(Object bundlePacket, Object packetObject) {
        if (bundlePacket instanceof ClientboundBundlePacket bundle && packetObject instanceof Packet<?> packet) {
            if (bundle.subPackets() instanceof Collection<Packet<ClientGamePacketListener>> collection) {
                collection.add((Packet<ClientGamePacketListener>) packet);
            }
        }
    }

    @Override
    public void removePacketFromBundle(Object bundlePacket, Object packetObject) {
        if (bundlePacket instanceof ClientboundBundlePacket bundle && packetObject instanceof Packet<?>) {
            Iterables.removeIf(bundle.subPackets(), subPacket -> subPacket.equals(packetObject));
        }
    }

    @Override
    public KClientboundPacketSpawnEntity transformSpawnEntity(Object bundlePacketObject, Object packetObject) {
        if (packetObject instanceof ClientboundAddEntityPacket packet) {
            ClientboundBundlePacket bundlePacket = bundlePacketObject instanceof ClientboundBundlePacket bundle ? bundle : null;
            return new KClientboundPacketSpawnEntity_v1_19_R3(bundlePacket, packet);
        }

        return null;
    }

    @Override
    public KClientboundPacketEntityMetadata transformEntityMetadata(Object bundlePacketObject, Object packetObject) {
        if (packetObject instanceof ClientboundSetEntityDataPacket packet) {
            ClientboundBundlePacket bundlePacket = bundlePacketObject instanceof ClientboundBundlePacket bundle ? bundle : null;
            return new KClientboundPacketEntityMetadata_v1_19_R3(bundlePacket, packet);
        }

        return null;
    }
}