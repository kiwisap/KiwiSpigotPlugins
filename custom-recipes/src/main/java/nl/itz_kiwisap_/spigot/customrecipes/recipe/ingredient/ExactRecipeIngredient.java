package nl.itz_kiwisap_.spigot.customrecipes.recipe.ingredient;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ExactRecipeIngredient implements RecipeIngredient {

    private final List<ItemStack> choices;

    public ExactRecipeIngredient(@NotNull List<ItemStack> choices) {
        this.choices = new ArrayList<>(choices);
        this.choices.removeIf(itemStack -> itemStack == null || itemStack.getType().isAir());
    }

    public ExactRecipeIngredient(@NotNull ItemStack itemStack) {
        this(Collections.singletonList(itemStack));
    }

    @Override
    public boolean test(ItemStack itemStack) {
        if (this.choices.isEmpty()) {
            return itemStack == null || itemStack.getType().isAir();
        }

        if (itemStack == null || itemStack.getType().isAir()) {
            return false;
        }

        for (ItemStack choice : this.choices) {
            if (!choice.getType().isAir() && itemStack.getAmount() >= choice.getAmount() && itemStack.isSimilar(choice)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public @NotNull List<ItemStack> getChoices() {
        return new ArrayList<>(this.choices);
    }
}