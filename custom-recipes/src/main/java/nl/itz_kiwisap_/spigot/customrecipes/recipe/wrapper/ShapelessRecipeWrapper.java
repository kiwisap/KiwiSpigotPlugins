package nl.itz_kiwisap_.spigot.customrecipes.recipe.wrapper;

import nl.itz_kiwisap_.spigot.customrecipes.recipe.types.ShapelessRecipe;
import org.bukkit.inventory.ItemStack;

final class ShapelessRecipeWrapper extends RecipeWrapper<ShapelessRecipe> {

    ShapelessRecipeWrapper(ShapelessRecipe recipe) {
        super(recipe);
    }

    @Override
    public boolean matches(ItemStack[][] matrix) {
        return false;
    }
}