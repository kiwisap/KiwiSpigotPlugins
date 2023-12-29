package nl.itz_kiwisap_.spigot.customrecipes.plugin;

import nl.itz_kiwisap_.spigot.customrecipes.KiwiCustomRecipes;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.RecipeHandler;
import nl.odalitadevelopments.menus.OdalitaMenus;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class KiwiCustomRecipesPlugin extends JavaPlugin implements KiwiCustomRecipes {

    private KiwiCustomRecipes instance;

    @Override
    public void onEnable() {
        this.instance = KiwiCustomRecipes.create(this);
    }

    @Override
    public @NotNull JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public @NotNull RecipeHandler getRecipeHandler() {
        return this.instance.getRecipeHandler();
    }

    @Override
    public @NotNull OdalitaMenus getMenuManager() {
        return this.instance.getMenuManager();
    }
}