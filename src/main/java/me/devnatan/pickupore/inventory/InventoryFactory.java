package me.devnatan.pickupore.inventory;

import me.devnatan.pickupore.PickupOre;
import me.devnatan.pickupore.autosell.AutoItem;
import me.devnatan.pickupore.nbt.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright (c) 2018 DevNatan. Todos os direitos reservados.
 * @author DevNatan
 * @version 1.0
 * @deprecated Não utiliza-se menu no plugin
 */
@Deprecated
public class InventoryFactory {

    public static Inventory getInventory() {
        List<AutoItem> list = PickupOre.getAutoSell();
        Inventory inventory = Bukkit.createInventory(new InventoryId(), 54, "Vender");

        list.forEach(item -> {
            ItemStack other = item.getItem().clone();
            ItemMeta im = other.getItemMeta();

            im.setLore(Arrays.asList(
                    "§eValor unitário: §fR$ " + item.getPrice(),
                    "§eClique para vender todos."
            ));

            other.setItemMeta(im);

            NBTItem nbt = new NBTItem(other);
            nbt.addCompound("sell");
            nbt.getCompound("sell").setInteger("price", item.getPrice());
            inventory.setItem(item.getSlot(), nbt.getItem());
        });

        return inventory;
    }

}
