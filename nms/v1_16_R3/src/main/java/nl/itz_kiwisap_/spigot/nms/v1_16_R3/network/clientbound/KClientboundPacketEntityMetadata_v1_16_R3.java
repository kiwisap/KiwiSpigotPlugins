package nl.itz_kiwisap_.spigot.nms.v1_16_R3.network.clientbound;

import net.minecraft.server.v1_16_R3.*;
import nl.itz_kiwisap_.spigot.common.utils.JavaReflections;
import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketEntityMetadata;
import nl.itz_kiwisap_.spigot.nms.v1_16_R3.ObfuscatedNames_v1_16_R3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class KClientboundPacketEntityMetadata_v1_16_R3 implements KClientboundPacketEntityMetadata {

    private static final JavaReflections.FieldAccessor<Integer> ENTITY_ID_FIELD;
    private static final JavaReflections.FieldAccessor<List<DataWatcher.Item<?>>> METADATA_FIELD;

    static {
        ENTITY_ID_FIELD = JavaReflections.getField(PacketPlayOutEntityMetadata.class, int.class, ObfuscatedNames_v1_16_R3.METADATA_PACKET_ENTITY_ID_FIELD);
        METADATA_FIELD = JavaReflections.getField(PacketPlayOutEntityMetadata.class, List.class, ObfuscatedNames_v1_16_R3.METADATA_PACKET_METADATA_FIELD);
    }

    private final PacketPlayOutEntityMetadata packet;

    private final int entityId;
    private final List<Entry<?>> entries;

    public KClientboundPacketEntityMetadata_v1_16_R3(PacketPlayOutEntityMetadata packet) {
        this.packet = packet;

        this.entityId = ENTITY_ID_FIELD.get(packet);

        List<Entry<?>> entries = new ArrayList<>();
        List<DataWatcher.Item<?>> items = METADATA_FIELD.get(packet);
        if (items != null) {
            for (DataWatcher.Item<?> packedItem : items) {
                int index = packedItem.a().a();
                int serializerId = DataWatcherRegistry.b(packedItem.a().b());

                entries.add(new Entry<>(index, serializerId, packedItem.b()));
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
        DataWatcherSerializer<T> serializer = (DataWatcherSerializer<T>) DataWatcherRegistry.a(entry.serializerId());
        if (serializer == null) return;

        DataWatcherObject<T> accessor = serializer.a(index);
        DataWatcher.Item<T> value = new DataWatcher.Item<>(accessor, entry.value());

        List<DataWatcher.Item<?>> data = METADATA_FIELD.get(this.packet);
        if (data == null) {
            data = new ArrayList<>();
            data.add(value);
            METADATA_FIELD.set(this.packet, data);
        } else {
            data.add(value);
        }
    }

    @Override
    public void removeEntry(int index) {
        this.entries.removeIf(entry -> entry.index() == index);

        List<DataWatcher.Item<?>> items = METADATA_FIELD.get(this.packet);
        if (items != null) {
            items.removeIf(value -> value.a().a() == index);
        }
    }

    @Override
    public Object getNMSInstance() {
        return this.packet;
    }
}