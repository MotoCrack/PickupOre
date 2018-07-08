package me.devnatan.pickupore.listener;

import me.devnatan.pickupore.PickupOre;
import me.devnatan.pickupore.inventory.InventoryId;
import me.devnatan.pickupore.nbt.NBTCompound;
import me.devnatan.pickupore.nbt.NBTItem;
import me.devnatan.pickupore.util.BukkitUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) 2018 DevNatan. Todos os direitos reservados.
 * @author DevNatan
 * @version 1.0
 * @deprecated Não utiliza-se menu no plugin
 */
@Deprecated
public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        if(e.getWhoClicked() instanceof Player &&
                e.getInventory().getHolder() != null &&
                e.getInventory().getHolder() instanceof InventoryId) {
            ItemStack is = e.getCurrentItem();
            if(is != null && is.getType() != Material.AIR) {
                NBTItem nbt = new NBTItem(is);
                if(nbt.hasKey("sell")) {
                    e.setCancelled(true);
                    NBTCompound compound = nbt.getCompound("sell");
                    Integer price = compound.getInteger("price");
                    Player p = (Player) e.getWhoClicked();
                    int[] val = BukkitUtil.getPriceAndRemove(p.getInventory(), is.getType(), price);
                    PickupOre.getEconomy().depositPlayer(p, val[1]);
                    p.sendMessage(ChatColor.GOLD + "Vendeu " + val[0] + " itens por R$ " + BukkitUtil.format(val[1], false) + ".");
                }
            }
        }
    }

}
