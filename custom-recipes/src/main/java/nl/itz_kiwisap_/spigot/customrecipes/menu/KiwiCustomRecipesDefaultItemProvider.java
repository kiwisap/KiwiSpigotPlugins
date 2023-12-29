package nl.itz_kiwisap_.spigot.customrecipes.menu;

import nl.itz_kiwisap_.spigot.common.item.ItemBuilder;
import nl.odalitadevelopments.menus.menu.providers.MenuProvider;
import nl.odalitadevelopments.menus.pagination.IPagination;
import nl.odalitadevelopments.menus.providers.providers.DefaultItemProvider;
import nl.odalitadevelopments.menus.scrollable.Scrollable;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class KiwiCustomRecipesDefaultItemProvider implements DefaultItemProvider {

    @Override
    public @NotNull ItemStack closeItem() {
        return ItemBuilder.of(Material.BARRIER, "&cClose").build(); // TODO load from config
    }

    @Override
    public @NotNull ItemStack backItem(@NotNull MenuProvider menuProvider) {
        return new ItemStack(Material.AIR); // Not used
    }

    @Override
    public @NotNull ItemStack nextPageItem(@NotNull IPagination<?, ?> iPagination) {
        return new ItemStack(Material.AIR); // Not used
    }

    @Override
    public @NotNull ItemStack previousPageItem(@NotNull IPagination<?, ?> iPagination) {
        return new ItemStack(Material.AIR); // Not used
    }

    @Override
    public @NotNull ItemStack scrollUpItem(@NotNull Scrollable scrollable) {
        return new ItemStack(Material.AIR); // Not used
    }

    @Override
    public @NotNull ItemStack scrollDownItem(@NotNull Scrollable scrollable) {
        return new ItemStack(Material.AIR); // Not used
    }

    @Override
    public @NotNull ItemStack scrollLeftItem(@NotNull Scrollable scrollable) {
        return new ItemStack(Material.AIR); // Not used
    }

    @Override
    public @NotNull ItemStack scrollRightItem(@NotNull Scrollable scrollable) {
        return new ItemStack(Material.AIR); // Not used
    }
}