package me.devnatan.pickupore.listener;

import me.devnatan.pickupore.PickupOre;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Copyright (c) 2018 DevNatan. Todos os direitos reservados.
 * @author DevNatan
 * @version 1.0
 */
public class EntityDeathListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDeathEvent(EntityDeathEvent e) {
        if(!(e.getEntity() instanceof Player) && e.getEntity().getKiller() != null){
            Player p = e.getEntity().getKiller();
            ItemStack hand = p.getItemInHand();

            if(hand != null && (hand.getType().name().endsWith("_AXE") || hand.getType().name().endsWith("_SWORD"))) {
                if (!PickupOre.getAutoSellToggle().contains(p.getName())) {
                    List<ItemStack> drops = new ArrayList<>(e.getDrops());
                    e.getDrops().clear();

                    AtomicInteger amount = new AtomicInteger();
                    AtomicInteger price = new AtomicInteger();
                    for (ItemStack item : drops) {
                        if (item != null && item.getType() != Material.AIR) {
                            PickupOre.getAutoSell().stream()
                                    .filter(autoItem -> autoItem.getItem().getType() == item.getType())
                                    .forEach(autoItem -> {
                                        amount.addAndGet(item.getAmount());
                                        price.addAndGet(autoItem.getPrice() * item.getAmount());
                                    });
                        }
                    }

                    if (amount.intValue() > 0) {
                        PickupOre.getEconomy().depositPlayer(p, price.intValue());
                    }

                    p.sendMessage(ChatColor.GOLD + "Vendeu automáticamente " + amount.intValue() + " drops por R$ " + price.intValue() + ".");
                } else {
                    p.sendMessage(ChatColor.GOLD + "Você matou este mob com o auto-vender desativado.");
                }
            }
        }
    }

}
