package nl.itz_kiwisap_.spigot.nms.v1_17_R1.network.clientbound;

import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import nl.itz_kiwisap_.spigot.common.utils.JavaReflections;
import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketEntityMetadata;
import nl.itz_kiwisap_.spigot.nms.v1_17_R1.ObfuscatedNames_v1_17_R1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class KClientboundPacketEntityMetadata_v1_17_R1 implements KClientboundPacketEntityMetadata {

    private static final JavaReflections.FieldAccessor<List<SynchedEntityData.DataItem<?>>> METADATA_FIELD;

    static {
        METADATA_FIELD = JavaReflections.getField(ClientboundSetEntityDataPacket.class, List.class, ObfuscatedNames_v1_17_R1.METADATA_PACKET_METADATA_FIELD);
    }

    private final ClientboundSetEntityDataPacket packet;

    private final int entityId;
    private final List<Entry<?>> entries;

    public KClientboundPacketEntityMetadata_v1_17_R1(ClientboundSetEntityDataPacket packet) {
        this.packet = packet;

        this.entityId = packet.getId();

        List<Entry<?>> entries = new ArrayList<>();
        if (packet.getUnpackedData() != null) {
            for (SynchedEntityData.DataItem<?> packedItem : packet.getUnpackedData()) {
                int index = packedItem.getAccessor().getId();
                int serializerId = EntityDataSerializers.getSerializedId(packedItem.getAccessor().getSerializer());

                entries.add(new Entry<>(index, serializerId, packedItem.getValue()));
            }
        }

        this.entries = entries;
    }

    @Override
    public int entityId() {
        return this.entityId;
    }

    @Override
    public List<Entry<?>> entries() {
        return this.entries;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @Nullable Entry<T> getEntry(int index) {
        for (Entry<?> entry : this.entries) {
            if (entry.index() == index) {
                return (Entry<T>) entry;
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void addEntry(@NotNull Entry<T> entry) {
        this.entries.add(entry);

        int index = entry.index();
        EntityDataSerializer<T> serializer = (EntityDataSerializer<T>) EntityDataSerializers.getSerializer(entry.serializerId());
        if (serializer == null) return;

        EntityDataAccessor<T> accessor = serializer.createAccessor(index);
        SynchedEntityData.DataItem<T> value = new SynchedEntityData.DataItem<>(accessor, entry.value());

        List<SynchedEntityData.DataItem<?>> data = this.packet.getUnpackedData();
        if (data == null) {
            METADATA_FIELD.set(this.packet, new ArrayList<>());
        }

        this.packet.getUnpackedData().add(value);
    }

    @Override
    public void removeEntry(int index) {
        this.entries.removeIf(entry -> entry.index() == index);

        if (this.packet.getUnpackedData() != null) {
            this.packet.getUnpackedData().removeIf(value -> value.getAccessor().getId() == index);
        }
    }

    @Override
    public Object getNMSInstance() {
        return this.packet;
    }
}