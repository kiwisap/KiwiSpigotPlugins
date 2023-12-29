package nl.itz_kiwisap_.spigot.customrecipes.recipe.wrapper;

import nl.itz_kiwisap_.spigot.customrecipes.recipe.Recipe;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.types.ShapedRecipe;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.types.ShapelessRecipe;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

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
}