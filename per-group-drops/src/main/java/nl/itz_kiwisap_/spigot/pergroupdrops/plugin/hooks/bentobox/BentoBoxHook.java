package nl.itz_kiwisap_.spigot.pergroupdrops.plugin.hooks.bentobox;

import nl.itz_kiwisap_.spigot.pergroupdrops.plugin.hooks.PluginHook;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GlowProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GroupProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;

public final class BentoBoxHook implements PluginHook {

    public static final String NAME = "BentoBox";

    @Override
    public boolean isEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("BentoBox");
    }

    @Override
    public @NotNull String getName() {
        return NAME;
    }

    @Override
    public @NotNull GroupProvider groupProvider() {
        return new BentoBoxGroupProvider();
    }

    @Override
    public @Nullable GlowProvider glowProvider() {
        return null;
    }

    private static final class BentoBoxGroupProvider implements GroupProvider {

        @Override
        public @Nullable String getGroup(@NotNull Player player) {
            Island island = BentoBox.getInstance().getIslands().getIsland(player.getWorld(), User.getInstance(player));
            return island == null ? null : island.getUniqueId();
        }
    }
}