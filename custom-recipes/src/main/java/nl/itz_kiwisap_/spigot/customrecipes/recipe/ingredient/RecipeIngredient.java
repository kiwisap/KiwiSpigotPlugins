package nl.itz_kiwisap_.spigot.customrecipes.recipe.ingredient;

import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

public interface RecipeIngredient extends Predicate<ItemStack> {
}