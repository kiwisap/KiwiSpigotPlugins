package nl.itz_kiwisap_.spigot.pergroupdrops;

import lombok.Getter;
import nl.itz_kiwisap_.spigot.common.KiwiSpigotPlugin;
import nl.itz_kiwisap_.spigot.common.utils.JavaReflections;
import nl.itz_kiwisap_.spigot.pergroupdrops.api.KiwiPerGroupDrops;
import nl.itz_kiwisap_.spigot.pergroupdrops.api.KiwiPerGroupDropsAPI;
import nl.itz_kiwisap_.spigot.pergroupdrops.api.provider.GlowProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.api.provider.GroupProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.drop.ItemDropHandler;
import nl.itz_kiwisap_.spigot.pergroupdrops.scoreboard.PerGroupDropsScoreboardHandler;
import org.jetbrains.annotations.NotNull;

@Getter
public final class KiwiPerGroupDropsPlugin extends KiwiSpigotPlugin implements KiwiPerGroupDrops {

    private GroupProvider groupProvider;
    private GlowProvider glowProvider;

    private PerGroupDropsScoreboardHandler scoreboardHandler;

    @Override
    public void load() {
        JavaReflections.getField(KiwiPerGroupDropsAPI.class, KiwiPerGroupDrops.class, "INSTANCE").set(null, this);
    }

    @Override
    public void onEnable() {
        this.scoreboardHandler = new PerGroupDropsScoreboardHandler();

        super.getServer().getPluginManager().registerEvents(this.scoreboardHandler, this);
        super.getServer().getPluginManager().registerEvents(new ItemDropHandler(this), this);
    }

    @Override
    public void setGroupProvider(@NotNull GroupProvider provider) {
        this.groupProvider = provider;
    }

    @Override
    public void setGlowProvider(@NotNull GlowProvider provider) {
        this.glowProvider = provider;
    }
}