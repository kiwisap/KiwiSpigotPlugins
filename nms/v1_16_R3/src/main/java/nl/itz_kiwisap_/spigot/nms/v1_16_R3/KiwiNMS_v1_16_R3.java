package nl.itz_kiwisap_.spigot.nms.v1_16_R3;

import io.netty.channel.Channel;
import net.minecraft.server.v1_16_R3.*;
import nl.itz_kiwisap_.spigot.common.utils.JavaReflections;
import nl.itz_kiwisap_.spigot.nms.KiwiNMS;
import nl.itz_kiwisap_.spigot.nms.network.KPacket;
import nl.itz_kiwisap_.spigot.nms.network.KiwiPacketWrapper;
import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketEntityMetadata;
import nl.itz_kiwisap_.spigot.nms.scoreboard.KScoreboardTeam;
import nl.itz_kiwisap_.spigot.nms.v1_16_R3.network.PacketTransformer_v1_16_R3;
import nl.itz_kiwisap_.spigot.nms.v1_16_R3.scoreboard.KScoreboardTeam_v1_16_R3;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class KiwiNMS_v1_16_R3 implements KiwiNMS {

    private static final JavaReflections.FieldAccessor<Integer> METADATA_PACKET_ENTITY_ID_FIELD;
    private static final JavaReflections.FieldAccessor<List<DataWatcher.Item<?>>> METADATA_PACKET_METADATA_FIELD;

    static {
        METADATA_PACKET_ENTITY_ID_FIELD = JavaReflections.getField(PacketPlayOutEntityMetadata.class, int.class, ObfuscatedNames_v1_16_R3.METADATA_PACKET_ENTITY_ID_FIELD);
        METADATA_PACKET_METADATA_FIELD = JavaReflections.getField(PacketPlayOutEntityMetadata.class, List.class, ObfuscatedNames_v1_16_R3.METADATA_PACKET_METADATA_FIELD);
    }

    private static final int ENTITY_FLAGS_INDEX = 0;

    private final PacketTransformer_v1_16_R3 packetTransformer;
    private final Scoreboard scoreboard;

    public KiwiNMS_v1_16_R3() {
        this.packetTransformer = new PacketTransformer_v1_16_R3();
        this.scoreboard = new Scoreboard();
    }

    @Override
    public void runSync(Runnable runnable) {
        MinecraftServer.getServer().executeSync(runnable);
    }

    @Override
    public void sendPacket(Player player, KiwiPacketWrapper packetWrapper) {
        Object packetObject = packetWrapper.packet();
        if (!(packetObject instanceof Packet<?> packet)) {
            throw new IllegalArgumentException("Packet inside packet wrapper is not an instance of a minecraft packet!");
        }

        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        entityPlayer.playerConnection.sendPacket(packet);
    }

    @Override
    public void sendPackets(Player player, List<KiwiPacketWrapper> packetWrappers) {
        for (KiwiPacketWrapper packetWrapper : packetWrappers) {
            this.sendPacket(player, packetWrapper);
        }
    }

    @Override
    public Channel getPacketChannel(Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
    }

    @Override
    public org.bukkit.entity.Entity getEntityById(World world, int entityId) {
        Entity nmsEntity = ((CraftWorld) world).getHandle().getEntity(entityId);
        return nmsEntity == null ? null : nmsEntity.getBukkitEntity();
    }

    @Override
    public KScoreboardTeam createScoreboardTeam(String name) {
        ScoreboardTeam team = this.scoreboard.createTeam(name);
        return new KScoreboardTeam_v1_16_R3(team);
    }

    @Override
    public Collection<KPacket> transformClientboundPacket(Object packetObject) {
        return this.packetTransformer.transformClientboundPacket(null, packetObject);
    }

    @Override
    public Collection<KPacket> transformServerboundPacket(Object packetObject) {
        return this.packetTransformer.transformServerboundPacket(packetObject);
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
    public KiwiPacketWrapper createPacketEntityMetadata(int entityId, List<KClientboundPacketEntityMetadata.Entry<?>> entries) {
        List<DataWatcher.Item<?>> items = new ArrayList<>();
        for (KClientboundPacketEntityMetadata.Entry<?> entry : entries) {
            DataWatcher.Item<?> dataValue = this.createDataValue(entry);
            if (dataValue != null) {
                items.add(dataValue);
            }
        }

        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata();
        METADATA_PACKET_ENTITY_ID_FIELD.set(packet, entityId);
        METADATA_PACKET_METADATA_FIELD.set(packet, items);
        return new KiwiPacketWrapper(packet);
    }

    @Override
    public KiwiPacketWrapper createPacketScoreboardTeamInitialize(KScoreboardTeam team) {
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam((ScoreboardTeam) team.getNMSInstance(), 0);
        return new KiwiPacketWrapper(packet);
    }

    @Override
    public KiwiPacketWrapper createPacketScoreboardTeamEntityAdd(KScoreboardTeam team, String entityName) {
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam((ScoreboardTeam) team.getNMSInstance(), List.of(entityName), 3);
        return new KiwiPacketWrapper(packet);
    }

    @SuppressWarnings("unchecked")
    private <T> DataWatcher.Item<T> createDataValue(KClientboundPacketEntityMetadata.Entry<T> entry) {
        DataWatcherSerializer<T> serializer = (DataWatcherSerializer<T>) DataWatcherRegistry.a(entry.serializerId());
        if (serializer == null) return null;

        DataWatcherObject<T> accessor = serializer.a(entry.index());
        return new DataWatcher.Item<>(accessor, entry.value());
    }
}