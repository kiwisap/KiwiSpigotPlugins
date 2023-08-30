package nl.itz_kiwisap_.spigot.nms.v1_19_R3.network.clientbound;

import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketEntityMetadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class KClientboundPacketEntityMetadata_v1_19_R3 implements KClientboundPacketEntityMetadata {

    private final ClientboundSetEntityDataPacket packet;

    private final int entityId;
    private final List<Entry<?>> entries;

    public KClientboundPacketEntityMetadata_v1_19_R3(ClientboundSetEntityDataPacket packet) {
        this.packet = packet;

        this.entityId = packet.id();

        List<Entry<?>> entries = new ArrayList<>();
        for (SynchedEntityData.DataValue<?> packedItem : packet.packedItems()) {
            int index = packedItem.id();
            int serializerId = EntityDataSerializers.getSerializedId(packedItem.serializer());

            entries.add(new Entry<>(index, serializerId, packedItem.value()));
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
        SynchedEntityData.DataValue<T> value = new SynchedEntityData.DataValue<>(index, serializer, entry.value());

        this.packet.packedItems().add(value);
    }

    @Override
    public void removeEntry(int index) {
        this.entries.removeIf(entry -> entry.index() == index);
        this.packet.packedItems().removeIf(value -> value.id() == index);
    }

    @Override
    public Object getNMSInstance() {
        return this.packet;
    }
}