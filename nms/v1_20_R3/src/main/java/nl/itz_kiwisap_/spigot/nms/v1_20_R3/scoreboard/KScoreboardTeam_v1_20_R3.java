package nl.itz_kiwisap_.spigot.nms.v1_20_R3.scoreboard;

import net.minecraft.ChatFormatting;
import net.minecraft.world.scores.PlayerTeam;
import nl.itz_kiwisap_.spigot.nms.scoreboard.KScoreboardTeam;
import org.bukkit.ChatColor;

public final class KScoreboardTeam_v1_20_R3 implements KScoreboardTeam {

    private final PlayerTeam team;

    public KScoreboardTeam_v1_20_R3(PlayerTeam team) {
        this.team = team;
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