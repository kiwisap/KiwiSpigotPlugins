package nl.itz_kiwisap_.spigot.customrecipes.menu;

import nl.itz_kiwisap_.spigot.customrecipes.recipe.RecipeHandler;
import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.menu.providers.MenuProvider;
import nl.odalitadevelopments.menus.menu.providers.MenuProviderLoader;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface KiwiCustomRecipesMenuProvider extends MenuProvider {

    static @NotNull MenuProviderLoader<KiwiCustomRecipesMenuProvider> loader(@NotNull RecipeHandler recipeHandler) {
        return (customRecipesMenuProvider, player, menuContents) -> customRecipesMenuProvider.onLoad(player, menuContents, recipeHandler);
    }

    void onLoad(@NotNull Player player, @NotNull MenuContents contents, @NotNull RecipeHandler handler);
}