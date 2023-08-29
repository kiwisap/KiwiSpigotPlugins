package nl.itz_kiwisap_.spigot.v1_16_R3.network.clientbound;

import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntity;
import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketSpawnEntity;
import nl.itz_kiwisap_.spigot.utils.JavaReflections;

public final class KClientboundPacketSpawnEntity_v1_16_R3 implements KClientboundPacketSpawnEntity {

    private static final JavaReflections.FieldAccessor<Integer> ENTITY_ID_FIELD;

    static {
        ENTITY_ID_FIELD = JavaReflections.getField(PacketPlayOutSpawnEntity.class, int.class, "a");
    }

    private final PacketPlayOutSpawnEntity packet;

    private final int entityId;

    public KClientboundPacketSpawnEntity_v1_16_R3(PacketPlayOutSpawnEntity packet) {
        this.packet = packet;

        this.entityId = ENTITY_ID_FIELD.get(packet);
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