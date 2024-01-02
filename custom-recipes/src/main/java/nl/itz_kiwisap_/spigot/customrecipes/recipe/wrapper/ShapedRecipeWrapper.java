package nl.itz_kiwisap_.spigot.customrecipes.recipe.wrapper;

import nl.itz_kiwisap_.spigot.customrecipes.recipe.ingredient.RecipeIngredient;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.types.ShapedRecipe;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

final class ShapedRecipeWrapper extends RecipeWrapper<ShapedRecipe> {

    private final RecipeIngredient[][] ingredients;
    private int height;
    private int width;

    ShapedRecipeWrapper(ShapedRecipe recipe) {
        super(recipe);
        this.ingredients = this.readPattern();
    }

    @Override
    public boolean matches(ItemStack[][] matrix) {
        for (int y = 0; y <= 3 - this.height; y++) {
            for (int x = 0; x <= 3 - this.width; x++) {
                if (this.matches(matrix, y, x, false)) {
                    return true;
                }

                if (this.matches(matrix, y, x, true)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean matches(ItemStack[][] matrix, int offsetY, int offsetX, boolean flipped) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                ItemStack itemStack = matrix[y][x];

                final int finalY = y - offsetY;
                final int finalX = x - offsetX;

                RecipeIngredient ingredient = this.getRecipeIngredient(flipped, finalY, finalX);
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

    private @Nullable RecipeIngredient getRecipeIngredient(boolean flipped, int finalY, int finalX) {
        RecipeIngredient ingredient = null;
        if (finalY >= 0 && finalY < this.height && finalX >= 0 && finalX < this.width) {
            int indexX = finalX;
            int indexY = finalY;

            if (flipped) {
                int index = this.width - finalX - 1 + finalY * this.width;
                indexX = index % this.width;
                indexY = index / this.width;
            }

            ingredient = this.ingredients[indexY][indexX];
        }
        return ingredient;
    }

    private RecipeIngredient[][] readPattern() {
        String[] shape = this.recipe.shape();
        this.height = shape.length;
        this.width = shape[0] == null ? 0 : shape[0].length();

        RecipeIngredient[][] ingredients = new RecipeIngredient[this.height][this.width];

        for (int y = 0; y < shape.length; y++) {
            String row = shape[y];
            if (row == null) continue;

            for (int x = 0; x < row.length(); x++) {
                char character = row.charAt(x);
                RecipeIngredient ingredient = this.recipe.ingredients().get(character);
                ingredients[y][x] = ingredient;
            }
        }

        return ingredients;
    }
}