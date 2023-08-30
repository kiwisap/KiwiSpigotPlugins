package nl.itz_kiwisap_.spigot.pergroupdrops.provider.test;

import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GroupProvider;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestGroupProvider implements GroupProvider {
    @Override
    public @Nullable String getGroup(@NotNull Player player) {
        return player.getName();
    }
}