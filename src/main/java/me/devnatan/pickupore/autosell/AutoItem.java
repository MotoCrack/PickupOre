package me.devnatan.pickupore.autosell;

import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) 2018 DevNatan. Todos os direitos reservados.
 * @author DevNatan
 * @version 1.0
 */
public class AutoItem {

    private final ItemStack item;
    @Deprecated private final int slot;
    private final int price;

    public AutoItem(ItemStack item, int slot, int price) {
        this.item = item;
        this.slot = slot;
        this.price = price;
    }

    /**
     * Retorna a representação do item
     * @return ItemStack
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * Retorna o slot do item no inventário
     * @return int
     * @deprecated Não utiliza-se menu no plugin
     */
    @Deprecated
    public int getSlot() {
        return slot;
    }

    /**
     * Preço UNITÁRIO do item.
     * @return int
     */
    public int getPrice() {
        return price;
    }
}
