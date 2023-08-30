package nl.itz_kiwisap_.spigot.nms.network.clientbound;

import nl.itz_kiwisap_.spigot.nms.network.KPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface KClientboundPacketEntityMetadata extends KPacket {

    int entityId();

    List<Entry<?>> entries();

    <T> @Nullable Entry<T> getEntry(int index);

    <T> void addEntry(@NotNull Entry<T> entry);

    void removeEntry(int index);

    record Entry<T>(int index, int serializerId, T value) {
    }
}