package nl.itz_kiwisap_.spigot.customrecipes.recipe.converter;

import nl.itz_kiwisap_.spigot.customrecipes.recipe.Recipe;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.ingredient.ExactRecipeIngredient;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.ingredient.MaterialRecipeIngredient;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.ingredient.RecipeIngredient;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.types.ShapedRecipe;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.types.ShapelessRecipe;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public final class BukkitRecipeConverter {

   public  Recipe convertRecipe(org.bukkit.inventory.Recipe bukkitRecipe) {
        if (bukkitRecipe instanceof org.bukkit.inventory.ShapedRecipe shapedRecipe) {
            return this.convertShapedRecipe(shapedRecipe);
        }

        if (bukkitRecipe instanceof org.bukkit.inventory.ShapelessRecipe shapelessRecipe) {
            return this.convertShapelessRecipe(shapelessRecipe);
        }

        return null;
    }

    private ShapedRecipe convertShapedRecipe(org.bukkit.inventory.ShapedRecipe bukkitRecipe) {
        return new BukkitShapedRecipeImpl(
                bukkitRecipe.getKey(),
                bukkitRecipe.getResult(),
                bukkitRecipe.getShape(),
                bukkitRecipe.getChoiceMap()
        );
    }

    private ShapelessRecipe convertShapelessRecipe(org.bukkit.inventory.ShapelessRecipe bukkitRecipe) {
        return new BukkitShapelessRecipeImpl(
                bukkitRecipe.getKey(),
                bukkitRecipe.getResult(),
                bukkitRecipe.getChoiceList()
        );
    }

    private RecipeIngredient convertRecipeChoice(RecipeChoice bukkitRecipeChoice) {
        if (bukkitRecipeChoice instanceof RecipeChoice.MaterialChoice materialChoice) {
            return new MaterialRecipeIngredient(materialChoice.getChoices());
        }

        if (bukkitRecipeChoice instanceof RecipeChoice.ExactChoice exactChoice) {
            return new ExactRecipeIngredient(exactChoice.getChoices());
        }

        throw new IllegalArgumentException("Unknown RecipeChoice type: " + bukkitRecipeChoice.getClass().getName());
    }

    private final class BukkitShapedRecipeImpl extends ShapedRecipe {

        private final ItemStack result;
        private final String[] shape;

        private BukkitShapedRecipeImpl(NamespacedKey key, ItemStack result, String[] shape, Map<Character, RecipeChoice> ingredients) {
            super(key);

            this.result = result;
            this.shape = shape;

            for (Map.Entry<Character, RecipeChoice> entry : ingredients.entrySet()) {
                if (entry.getValue() == null) continue;
                this.addIngredient(entry.getKey(), convertRecipeChoice(entry.getValue()));
            }
        }

        @Override
        public @NotNull ItemStack result() {
            return this.result;
        }

        @Override
        public @NotNull String[] shape() {
            return this.shape;
        }
    }

    private final class BukkitShapelessRecipeImpl extends ShapelessRecipe {

        private final ItemStack result;

        private BukkitShapelessRecipeImpl(NamespacedKey key, ItemStack result, List<RecipeChoice> ingredients) {
            super(key);

            this.result = result;

            for (RecipeChoice ingredient : ingredients) {
                if (ingredient == null) continue;
                this.addIngredient(convertRecipeChoice(ingredient));
            }
        }

        @Override
        public @NotNull ItemStack result() {
            return this.result;
        }
    }
}