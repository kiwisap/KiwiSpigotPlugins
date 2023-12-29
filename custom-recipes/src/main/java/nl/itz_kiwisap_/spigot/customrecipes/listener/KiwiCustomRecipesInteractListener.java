package nl.itz_kiwisap_.spigot.customrecipes.listener;

import nl.itz_kiwisap_.spigot.customrecipes.KiwiCustomRecipes;
import nl.itz_kiwisap_.spigot.customrecipes.menu.types.CraftingTableMenu;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public final class KiwiCustomRecipesInteractListener implements Listener {

    private final KiwiCustomRecipes instance;

    public KiwiCustomRecipesInteractListener(KiwiCustomRecipes instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        Player player = event.getPlayer();

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock != null) {
            if (clickedBlock.getType() == Material.CRAFTING_TABLE) {
                event.setCancelled(true);
                this.instance.getMenuManager().openMenu(new CraftingTableMenu(), player);
            }
        }
    }
}