package nl.itz_kiwisap_.spigot.nms.v1_16_R3.scoreboard;

import net.minecraft.server.v1_16_R3.EnumChatFormat;
import net.minecraft.server.v1_16_R3.ScoreboardTeam;
import nl.itz_kiwisap_.spigot.nms.scoreboard.KScoreboardTeam;
import org.bukkit.ChatColor;

public final class KScoreboardTeam_v1_16_R3 implements KScoreboardTeam {

    private final ScoreboardTeam team;

    public KScoreboardTeam_v1_16_R3(ScoreboardTeam team) {
        this.team = team;
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