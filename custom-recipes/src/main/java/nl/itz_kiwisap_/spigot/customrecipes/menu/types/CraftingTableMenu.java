package nl.itz_kiwisap_.spigot.customrecipes.menu.types;

import nl.itz_kiwisap_.spigot.common.item.ItemBuilder;
import nl.itz_kiwisap_.spigot.customrecipes.menu.KiwiCustomRecipesMenuProvider;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.Recipe;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.RecipeHandler;
import nl.odalitadevelopments.menus.annotations.Menu;
import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemsCloseAction;
import nl.odalitadevelopments.menus.items.ClickableItem;
import nl.odalitadevelopments.menus.items.DisplayItem;
import nl.odalitadevelopments.menus.items.buttons.CloseItem;
import nl.odalitadevelopments.menus.menu.type.MenuType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

@Menu(
        title = "Craft Item",
        type = MenuType.CHEST_6_ROW
)
public final class CraftingTableMenu implements KiwiCustomRecipesMenuProvider {

    private static final int[] CRAFTING_GRID_SLOTS = {
            10, 11, 12,
            19, 20, 21,
            28, 29, 30
    };

    @Override
    public void onLoad(@NotNull Player player, @NotNull MenuContents contents, @NotNull RecipeHandler handler) {
        contents.fill(DisplayItem.of(ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE, "&0").build()));

        for (int craftingGridSlot : CRAFTING_GRID_SLOTS) {
            contents.clear(craftingGridSlot);
        }

        contents.placeableItemsCloseAction(PlaceableItemsCloseAction.RETURN);
        contents.registerPlaceableItemSlots(CRAFTING_GRID_SLOTS);

        contents.events().onPlaceableItemShiftClick((slots, addedItem, event) -> {
            this.matchRecipe(contents, handler);
            return true;
        });

        contents.events().onPlaceableItemClick((slot, event) -> {
            this.matchRecipe(contents, handler);
            return true;
        });

        contents.events().onPlaceableItemDrag((slots, event) -> {
            this.matchRecipe(contents, handler);
            return true;
        });

        contents.setRefreshable(23, () -> {
            ItemStack result = contents.cache("RESULT", null);
            if (result == null) {
                return DisplayItem.of(ItemBuilder.of(Material.BARRIER, "&cRecipe required")
                        .lore(
                                "&7Add the items for a valid recipe",
                                "&7in the crafting grid to the left."
                        )
                        .build()
                );
            } else {
                return ClickableItem.of(result, (event) -> {
                    event.getWhoClicked().sendMessage("You clicked the result item!");
                });
            }
        });

        contents.fillRow(5, DisplayItem.of(ItemBuilder.of(Material.RED_STAINED_GLASS_PANE, "&0").build()));
        contents.set(49, CloseItem.get());
    }

    private void matchRecipe(MenuContents contents, RecipeHandler handler) {
        // Always reset result
        contents.pruneCache("RESULT");
        contents.refreshItem(23);

        BukkitTask task = contents.cache("TASK", null);
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }

        task = Bukkit.getScheduler().runTaskLaterAsynchronously(contents.menuSession().getInstance().getJavaPlugin(), () -> {
            ItemStack[][] matrix = new ItemStack[3][3];

            for (int craftingGridSlot : CRAFTING_GRID_SLOTS) {
                ItemStack itemStack = contents.getPlaceableItems().get(craftingGridSlot);
                if (itemStack == null || itemStack.getType().isAir()) continue;

                int row = (craftingGridSlot - 10) / 9;
                int column = (craftingGridSlot - 10) % 9;

                matrix[row][column] = itemStack;
            }

            Recipe recipe = handler.matchRecipe(matrix);
            if (recipe == null) return;

            contents.setCache("RESULT", recipe.result());
            contents.refreshItem(23);
        }, 1L);

        contents.setCache("TASK", task);
    }
}