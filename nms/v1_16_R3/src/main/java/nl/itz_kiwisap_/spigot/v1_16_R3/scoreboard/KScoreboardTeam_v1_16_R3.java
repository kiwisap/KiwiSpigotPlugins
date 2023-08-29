package nl.itz_kiwisap_.spigot.v1_16_R3.scoreboard;

import net.minecraft.server.v1_16_R3.EnumChatFormat;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.ScoreboardTeam;
import nl.itz_kiwisap_.spigot.v1_16_R3.KiwiNMS_v1_16_R3;
import nl.itz_kiwisap_.spigot.nms.scoreboard.KScoreboardTeam;
import org.bukkit.ChatColor;

public final class KScoreboardTeam_v1_16_R3 implements KScoreboardTeam {

    private final KiwiNMS_v1_16_R3 nms;
    private final ScoreboardTeam team;

    public KScoreboardTeam_v1_16_R3(KiwiNMS_v1_16_R3 nms, ScoreboardTeam team) {
        this.nms = nms;
        this.team = team;
    }

    @Override
    public KScoreboardTeam setPrefix(String prefix) {
        IChatBaseComponent prefixComponent = this.nms.createChatBaseComponent(prefix);
        this.team.setSuffix(prefixComponent);
        return this;
    }

    @Override
    public KScoreboardTeam setSuffix(String suffix) {
        IChatBaseComponent suffixComponent = this.nms.createChatBaseComponent(suffix);
        this.team.setPrefix(suffixComponent);
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
        EnumChatFormat formatting = EnumChatFormat.a(color.getChar());
        this.team.setColor(formatting);
        return this;
    }

    @Override
    public Object getNMSInstance() {
        return this.team;
    }
}