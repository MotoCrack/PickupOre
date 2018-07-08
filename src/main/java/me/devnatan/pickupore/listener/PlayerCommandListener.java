package me.devnatan.pickupore.listener;

import me.devnatan.pickupore.PickupOre;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Copyright (c) 2018 DevNatan. Todos os direitos reservados.
 * @author DevNatan
 * @version 1.0
 */
public class PlayerCommandListener implements Listener {

    @EventHandler
    public void onPlayerCommandEvent(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        String message = e.getMessage();
        String cmd = message.contains(" ") ? message.split(" ")[0] : message;
        String[] args = message.contains(" ") ? Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length) : new String[0];

        if (cmd.equalsIgnoreCase("/autoblock")) {
            e.setCancelled(true);
            List<String> auto = PickupOre.getAutoBlockToggle();
            if (auto.contains(p.getName())) {
                auto.remove(p.getName());
                p.sendMessage(ChatColor.GOLD + "Opção de compactar blocos automáticamente ativada.");
            } else {
                auto.add(p.getName());
                p.sendMessage(ChatColor.GOLD + "Opção de compactar blocos automáticamente desativada.");
            }
            return;
        }

        if (cmd.equalsIgnoreCase("/autovender")) {
            e.setCancelled(true);
            if (args.length == 0) {
                Map<String, Long> cool = PickupOre.getCooldown();
                if (cool.containsKey(p.getName())) {
                    long expected = TimeUnit.SECONDS.toMillis(3);
                    long elapsed = System.currentTimeMillis() - cool.get(p.getName());
                    if (elapsed >= expected) {
                        cool.remove(p.getName());
                    } else {
                        p.sendMessage(ChatColor.GOLD + "Aguarde " + ((expected - elapsed) / 1000) + " segundos para vender novamente.");
                        return;
                    }
                }

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

                cool.put(p.getName(), System.currentTimeMillis());
                p.sendMessage(ChatColor.GOLD + "Vendeu " + amount.intValue() + " itens por R$ " + price.intValue() + ".");
            } else {
                if (args[0].equalsIgnoreCase("toggle")) {
                    List<String> list = PickupOre.getAutoSellToggle();
                    if (list.contains(p.getName())) {
                        list.remove(p.getName());
                        p.sendMessage(ChatColor.GOLD + "Opção de vender automáticamente ativada.");
                    } else {
                        list.add(p.getName());
                        p.sendMessage(ChatColor.GOLD + "Opção de vender automáticamente deativada.");
                    }
                }
            }
        }
    }

}
