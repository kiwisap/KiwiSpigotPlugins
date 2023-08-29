package nl.itz_kiwisap_.spigot.nms.scoreboard;

import org.bukkit.ChatColor;

public interface KScoreboardTeam {

    KScoreboardTeam setPrefix(String prefix);

    KScoreboardTeam setSuffix(String suffix);

    KScoreboardTeam setPrefixAndSuffix(String prefix, String suffix);

    KScoreboardTeam setColor(ChatColor color);

    Object getNMSInstance();
}