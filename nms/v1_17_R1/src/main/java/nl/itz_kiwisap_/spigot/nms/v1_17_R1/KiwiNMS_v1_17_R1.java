package nl.itz_kiwisap_.spigot.nms.v1_17_R1;

import io.netty.channel.Channel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import nl.itz_kiwisap_.spigot.nms.KiwiNMS;
import nl.itz_kiwisap_.spigot.nms.network.KPacket;
import nl.itz_kiwisap_.spigot.nms.network.KiwiPacketWrapper;
import nl.itz_kiwisap_.spigot.nms.scoreboard.KScoreboardTeam;
import nl.itz_kiwisap_.spigot.nms.v1_17_R1.network.PacketTransformer_v1_17_R1;
import nl.itz_kiwisap_.spigot.nms.v1_17_R1.scoreboard.KScoreboardTeam_v1_17_R1;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public final class KiwiNMS_v1_17_R1 implements KiwiNMS {

    private final PacketTransformer_v1_17_R1 packetTransformer;
    private final Scoreboard scoreboard;

    public KiwiNMS_v1_17_R1() {
        this.packetTransformer = new PacketTransformer_v1_17_R1();
        this.scoreboard = new Scoreboard();
    }

    @Override
    public void sendPacket(Player player, KiwiPacketWrapper packetWrapper) {
        Object packetObject = packetWrapper.packet();
        if (!(packetObject instanceof Packet<?> packet)) {
            throw new IllegalArgumentException("Packet inside packet wrapper is not an instance of a minecraft packet!");
        }

        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        serverPlayer.connection.send(packet);
    }

    @Override
    public void sendPackets(Player player, List<KiwiPacketWrapper> packetWrappers) {
        for (KiwiPacketWrapper packetWrapper : packetWrappers) {
            this.sendPacket(player, packetWrapper);
        }
    }

    @Override
    public Channel getPacketChannel(Player player) {
        return ((CraftPlayer) player).getHandle().connection.connection.channel;
    }

    @Override
    public Component createChatBaseComponent(String text) {
        return Component.Serializer.fromJson("{\"text\":\"" + text + "\"}");
    }

    @Override
    public KScoreboardTeam createScoreboardTeam(String name) {
        PlayerTeam team = this.scoreboard.addPlayerTeam(name);
        return new KScoreboardTeam_v1_17_R1(this, team);
    }

    @Override
    public Collection<KPacket> transformClientboundPacket(Object packetObject) {
        return this.packetTransformer.transformClientboundPacket(packetObject);
    }

    @Override
    public Collection<KPacket> transformServerboundPacket(Object packetObject) {
        return this.packetTransformer.transformServerboundPacket(packetObject);
    }

    @Override
    public KiwiPacketWrapper createPacketScoreboardTeamInitialize(KScoreboardTeam team) {
        ClientboundSetPlayerTeamPacket packet = ClientboundSetPlayerTeamPacket.createAddOrModifyPacket((PlayerTeam) team.getNMSInstance(), true);
        return new KiwiPacketWrapper(packet);
    }

    @Override
    public KiwiPacketWrapper createPacketScoreboardTeamEntityAdd(KScoreboardTeam team, String entityName) {
        ClientboundSetPlayerTeamPacket packet = ClientboundSetPlayerTeamPacket.createPlayerPacket((PlayerTeam) team.getNMSInstance(), entityName, ClientboundSetPlayerTeamPacket.Action.ADD);
        return new KiwiPacketWrapper(packet);
    }
}