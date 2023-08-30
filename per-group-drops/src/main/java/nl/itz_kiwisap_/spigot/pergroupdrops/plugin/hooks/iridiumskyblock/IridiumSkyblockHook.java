package nl.itz_kiwisap_.spigot.pergroupdrops.plugin.hooks.iridiumskyblock;

import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import nl.itz_kiwisap_.spigot.pergroupdrops.plugin.hooks.PluginHook;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GlowProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GroupProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class IridiumSkyblockHook implements PluginHook {

    public static final String NAME = "IridiumSkyblock";

    @Override
    public boolean isEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("IridiumSkyblock");
    }

    @Override
    public @NotNull String getName() {
        return NAME;
    }

    @Override
    public @NotNull GroupProvider groupProvider() {
        return new IridiumSkyblockGroupProvider();
    }

    @Override
    public @Nullable GlowProvider glowProvider() {
        return null;
    }

    private static final class IridiumSkyblockGroupProvider implements GroupProvider {

        @Override
        public @Nullable String getGroup(@NotNull Player player) {
            Island island = IridiumSkyblockAPI.getInstance().getUser(player).getIsland().orElse(null);
            return island == null ? null : "iridium-" + island.getId();
        }
    }
}