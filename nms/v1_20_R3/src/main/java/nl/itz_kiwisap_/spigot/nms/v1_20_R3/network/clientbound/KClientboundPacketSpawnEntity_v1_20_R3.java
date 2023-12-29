package nl.itz_kiwisap_.spigot.nms.v1_20_R3.network.clientbound;

import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketSpawnEntity;

public final class KClientboundPacketSpawnEntity_v1_20_R3 implements KClientboundPacketSpawnEntity {

    private final ClientboundBundlePacket bundlePacket;
    private final ClientboundAddEntityPacket packet;

    private final int entityId;

    public KClientboundPacketSpawnEntity_v1_20_R3(ClientboundBundlePacket bundlePacket, ClientboundAddEntityPacket packet) {
        this.bundlePacket = bundlePacket;
        this.packet = packet;

        this.entityId = packet.getId();
    }

    @Override
    public int entityId() {
        return this.entityId;
    }

    @Override
    public Object getNMSInstance() {
        return this.packet;
    }

    @Override
    public Object getBundleNMSInstance() {
        return this.bundlePacket;
    }
}