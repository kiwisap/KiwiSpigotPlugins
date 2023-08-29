package nl.itz_kiwisap_.spigot.nms.v1_17_R1.scoreboard;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.PlayerTeam;
import nl.itz_kiwisap_.spigot.nms.v1_17_R1.KiwiNMS_v1_17_R1;
import nl.itz_kiwisap_.spigot.nms.scoreboard.KScoreboardTeam;
import org.bukkit.ChatColor;

public final class KScoreboardTeam_v1_17_R1 implements KScoreboardTeam {

    private final KiwiNMS_v1_17_R1 nms;
    private final PlayerTeam team;

    public KScoreboardTeam_v1_17_R1(KiwiNMS_v1_17_R1 nms, PlayerTeam team) {
        this.nms = nms;
        this.team = team;
    }

    @Override
    public KScoreboardTeam setPrefix(String prefix) {
        Component prefixComponent = this.nms.createChatBaseComponent(prefix);
        this.team.setPlayerPrefix(prefixComponent);
        return this;
    }

    @Override
    public KScoreboardTeam setSuffix(String suffix) {
        Component suffixComponent = this.nms.createChatBaseComponent(suffix);
        this.team.setPlayerSuffix(suffixComponent);
        return this;
    }

    @Override
    public KScoreboardTeam setPrefixAndSuffix(String prefix, String suffix) {
        this.setPrefix(prefix);
        this.setSuffix(suffix);
        return this;
    }

    @Override
    public KScoreboardTeam setColor(ChatColor color) {
        ChatFormatting formatting = ChatFormatting.getByCode(color.getChar());
        this.team.setColor(formatting);
        return this;
    }

    @Override
    public Object getNMSInstance() {
        return this.team;
    }
}