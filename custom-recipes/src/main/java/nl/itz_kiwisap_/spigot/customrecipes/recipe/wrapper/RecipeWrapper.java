package nl.itz_kiwisap_.spigot.customrecipes.recipe.wrapper;

import nl.itz_kiwisap_.spigot.customrecipes.recipe.Recipe;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.ingredient.RecipeIngredient;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.types.ShapedRecipe;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.types.ShapelessRecipe;
import nl.itz_kiwisap_.spigot.customrecipes.utils.ArrayUtils;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class RecipeWrapper<T extends Recipe> {

    public static RecipeWrapper<?> createWrapper(Recipe recipe) {
        if (recipe instanceof ShapedRecipe shapedRecipe) {
            return new ShapedRecipeWrapper(shapedRecipe);
        }

        if (recipe instanceof ShapelessRecipe shapelessRecipe) {
            return new ShapelessRecipeWrapper(shapelessRecipe);
        }

        throw new IllegalArgumentException("Unknown recipe type: " + recipe.getClass().getName());
    }

    protected final T recipe;

    protected RecipeWrapper(T recipe) {
        this.recipe = recipe;
    }

    public final @NotNull T getRecipe() {
        return this.recipe;
    }

    public abstract boolean matches(ItemStack[][] matrix);

    public final int craft(ItemStack[][] matrix, boolean maximise) {
        ItemStack[][] clonedMatrix = ArrayUtils.cloneMatrix(matrix);
        int crafted = 0;

        if (maximise) {
            for (int i = 0; i < 64; i++) { // Can only craft 64 times
                if (!this.tryCraft(clonedMatrix)) {
                    break;
                }

                ArrayUtils.updateMatrix(matrix, clonedMatrix);
                crafted++;
            }
        } else {
            if (this.tryCraft(clonedMatrix)) {
                ArrayUtils.updateMatrix(matrix, clonedMatrix);
                crafted++;
            }
        }

        return crafted;
    }

    private boolean tryCraft(final ItemStack[][] matrix) {
        ArrayList<RecipeIngredient> clonedIngredients = new ArrayList<>(this.recipe.listIngredients());

        yLoop:
        for (int y = 0; y < 3; y++) {
            xLoop:
            for (int x = 0; x < 3; x++) {
                if (clonedIngredients.isEmpty()) {
                    break yLoop;
                }

                ItemStack itemStack = matrix[y][x];
                if (itemStack == null || itemStack.getType().isAir()) continue;

                for (int i = 0; i < clonedIngredients.size(); i++) {
                    RecipeIngredient ingredient = clonedIngredients.get(i);
                    if (ingredient.test(itemStack)) {
                        for (ItemStack choice : ingredient.getChoices()) {
                            if (itemStack.getType() == choice.getType() && itemStack.getAmount() >= choice.getAmount() && itemStack.isSimilar(choice)) {
                                itemStack.setAmount(itemStack.getAmount() - choice.getAmount());
                                if (itemStack.getAmount() <= 0) {
                                    matrix[y][x] = null;
                                }

                                break;
                            }
                        }

                        clonedIngredients.remove(i);
                        continue xLoop;
                    }
                }
            }
        }

        return clonedIngredients.isEmpty();
    }
}