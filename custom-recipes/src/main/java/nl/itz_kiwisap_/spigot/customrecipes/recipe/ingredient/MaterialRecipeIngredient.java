package nl.itz_kiwisap_.spigot.customrecipes.recipe.ingredient;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MaterialRecipeIngredient implements RecipeIngredient {

    private final List<Material> choices;

    public MaterialRecipeIngredient(@NotNull List<Material> choices) {
        this.choices = new ArrayList<>(choices);
        this.choices.removeIf(material -> material == null || material.isAir());
    }

    public MaterialRecipeIngredient(@NotNull Material material) {
        this(Collections.singletonList(material));
    }

    @Override
    public boolean test(ItemStack itemStack) {
        if (this.choices.isEmpty()) {
            return itemStack == null || itemStack.getType().isAir();
        }

        if (itemStack == null || itemStack.getType().isAir()) {
            return false;
        }

        for (Material choice : this.choices) {
            if (itemStack.getType() == choice) {
                return true;
            }
        }

        return false;
    }
}