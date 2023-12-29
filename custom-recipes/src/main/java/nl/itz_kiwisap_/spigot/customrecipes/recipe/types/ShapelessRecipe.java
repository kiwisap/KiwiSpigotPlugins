package nl.itz_kiwisap_.spigot.customrecipes.recipe.types;

import nl.itz_kiwisap_.spigot.customrecipes.recipe.Recipe;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.ingredient.ExactRecipeIngredient;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.ingredient.MaterialRecipeIngredient;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.ingredient.RecipeIngredient;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public abstract class ShapelessRecipe extends Recipe {

    private final Collection<RecipeIngredient> ingredients = new ArrayList<>();

    protected ShapelessRecipe(@NotNull NamespacedKey key) {
        super(key);
    }

    public final @NotNull Collection<RecipeIngredient> ingredients() {
        return this.ingredients;
    }

    public final void addIngredient(@NotNull RecipeIngredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public final void addIngredient(@NotNull ItemStack ingredient) {
        this.addIngredient(new ExactRecipeIngredient(ingredient.clone()));
    }

    public final void addIngredient(@NotNull ItemStack ingredient, int amount) {
        ItemStack clone = ingredient.clone();
        clone.setAmount(amount);
        this.addIngredient(new ExactRecipeIngredient(clone));
    }

    public final void addIngredient(@NotNull Material material) {
        this.addIngredient(new MaterialRecipeIngredient(material));
    }

    public final void addIngredient(@NotNull Material material, int amount) {
        ItemStack itemStack = new ItemStack(material);
        itemStack.setAmount(amount);
        this.addIngredient(new ExactRecipeIngredient(itemStack));
    }
}