package nl.itz_kiwisap_.spigot.customrecipes.recipe;

import nl.itz_kiwisap_.spigot.customrecipes.recipe.converter.BukkitRecipeConverter;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.wrapper.RecipeWrapper;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

public final class RecipeHandler {

    private final Map<NamespacedKey, RecipeWrapper<?>> recipes = new HashMap<>();

    private final JavaPlugin plugin;
    private final BukkitRecipeConverter recipeConverter;

    public RecipeHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        this.recipeConverter = new BukkitRecipeConverter();

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this::convertBukkitRecipes);
    }

    public Recipe matchRecipe(ItemStack[][] matrix) {
        for (RecipeWrapper<?> wrapper : this.recipes.values()) {
            if (wrapper.matches(matrix)) {
                return wrapper.getRecipe();
            }
        }

        return null;
    }

    public @NotNull Map<NamespacedKey, RecipeWrapper<?>> getRecipes() {
        return this.recipes;
    }

    private void convertBukkitRecipes() {
        Iterator<org.bukkit.inventory.Recipe> iterator = Bukkit.recipeIterator();
        while (iterator.hasNext()) {
            org.bukkit.inventory.Recipe bukkitRecipe = iterator.next();

            try {
                Recipe recipe = this.recipeConverter.convertRecipe(bukkitRecipe);
                if (recipe == null) continue;

                RecipeWrapper<?> wrapper = RecipeWrapper.createWrapper(recipe);

                RecipeWrapper<?> present = this.recipes.putIfAbsent(recipe.key(), wrapper);
                if (present != null) {
                    throw new IllegalArgumentException("Recipe with key " + recipe.key() + " already exists!");
                }
            } catch (Exception exception) {
                this.plugin.getLogger().log(Level.WARNING, "Failed to convert recipe to custom recipe", exception);
            }
        }

        Bukkit.clearRecipes();
    }
}