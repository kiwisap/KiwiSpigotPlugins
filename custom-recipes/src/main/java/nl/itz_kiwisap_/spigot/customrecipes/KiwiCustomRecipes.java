package nl.itz_kiwisap_.spigot.customrecipes;

import nl.itz_kiwisap_.spigot.customrecipes.plugin.KiwiCustomRecipesPlugin;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.RecipeHandler;
import nl.odalitadevelopments.menus.OdalitaMenus;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface KiwiCustomRecipes {

    static @NotNull KiwiCustomRecipes create(@NotNull JavaPlugin plugin) {
        return new KiwiCustomRecipesImpl(plugin);
    }

    static @Nullable KiwiCustomRecipes getInstance(@NotNull JavaPlugin plugin) {
        return KiwiCustomRecipesImpl.getInstance(plugin);
    }

    static @Nullable KiwiCustomRecipes getPluginInstance() {
        if (Bukkit.getPluginManager().isPluginEnabled("KiwiCustomRecipes")) {
            KiwiCustomRecipesPlugin plugin = JavaPlugin.getPlugin(KiwiCustomRecipesPlugin.class);
            return getInstance(plugin);
        }

        return null;
    }

    @NotNull JavaPlugin getJavaPlugin();

    @NotNull RecipeHandler getRecipeHandler();

    @NotNull OdalitaMenus getMenuManager();
}