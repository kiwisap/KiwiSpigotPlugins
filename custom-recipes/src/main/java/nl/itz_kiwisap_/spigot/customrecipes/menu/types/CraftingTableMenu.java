package nl.itz_kiwisap_.spigot.customrecipes.menu.types;

import nl.itz_kiwisap_.spigot.common.item.ItemBuilder;
import nl.itz_kiwisap_.spigot.customrecipes.menu.KiwiCustomRecipesMenuProvider;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.Recipe;
import nl.itz_kiwisap_.spigot.customrecipes.recipe.RecipeHandler;
import nl.itz_kiwisap_.spigot.customrecipes.utils.InventoryUtils;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private static final DisplayItem UNMATCHED_GLASS_PANE = DisplayItem.of(ItemBuilder.of(Material.RED_STAINED_GLASS_PANE, "&0").build());
    private static final DisplayItem MATCHED_GLASS_PANE = DisplayItem.of(ItemBuilder.of(Material.LIME_STAINED_GLASS_PANE, "&0").build());

    @Override
    public void onLoad(@NotNull Player player, @NotNull MenuContents contents, @NotNull RecipeHandler handler) {
        contents.fill(DisplayItem.of(ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE, "&0").build()));

        for (int craftingGridSlot : CRAFTING_GRID_SLOTS) {
            contents.clear(craftingGridSlot);
        }

        contents.placeableItemsCloseAction(PlaceableItemsCloseAction.RETURN);
        contents.registerPlaceableItemSlots(CRAFTING_GRID_SLOTS);

        contents.events().onPlaceableItemShiftClick((slots, addedItem, event) -> {
            this.matchRecipe(contents, handler, null);
            return true;
        });

        contents.events().onPlaceableItemClick((slot, event) -> {
            this.matchRecipe(contents, handler, null);
            return true;
        });

        contents.events().onPlaceableItemDrag((slots, event) -> {
            this.matchRecipe(contents, handler, null);
            return true;
        });

        contents.setRefreshable(23, () -> {
            Recipe resultRecipe = contents.cache("RESULT_RECIPE", null);
            if (resultRecipe == null) {
                return DisplayItem.of(ItemBuilder.of(Material.BARRIER, "&cRecipe required")
                        .lore(
                                "&7Add the items for a valid recipe",
                                "&7in the crafting grid to the left."
                        )
                        .build()
                );
            } else {
                ItemStack result = resultRecipe.result();
                return ClickableItem.of(result, (event) -> {
                    boolean shiftClick = event.isShiftClick();
                    if (shiftClick && player.getInventory().firstEmpty() == -1) {
                        // Inventory full
                        event.setCancelled(true);
                        return;
                    }

                    ItemStack[][] matrix = this.createMatrix(contents);
                    int crafted = handler.craft(resultRecipe, matrix, shiftClick);
                    if (crafted == 0) { // Probably won't happen, but just in case
                        event.setCancelled(true);
                        return;
                    }

                    ItemStack cursor = event.getView().getCursor();
                    List<ItemStack> itemsToAdd = new ArrayList<>();
                    if (!shiftClick) {
                        if (cursor == null || cursor.getType().isAir()) {
                            cursor = result;
                        } else if (cursor.getType() == result.getType() && cursor.getAmount() + result.getAmount() <= result.getMaxStackSize() && cursor.isSimilar(result)) {
                            cursor.setAmount(cursor.getAmount() + result.getAmount());
                        } else {
                            return;
                        }
                    } else {
                        int amount = result.getAmount() * crafted;
                        int items = amount / result.getMaxStackSize();
                        int remainder = amount % result.getMaxStackSize();
                        int spaceNeeded = items + (remainder > 0 ? 1 : 0);
                        if (InventoryUtils.getAmountOfFreeSlots(player) < spaceNeeded) {
                            // Inventory full
                            event.setCancelled(true);
                            return;
                        }

                        ItemStack maxStackSizeClone = result.clone();
                        maxStackSizeClone.setAmount(result.getMaxStackSize());
                        for (int i = 0; i < items; i++) {
                            itemsToAdd.add(maxStackSizeClone);
                        }

                        if (remainder > 0) {
                            ItemStack clone = result.clone();
                            clone.setAmount(remainder);
                            itemsToAdd.add(clone);
                        }
                    }

                    for (int y = 0; y < matrix.length; y++) {
                        for (int x = 0; x < matrix[y].length; x++) {
                            ItemStack itemStack = matrix[y][x];

                            int slot = 10 + (y * 9) + x;
                            contents.setForcedPlaceableItem(slot, itemStack);
                        }
                    }

                    if (!shiftClick) {
                        event.getView().setCursor(cursor);
                    } else {
                        for (ItemStack itemStack : itemsToAdd) {
                            player.getInventory().addItem(itemStack);
                        }
                    }

                    this.matchRecipe(contents, handler, resultRecipe);
                });
            }
        });

        for (int i = 45; i < 54; i++) {
            if (i == 49) continue; // Skip close button

            contents.setRefreshable(i, () -> contents.cache("RESULT_RECIPE", null) == null ? UNMATCHED_GLASS_PANE : MATCHED_GLASS_PANE);
        }

        contents.set(49, CloseItem.get());
    }

    private void matchRecipe(MenuContents contents, RecipeHandler handler, Recipe checkFirst) {
        // Always reset result
        contents.pruneCache("RESULT_RECIPE");
        contents.refreshItem(23);

        BukkitTask task = contents.cache("TASK", null);
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }

        task = Bukkit.getScheduler().runTaskLaterAsynchronously(contents.menuSession().getInstance().getJavaPlugin(), () -> {
            ItemStack[][] matrix = this.createMatrix(contents);

            Recipe recipe = handler.matchRecipe(matrix, checkFirst);
            if (recipe == null) {
                this.refreshBottomRow(contents);
                return;
            }

            contents.setCache("RESULT_RECIPE", recipe);
            contents.refreshItem(23);
            this.refreshBottomRow(contents);
        }, 1L);

        contents.setCache("TASK", task);
    }

    private ItemStack[][] createMatrix(MenuContents contents) {
        ItemStack[][] matrix = new ItemStack[3][3];

        Map<Integer, ItemStack> clonedPlaceableItems = contents.getPlaceableItems().entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().clone()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        for (int craftingGridSlot : CRAFTING_GRID_SLOTS) {
            ItemStack itemStack = clonedPlaceableItems.get(craftingGridSlot);
            if (itemStack == null || itemStack.getType().isAir()) continue;

            int row = (craftingGridSlot - 10) / 9;
            int column = (craftingGridSlot - 10) % 9;

            matrix[row][column] = itemStack;
        }

        return matrix;
    }

    private void refreshBottomRow(MenuContents contents) {
        contents.refreshItems(45, 46, 47, 48, 50, 51, 52, 53);
    }
}