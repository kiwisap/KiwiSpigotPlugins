package nl.itz_kiwisap_.spigot.nms.v1_17_R1.network;

import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import nl.itz_kiwisap_.spigot.nms.network.KPacket;
import nl.itz_kiwisap_.spigot.nms.network.PacketTransformer;
import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketEntityMetadata;
import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketSpawnEntity;
import nl.itz_kiwisap_.spigot.nms.v1_17_R1.network.clientbound.KClientboundPacketEntityMetadata_v1_17_R1;
import nl.itz_kiwisap_.spigot.nms.v1_17_R1.network.clientbound.KClientboundPacketSpawnEntity_v1_17_R1;

import java.util.Collection;
import java.util.List;

public final class PacketTransformer_v1_17_R1 implements PacketTransformer {

    @Override
    public Collection<KPacket> transformClientboundPacket(Object bundlePacketObject, Object packetObject) {
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
        return false;
    }

    @Override
    public boolean isBundleEmpty(Object bundlePacket) {
        return false;
    }

    @Override
    public void addPacketToBundle(Object bundlePacket, Object packetObject) {
    }

    @Override
    public void removePacketFromBundle(Object bundlePacket, Object packetObject) {
    }

    @Override
    public KClientboundPacketSpawnEntity transformSpawnEntity(Object bundlePacketObject, Object packetObject) {
        if (packetObject instanceof ClientboundAddEntityPacket packet) {
            return new KClientboundPacketSpawnEntity_v1_17_R1(packet);
        }

        return null;
    }

    @Override
    public KClientboundPacketEntityMetadata transformEntityMetadata(Object bundlePacketObject, Object packetObject) {
        if (packetObject instanceof ClientboundSetEntityDataPacket packet) {
            return new KClientboundPacketEntityMetadata_v1_17_R1(packet);
        }

        return null;
    }
}