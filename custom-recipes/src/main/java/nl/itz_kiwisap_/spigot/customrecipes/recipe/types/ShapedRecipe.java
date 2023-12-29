package nl.itz_kiwisap_.spigot.customrecipes.recipe.types;

import nl.itz_kiwisap_.spigot.customrecipes.recipe.Recipe;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.ingredient.ExactRecipeIngredient;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.ingredient.MaterialRecipeIngredient;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.ingredient.RecipeIngredient;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class ShapedRecipe extends Recipe {

    private final Map<Character, RecipeIngredient> ingredients = new HashMap<>();

    protected ShapedRecipe(@NotNull NamespacedKey key) {
        super(key);
    }

    public abstract @NotNull String[] shape();

    public final @NotNull Map<Character, RecipeIngredient> ingredients() {
        return this.ingredients;
    }

    public final void addIngredient(char key, @NotNull RecipeIngredient ingredient) {
        this.ingredients.put(key, ingredient);
    }

    public final void addIngredient(char key, @NotNull ItemStack ingredient) {
        this.addIngredient(key, new ExactRecipeIngredient(ingredient.clone()));
    }

    public final void addIngredient(char key, @NotNull ItemStack ingredient, int amount) {
        ItemStack clone = ingredient.clone();
        clone.setAmount(amount);
        this.addIngredient(key, new ExactRecipeIngredient(clone));
    }

    public final void addIngredient(char key, @NotNull Material material) {
        this.addIngredient(key, new MaterialRecipeIngredient(material));
    }

    public final void addIngredient(char key, @NotNull Material material, int amount) {
        ItemStack itemStack = new ItemStack(material);
        itemStack.setAmount(amount);
        this.addIngredient(key, new ExactRecipeIngredient(itemStack));
    }
}