package me.devnatan.pickupore.listener;

import me.devnatan.pickupore.PickupOre;
import me.devnatan.pickupore.util.BukkitUtil;
import me.devnatan.pickupore.util.FortuneUtil;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Copyright (c) 2018 DevNatan. Todos os direitos reservados.
 * @author DevNatan
 * @version 1.0
 */
public class BlockBreakListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreakEvent(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (p != null && p.getGameMode() != GameMode.CREATIVE) {
            Block b = e.getBlock();
            Material t = b.getType();
            if (t.isSolid() && t.name().endsWith("_ORE")) {
                ItemStack hand = p.getItemInHand();

                if (hand.getType().name().endsWith("_PICKAXE")) {
                    Collection<ItemStack> dr = b.getDrops();
                    int exp = e.getExpToDrop();
                    if (hand.getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
                        int level = hand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                        dr = FortuneUtil.getDrops(level, dr);
                        exp = exp + (level * dr.size()) * 7;
                    }

                    ItemStack[] arr = dr != null ? dr.toArray(new ItemStack[0]) : new ItemStack[0];
                    if(arr != null && arr.length > 0) {
                        p.getInventory().addItem(arr);
                    }
                    e.setCancelled(true);
                    try {
                        b.breakNaturally(null);
                    } catch (Exception ex) {
                        b.breakNaturally(new ItemStack(Material.AIR));
                    }
                    p.giveExp(exp);

                    if(!PickupOre.getAutoBlockToggle().contains(p.getName()) && p.getInventory().firstEmpty() == -1) {
                        int a = BukkitUtil.autoSmelt(p.getInventory());
                        if(a == 0) {
                            if(!PickupOre.getAutoSellToggle().contains(p.getName())) {
                                AtomicInteger amount = new AtomicInteger();
                                AtomicInteger price = new AtomicInteger();
                                Inventory inv = p.getInventory();
                                for (int i = 0; i < inv.getSize(); i++) {
                                    ItemStack item = inv.getItem(i);
                                    if (item != null && item.getType() != Material.AIR) {
                                        int finalI = i;
                                        PickupOre.getAutoSell().stream()
                                                .filter(autoItem -> autoItem.getItem().getType() == item.getType())
                                                .forEach(autoItem -> {
                                                    amount.addAndGet(item.getAmount());
                                                    price.addAndGet(autoItem.getPrice() * item.getAmount());
                                                    inv.clear(finalI);
                                                });
                                    }
                                }

                                if (amount.intValue() > 0) {
                                    PickupOre.getEconomy().depositPlayer(p, price.intValue());
                                }

                                p.sendMessage(ChatColor.GOLD + "Vendeu " + amount.intValue() + " itens por R$ " + price.intValue() + ".");
                            } else {
                                p.sendMessage(ChatColor.GOLD + "Seu inventário está cheio.");
                            }
                        }
                    }
                }
            }
        }
    }

}
