package nl.itz_kiwisap_.spigot.pergroupdrops.drop;

import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketSpawnEntity;
import nl.itz_kiwisap_.spigot.nms.scoreboard.KScoreboardTeam;
import nl.itz_kiwisap_.spigot.pergroupdrops.KiwiPerGroupDrops;
import nl.itz_kiwisap_.spigot.pergroupdrops.KiwiPerGroupDropsConstants;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.GlowProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.GroupProvider;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public final class ItemDropHandler implements Listener {

    private final Collection<Integer> playerItems = new HashSet<>();
    private final Map<String, Collection<Integer>> groupItems = new HashMap<>();

    private final KiwiPerGroupDrops instance;

    public ItemDropHandler(KiwiPerGroupDrops instance) {
        this.instance = instance;

        // Don't spawn entities client-side that are not supposed to be seen by the player
        instance.getPacketInterceptorHandler().interceptClientbound(KClientboundPacketSpawnEntity.class, (player, packet) -> {
            if (player.hasPermission(KiwiPerGroupDropsConstants.BYPASS_PERMISSION)) return false;

            GroupProvider groupProvider = this.instance.getProvider().getGroupProvider();
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
        if (event.getPlayer().hasPermission(KiwiPerGroupDropsConstants.BYPASS_PERMISSION)) return;

        GroupProvider groupProvider = this.instance.getProvider().getGroupProvider();
        if (groupProvider == null) return; // No group provider set, so no groups to handle

        Player player = event.getPlayer();
        String group = groupProvider.getGroup(player);
        if (group == null) return; // Player is not in a group, so no group to handle

        Item item = event.getItemDrop();

        GlowProvider glowProvider = this.instance.getProvider().getGlowProvider();
        if (glowProvider != null) {
            ChatColor glowColor = glowProvider.getGlowColor(group, item.getItemStack());

            if (glowColor != null) {
                KScoreboardTeam scoreboardTeam = this.instance.getScoreboardHandler().getTeam(glowColor);

                // If team is found, enable glowing and add the item to the team with the glow color
                if (scoreboardTeam != null) {
                    item.setGlowing(true);
                    this.instance.getScoreboardHandler().addItemToTeam(player, scoreboardTeam, item);
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

        GroupProvider groupProvider = this.instance.getProvider().getGroupProvider();
        if (groupProvider == null) return; // No group provider set, so no groups to handle

        String group = groupProvider.getGroup(player);
        if (group == null) return; // Player is not in a group, so no group to handle

        Collection<Integer> ids = this.groupItems.getOrDefault(group, new HashSet<>());
        if (!player.hasPermission(KiwiPerGroupDropsConstants.BYPASS_PERMISSION) && this.playerItems.contains(entityId) && !ids.contains(entityId)) {
            event.setCancelled(true);
            return;
        }

        this.playerItems.remove(entityId);
        ids.remove(entityId);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onItemDespawn(ItemDespawnEvent event) {
        Item item = event.getEntity();
        this.playerItems.remove(item.getEntityId());
        this.groupItems.values().forEach(items -> items.remove(item.getEntityId()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onItemMerge(ItemMergeEvent event) {
        Item entity = event.getEntity();
        Item target = event.getTarget();

        if (!this.playerItems.contains(entity.getEntityId()) && !this.playerItems.contains(target.getEntityId())) {
            return;
        }

        for (Collection<Integer> value : this.groupItems.values()) {
            if (value.contains(entity.getEntityId()) && !value.contains(target.getEntityId())) {
                event.setCancelled(true);
                break;
            }
        }
    }
}