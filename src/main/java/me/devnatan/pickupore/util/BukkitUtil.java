package me.devnatan.pickupore.util;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2018 DevNatan. Todos os direitos reservados.
 * @author DevNatan
 * @version 1.0
 */
public class BukkitUtil {

    public static final ImmutableMap<ItemStack, Material> autoSmeltMap = ImmutableMap.<ItemStack, Material>builder()
            .put(new ItemStack(Material.INK_SACK, 1, (short) 4), Material.LAPIS_BLOCK)
            .put(new ItemStack(Material.REDSTONE), Material.REDSTONE_BLOCK)
            .put(new ItemStack(Material.DIAMOND), Material.DIAMOND_BLOCK)
            .build();

    /**
     * Converte 9 itens em um inventário para outro material
     * @param inventory = um inventário
     * @return = quantidade de blocos convertido
     */
    public static int autoSmelt(Inventory inventory) {
        List<ItemStack> insert = new ArrayList<>();

        for(int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if(item != null && item.getType() != Material.AIR) {
                int finalI = i;
                autoSmeltMap.keySet().stream()
                        .filter(stack -> stack.getType() == item.getType() && stack.getDurability() == item.getDurability())
                        .forEach(stack -> {
                            Material result = autoSmeltMap.get(stack);
                            int itemAmount = item.getAmount();

                            while(itemAmount > 9) {
                                itemAmount -= 9;

                                if(itemAmount <= 1) {
                                    inventory.clear(finalI);
                                    break;
                                } else {
                                    insert.add(new ItemStack(result));
                                    item.setAmount(itemAmount);
                                }
                            }
                        });
            }
        }

        for(ItemStack itemStack : insert) {
            inventory.addItem(itemStack);
        }

        return insert.size();
    }

    /**
     * Obtem o valor total dos itens de um inventário através
     * de um preço fixo que é multiplicado pela quantidade do mesmo.
     * @param inventory = um inventário
     * @param type = tipo do item
     * @param price = preço do item
     * @retur quantidade e o valor total
     */
    public static int[] getPriceAndRemove(Inventory inventory, Material type, int price) {
        int amount = 0;

        for(int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if(item != null && item.getType() == type) {
                amount += item.getAmount();
                inventory.clear(i);
            }
        }

        return new int[] { amount, price * amount };
    }

    /**
     * Formatar a quantidade de dinheiro para um tamanho menor.
     * @param count = quantidade de dinheiro
     * @param large = formatar para 'milhões' em vez de 'm'
     * @return dinheiro formatado
     */
    public static String format(long count, boolean large) {
        if (count < 1000) return String.valueOf(count);
        int exp = (int) (Math.log(count) / Math.log(1000));
        char ch = "kMBTQ".charAt(exp - 1);
        double val = count / Math.pow(1000, exp);
        String string = "???";

        if(large) {
            switch (ch) {
                case 'k':
                    string = val + " mil";
                    break;
                case 'M':
                    string = val + " milhões";
                    break;
                case 'B':
                    string = val + " bilhões";
                    break;
                case 'Q':
                    string = val + " quadrilhões";
                    break;
            }
        }

        return large ? String.format("%.1f%s", val, string) : String.format("%.1f%s", val, ch);
    }

}
