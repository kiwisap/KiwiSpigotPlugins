package nl.itz_kiwisap_.spigot.customrecipes.recipe.wrapper;

import nl.itz_kiwisap_.spigot.customrecipes.recipe.ingredient.RecipeIngredient;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.types.ShapelessRecipe;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

final class ShapelessRecipeWrapper extends RecipeWrapper<ShapelessRecipe> {

    ShapelessRecipeWrapper(ShapelessRecipe recipe) {
        super(recipe);
    }

    @Override
    public boolean matches(ItemStack[][] matrix) {
        List<RecipeIngredient> clonedIngredients = new ArrayList<>(this.recipe.ingredients());

        int matched = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                ItemStack item = matrix[y][x];
                if (item == null || item.getType().isAir()) continue;

                for (int i = 0; i < clonedIngredients.size(); i++) {
                    RecipeIngredient ingredient = clonedIngredients.get(i);
                    if (ingredient.test(item)) {
                        clonedIngredients.remove(i);
                        matched++;
                        break;
                    }
                }
            }
        }

        return matched == this.recipe.ingredients().size();
    }
}