package nl.itz_kiwisap_.spigot.customrecipes;

import nl.itz_kiwisap_.spigot.common.KiwiSpigotLibraryGetter;
import nl.itz_kiwisap_.spigot.customrecipes.listener.KiwiCustomRecipesInteractListener;
import nl.itz_kiwisap_.spigot.customrecipes.menu.KiwiCustomRecipesDefaultItemProvider;
import nl.itz_kiwisap_.spigot.customrecipes.menu.KiwiCustomRecipesMenuProvider;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.RecipeHandler;
import nl.odalitadevelopments.menus.OdalitaMenus;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

final class KiwiCustomRecipesImpl implements KiwiCustomRecipes {

    private static final Map<JavaPlugin, KiwiCustomRecipesImpl> INSTANCES = new HashMap<>();

    public static KiwiCustomRecipes getInstance(JavaPlugin plugin) {
        return INSTANCES.get(plugin);
    }

    private final JavaPlugin javaPlugin;

    private final RecipeHandler recipeHandler;

    private final OdalitaMenus menuManager;

    KiwiCustomRecipesImpl(@NotNull JavaPlugin plugin) {
        KiwiSpigotLibraryGetter.get(plugin);

        this.javaPlugin = plugin;

        this.recipeHandler = new RecipeHandler(plugin);

        this.menuManager = OdalitaMenus.createInstance(plugin);

        this.menuManager.getProvidersContainer().setDefaultItemProvider(new KiwiCustomRecipesDefaultItemProvider());
        this.menuManager.registerProviderLoader(KiwiCustomRecipesMenuProvider.class, KiwiCustomRecipesMenuProvider.loader(this.recipeHandler));

        plugin.getServer().getPluginManager().registerEvents(new KiwiCustomRecipesInteractListener(this), plugin);

        INSTANCES.put(plugin, this);
    }

    @Override
    public @NotNull JavaPlugin getJavaPlugin() {
        return this.javaPlugin;
    }

    @Override
    public @NotNull RecipeHandler getRecipeHandler() {
        return this.recipeHandler;
    }

    @Override
    public @NotNull OdalitaMenus getMenuManager() {
        return this.menuManager;
    }
}