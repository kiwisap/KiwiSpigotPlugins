package nl.itz_kiwisap_.spigot.nms.scoreboard;

import org.bukkit.ChatColor;

public interface KScoreboardTeam {

    KScoreboardTeam setColor(ChatColor color);

    Object getNMSInstance();
}