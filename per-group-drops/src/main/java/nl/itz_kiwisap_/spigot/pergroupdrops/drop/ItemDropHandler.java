package nl.itz_kiwisap_.spigot.pergroupdrops.drop;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nl.itz_kiwisap_.spigot.nms.KiwiNMS;
import nl.itz_kiwisap_.spigot.nms.network.KiwiPacketWrapper;
import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketEntityMetadata;
import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketSpawnEntity;
import nl.itz_kiwisap_.spigot.nms.scoreboard.KScoreboardTeam;
import nl.itz_kiwisap_.spigot.pergroupdrops.KiwiPerGroupDrops;
import nl.itz_kiwisap_.spigot.pergroupdrops.KiwiPerGroupDropsConstants;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.GlowColor;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GlowProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GroupProvider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public final class ItemDropHandler implements Listener {

    private static final byte GLOWING_FLAG_BIT = 0x40;

    private final Collection<Integer> playerItems = Sets.newCopyOnWriteArraySet();
    private final Map<String, Collection<Integer>> groupItems = Maps.newConcurrentMap();

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
            if (!this.playerItems.contains(entityId)) return false;

            Collection<Integer> ids = this.groupItems.getOrDefault(group, new HashSet<>());
            if (ids.contains(entityId)) {
                KiwiNMS.getInstance().runSync(() -> {
                    Entity entity = KiwiNMS.getInstance().getEntityById(player.getWorld(), entityId);
                    if (!(entity instanceof Item item)) return;

                    String glowColor = entity.getPersistentDataContainer().get(KiwiPerGroupDropsConstants.GLOW_KEY, PersistentDataType.STRING);
                    if (glowColor == null) return;

                    KScoreboardTeam scoreboardTeam = this.instance.getScoreboardHandler().getTeam(GlowColor.valueOf(glowColor));
                    if (scoreboardTeam == null) return;

                    this.instance.getScoreboardHandler().addItemToTeam(player, scoreboardTeam, item);
                });

                return false;
            }

            return true;
        });

        // Make sure the glowing flag is set for the player to see the item, but only when the entity flags metadata is provided in the packet
        instance.getPacketInterceptorHandler().interceptClientbound(KClientboundPacketEntityMetadata.class, (player, packet) -> {
            if (player.hasPermission(KiwiPerGroupDropsConstants.BYPASS_PERMISSION)) {
                int entityId = packet.entityId();
                if (!this.playerItems.contains(entityId)) return false;

                KClientboundPacketEntityMetadata.Entry<Byte> entry = packet.getEntry(0);
                if (entry == null) return false;

                byte value = entry.value();
                if ((value & GLOWING_FLAG_BIT) == GLOWING_FLAG_BIT) {
                    value &= ~GLOWING_FLAG_BIT;

                    List<KClientboundPacketEntityMetadata.Entry<?>> entries = packet.entries();
                    entries.removeIf(e -> e.index() == 0);
                    entries.add(new KClientboundPacketEntityMetadata.Entry<>(0, entry.serializerId(), value));

                    KiwiNMS nms = KiwiNMS.getInstance();

                    KiwiPacketWrapper packetEntityMetadata = nms.createPacketEntityMetadata(entityId, entries);
                    if (packet.getBundleNMSInstance() != null) {
                        nms.addPacketToBundle(packet.getBundleNMSInstance(), packetEntityMetadata.packet());
                    } else {
                        nms.sendPacket(player, packetEntityMetadata);
                    }

                    return true;
                }
            }

            return false;
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
        item.getPersistentDataContainer().set(KiwiPerGroupDropsConstants.GROUP_KEY, PersistentDataType.STRING, group);

        GlowProvider glowProvider = this.instance.getProvider().getGlowProvider();
        if (glowProvider != null) {
            GlowColor glowColor = glowProvider.getGlowColor(group, item.getItemStack());

            if (glowColor != null) {
                KScoreboardTeam scoreboardTeam = this.instance.getScoreboardHandler().getTeam(glowColor);

                // If team is found, enable glowing and add the item to the team with the glow color
                if (scoreboardTeam != null) {
                    item.setGlowing(true);
                    item.getPersistentDataContainer().set(KiwiPerGroupDropsConstants.GLOW_KEY, PersistentDataType.STRING, glowColor.name());
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

        String entityGroup = entity.getPersistentDataContainer().get(KiwiPerGroupDropsConstants.GROUP_KEY, PersistentDataType.STRING);
        String targetGroup = target.getPersistentDataContainer().get(KiwiPerGroupDropsConstants.GROUP_KEY, PersistentDataType.STRING);
        if (entityGroup == null || targetGroup == null) return;

        if (!entityGroup.equals(targetGroup)) {
            event.setCancelled(true);
        }
    }
}