package nl.itz_kiwisap_.spigot.nms.v1_18_R2.network.clientbound;

import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketSpawnEntity;

public final class KClientboundPacketSpawnEntity_v1_18_R2 implements KClientboundPacketSpawnEntity {

    private final ClientboundAddEntityPacket packet;

    private final int entityId;

    public KClientboundPacketSpawnEntity_v1_18_R2(ClientboundAddEntityPacket packet) {
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
}