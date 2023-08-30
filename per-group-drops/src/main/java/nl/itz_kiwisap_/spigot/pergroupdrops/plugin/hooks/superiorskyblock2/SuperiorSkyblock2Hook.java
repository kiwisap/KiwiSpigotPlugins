package nl.itz_kiwisap_.spigot.pergroupdrops.plugin.hooks.superiorskyblock2;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import nl.itz_kiwisap_.spigot.pergroupdrops.plugin.hooks.PluginHook;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GlowProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GroupProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class SuperiorSkyblock2Hook implements PluginHook {

    public static final String NAME = "SuperiorSkyblock2";

    @Override
    public boolean isEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("SuperiorSkyblock2");
    }

    @Override
    public @NotNull String getName() {
        return NAME;
    }

    @Override
    public @NotNull GroupProvider groupProvider() {
        return new SuperiorSkyblockGroupProvider();
    }

    @Override
    public @Nullable GlowProvider glowProvider() {
        return null;
    }

    private static final class SuperiorSkyblockGroupProvider implements GroupProvider {

        @Override
        public @Nullable String getGroup(@NotNull Player player) {
            Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();
            return island == null ? null : island.getUniqueId().toString();
        }
    }
}