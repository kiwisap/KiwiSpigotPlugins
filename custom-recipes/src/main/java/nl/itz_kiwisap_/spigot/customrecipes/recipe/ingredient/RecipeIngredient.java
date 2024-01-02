package nl.itz_kiwisap_.spigot.customrecipes.recipe.ingredient;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public interface RecipeIngredient extends Predicate<ItemStack> {

    @NotNull List<ItemStack> getChoices();
}