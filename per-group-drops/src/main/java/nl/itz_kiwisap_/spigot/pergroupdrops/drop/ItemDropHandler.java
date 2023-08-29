package nl.itz_kiwisap_.spigot.pergroupdrops.drop;

import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketSpawnEntity;
import nl.itz_kiwisap_.spigot.nms.scoreboard.KScoreboardTeam;
import nl.itz_kiwisap_.spigot.pergroupdrops.KiwiPerGroupDropsPlugin;
import nl.itz_kiwisap_.spigot.pergroupdrops.api.provider.GlowProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.api.provider.GroupProvider;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public final class ItemDropHandler implements Listener {

    private final Collection<Integer> playerItems = new HashSet<>();
    private final Map<String, Collection<Integer>> groupItems = new HashMap<>();

    private final KiwiPerGroupDropsPlugin plugin;

    public ItemDropHandler(KiwiPerGroupDropsPlugin plugin) {
        this.plugin = plugin;

        // Don't spawn entities client-side that are not supposed to be seen by the player
        plugin.getPacketInterceptorHandler().interceptClientbound(KClientboundPacketSpawnEntity.class, (player, packet) -> {
            GroupProvider groupProvider = this.plugin.getGroupProvider();
            if (groupProvider == null) return false; // No group provider set, so no groups to handle

            String group = groupProvider.getGroup(player);
            if (group == null) return false; // Player is not in a group, so no group to handle

            int entityId = packet.entityId();

            Collection<Integer> ids = this.groupItems.getOrDefault(group, new HashSet<>());
            return this.playerItems.contains(entityId) && !ids.contains(entityId);
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerDropItem(PlayerDropItemEvent event) {
        GroupProvider groupProvider = this.plugin.getGroupProvider();
        if (groupProvider == null) return; // No group provider set, so no groups to handle

        Player player = event.getPlayer();
        String group = groupProvider.getGroup(player);
        if (group == null) return; // Player is not in a group, so no group to handle

        Item item = event.getItemDrop();

        GlowProvider glowProvider = this.plugin.getGlowProvider();
        if (glowProvider != null) {
            ChatColor glowColor = glowProvider.getGlowColor(group, item.getItemStack());

            if (glowColor != null) {
                KScoreboardTeam scoreboardTeam = this.plugin.getScoreboardHandler().getTeam(glowColor);

                // If team is found, enable glowing and add the item to the team with the glow color
                if (scoreboardTeam != null) {
                    item.setGlowing(true);
                    this.plugin.getScoreboardHandler().addItemToTeam(player, scoreboardTeam, item);
                }
            }
        }

        this.playerItems.add(item.getEntityId());
        this.groupItems.compute(group, (key, value) -> {
            if (value == null) value = new HashSet<>();
            value.add(item.getEntityId());
            return value;
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onEntityPickupItem(EntityPickupItemEvent event) {
        int entityId = event.getItem().getEntityId();

        if (!(event.getEntity() instanceof Player player)) {
            if (event.getRemaining() == 0) {
                this.playerItems.remove(entityId);
                this.groupItems.values().forEach(items -> items.remove(entityId));
            }
            return;
        }

        GroupProvider groupProvider = this.plugin.getGroupProvider();
        if (groupProvider == null) return; // No group provider set, so no groups to handle

        String group = groupProvider.getGroup(player);
        if (group == null) return; // Player is not in a group, so no group to handle

        Collection<Integer> ids = this.groupItems.getOrDefault(group, new HashSet<>());
        if (this.playerItems.contains(entityId) && !ids.contains(entityId)) {
            event.setCancelled(true);
            return;
        }

        this.playerItems.remove(entityId);
        ids.remove(entityId);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onEntityRemoveFromWorld(ItemDespawnEvent event) {
        Item item = event.getEntity();
        this.playerItems.remove(item.getEntityId());
        this.groupItems.values().forEach(items -> items.remove(item.getEntityId()));
    }
}