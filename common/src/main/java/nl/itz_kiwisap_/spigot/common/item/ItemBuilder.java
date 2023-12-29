package nl.itz_kiwisap_.spigot.common.item;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public final class ItemBuilder {

    private final ItemStack stack;
    private ItemMeta meta;

    private ItemBuilder(@NotNull ItemStack stack) {
        this.stack = stack;
        this.meta = this.stack.getItemMeta();

        if (this.meta == null) {
            this.meta = Bukkit.getItemFactory().getItemMeta(this.stack.getType());
        }
    }

    public static @NotNull ItemBuilder of(@NotNull ItemStack stack) {
        return new ItemBuilder(stack);
    }

    public static @NotNull ItemBuilder of(@NotNull ItemStack stack, @NotNull String displayName) {
        return of(stack).displayName(displayName);
    }

    public static @NotNull ItemBuilder of(@NotNull Material material) {
        return of(new ItemStack(material));
    }

    public static @NotNull ItemBuilder of(@NotNull Material material, @NotNull String displayName) {
        return of(material).displayName(displayName);
    }

    public static @NotNull ItemBuilder clone(@NotNull ItemStack stack) {
        return of(stack.clone());
    }

    public ItemBuilder meta(@NotNull Consumer<ItemMeta> metaConsumer) {
        metaConsumer.accept(this.meta);
        return this;
    }

    public <T extends ItemMeta> ItemBuilder meta(@NotNull Class<T> metaClass, @NotNull UnaryOperator<T> metaUnaryOperator) {
        this.meta = metaUnaryOperator.apply(metaClass.cast(this.meta));
        return this;
    }

    public <T extends ItemMeta> ItemBuilder metaConsumer(@NotNull Class<T> metaClass, @NotNull Consumer<T> metaConsumer) {
        return this.meta(metaClass, (meta) -> {
            metaConsumer.accept(meta);
            return meta;
        });
    }

    public ItemBuilder displayName(@NotNull String displayName) {
        return this.meta((meta) -> meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName)));
    }

    public ItemBuilder durability(short durability) {
        this.stack.setDurability(durability);
        return this;
    }

    public ItemBuilder material(@NotNull Material material) {
        this.stack.setType(material);
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.stack.setAmount(amount);
        return this;
    }

    public ItemBuilder lore(@NotNull List<String> lore) {
        return this.meta((meta) -> {
            List<String> loreList = new ArrayList<>();

            for (String s : lore) {
                loreList.add(ChatColor.translateAlternateColorCodes('&', s));
            }

            this.meta.setLore(loreList);
        });
    }

    public ItemBuilder lore(@NotNull String... lore) {
        return this.lore(Arrays.asList(lore));
    }

    public ItemBuilder lore(@NotNull UnaryOperator<@NotNull List<String>> loreUnaryOperator) {
        return this.lore(loreUnaryOperator.apply((this.meta.getLore() == null) ? new ArrayList<>() : this.meta.getLore()));
    }

    public ItemBuilder addToLore(@NotNull String line) {
        return this.lore((lore) -> {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
            return lore;
        });
    }

    public ItemBuilder removeFromLore(@NotNull String line) {
        return this.lore((lore) -> {
            lore.removeIf((loreLine) -> loreLine.equals(ChatColor.translateAlternateColorCodes('&', line)));
            return lore;
        });
    }

    public ItemBuilder removeLore() {
        return this.meta((meta) -> meta.setLore(null));
    }

    public ItemBuilder enchantment(@NotNull Enchantment enchantment, int level) {
        return this.meta((meta) -> meta.addEnchant(enchantment, level, true));
    }

    public ItemBuilder removeEnchantments() {
        return this.meta((meta) -> meta.getEnchants().forEach((enchantment, level) -> meta.removeEnchant(enchantment)));
    }

    public ItemBuilder itemFlags(@NotNull ItemFlag... flags) {
        return this.meta((meta) -> meta.addItemFlags(flags));
    }

    public ItemBuilder removeItemFlags(@NotNull ItemFlag... flags) {
        return this.meta((meta) -> meta.removeItemFlags(flags));
    }

    public ItemBuilder allItemFlags() {
        return this.itemFlags(ItemFlag.values());
    }

    public ItemBuilder removeAllItemFlags() {
        return this.removeItemFlags(ItemFlag.values());
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        return this.meta((meta) -> meta.setUnbreakable(unbreakable));
    }

    public ItemBuilder unbreakable() {
        return unbreakable(true);
    }

    public ItemStack build() {
        this.stack.setItemMeta(this.meta);
        return this.stack;
    }
}