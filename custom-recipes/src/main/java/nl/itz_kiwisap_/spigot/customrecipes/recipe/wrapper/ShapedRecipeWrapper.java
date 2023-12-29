package nl.itz_kiwisap_.spigot.customrecipes.recipe.wrapper;

import nl.itz_kiwisap_.spigot.customrecipes.recipe.ingredient.RecipeIngredient;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.types.ShapedRecipe;
import org.bukkit.inventory.ItemStack;

final class ShapedRecipeWrapper extends RecipeWrapper<ShapedRecipe> {

    private final RecipeIngredient[][] ingredients;

    ShapedRecipeWrapper(ShapedRecipe recipe) {
        super(recipe);
        this.ingredients = this.readPattern();
    }

    @Override
    public boolean matches(ItemStack[][] matrix) {
        return this.matches(matrix, false) || this.matches(matrix, true);
    }

    private boolean matches(ItemStack[][] matrix, boolean flipped) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                ItemStack itemStack = matrix[x][y];
                RecipeIngredient ingredient = this.ingredients[flipped ? 2 - y : y][flipped ? 2 - x : x];
                if (ingredient == null) {
                    if (itemStack != null && !itemStack.getType().isAir()) {
                        return false;
                    }

                    continue;
                }

                if (!ingredient.test(itemStack)) {
                    return false;
                }
            }
        }

        return true;
    }

    private RecipeIngredient[][] readPattern() {
        RecipeIngredient[][] ingredients = new RecipeIngredient[3][3];

        String[] shape = this.recipe.shape();
        for (int i = 0; i < ingredients.length; i++) {
            String row = shape.length <= i ? null : shape[i];
            if (row == null) continue;

            for (int j = 0; j < row.length(); j++) {
                char character = row.charAt(j);
                RecipeIngredient ingredient = this.recipe.ingredients().get(character);
                ingredients[j][i] = ingredient;
            }
        }

        return ingredients;
    }
}