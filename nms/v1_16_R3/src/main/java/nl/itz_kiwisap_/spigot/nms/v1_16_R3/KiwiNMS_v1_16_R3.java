package nl.itz_kiwisap_.spigot.nms.v1_16_R3;

import io.netty.channel.Channel;
import net.minecraft.server.v1_16_R3.*;
import nl.itz_kiwisap_.spigot.nms.KiwiNMS;
import nl.itz_kiwisap_.spigot.nms.network.KPacket;
import nl.itz_kiwisap_.spigot.nms.network.KiwiPacketWrapper;
import nl.itz_kiwisap_.spigot.nms.scoreboard.KScoreboardTeam;
import nl.itz_kiwisap_.spigot.nms.v1_16_R3.scoreboard.KScoreboardTeam_v1_16_R3;
import nl.itz_kiwisap_.spigot.nms.v1_16_R3.network.PacketTransformer_v1_16_R3;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public final class KiwiNMS_v1_16_R3 implements KiwiNMS {

    private final PacketTransformer_v1_16_R3 packetTransformer;
    private final Scoreboard scoreboard;

    public KiwiNMS_v1_16_R3() {
        this.packetTransformer = new PacketTransformer_v1_16_R3();
        this.scoreboard = new Scoreboard();
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
    public IChatBaseComponent createChatBaseComponent(String text) {
        return IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}");
    }

    @Override
    public KScoreboardTeam createScoreboardTeam(String name) {
        ScoreboardTeam team = this.scoreboard.createTeam(name);
        return new KScoreboardTeam_v1_16_R3(this, team);
    }

    @Override
    public KPacket transformClientboundPacket(Object packetObject) {
        return this.packetTransformer.transformClientboundPacket(packetObject);
    }

    @Override
    public KPacket transformServerboundPacket(Object packetObject) {
        return this.packetTransformer.transformServerboundPacket(packetObject);
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
}