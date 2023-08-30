package nl.itz_kiwisap_.spigot.pergroupdrops.scoreboard;

import nl.itz_kiwisap_.spigot.nms.KiwiNMS;
import nl.itz_kiwisap_.spigot.nms.network.KiwiPacketWrapper;
import nl.itz_kiwisap_.spigot.nms.scoreboard.KScoreboardTeam;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PerGroupDropsScoreboardHandler implements Listener {

    private static final String TEAM_NAME = "KIWI_PER_GROUP_DROPS_%s";

    private final Map<ChatColor, KScoreboardTeam> teams = new HashMap<>();

    public PerGroupDropsScoreboardHandler() {
        for (ChatColor color : ChatColor.values()) {
            if (color.isColor()) {
                this.teams.put(color, KiwiNMS.getInstance().createScoreboardTeam(TEAM_NAME.formatted(color.name())).setColor(color));
            }
        }
    }

    public KScoreboardTeam getTeam(ChatColor color) {
        return this.teams.get(color);
    }

    public void addItemToTeam(Player player, KScoreboardTeam team, Item item) {
        KiwiNMS nms = KiwiNMS.getInstance();
        KiwiPacketWrapper packet = nms.createPacketScoreboardTeamEntityAdd(team, item.getUniqueId().toString());
        nms.sendPacket(player, packet);
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        KiwiNMS nms = KiwiNMS.getInstance();

        // Initialize all scoreboard teams for the player
        List<KiwiPacketWrapper> packets = new ArrayList<>();
        for (KScoreboardTeam team : this.teams.values()) {
            packets.add(nms.createPacketScoreboardTeamInitialize(team));
        }

        nms.sendPackets(event.getPlayer(), packets);
    }
}