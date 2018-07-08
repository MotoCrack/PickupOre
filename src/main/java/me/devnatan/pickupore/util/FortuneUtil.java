package me.devnatan.pickupore.util;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Copyright (c) 2018 DevNatan. Todos os direitos reservados.
 * @author DevNatan
 * @version 1.0
 */
public class FortuneUtil {

    /**
     * Falso simulador de multiplicador de drops com fortuna.
     * @param level = nível da fortuna
     * @param current = quantidade de drops atual
     * @return List
     */
    public static List<ItemStack> getDrops(int level, Collection<ItemStack> current) {
        if(level == 0) return new ArrayList<>(current);

        List<ItemStack> drops = new ArrayList<>();
        current.forEach(item -> {
                    item.setAmount(item.getAmount() * (level + 3));
                    drops.add(item);
                });

        return drops;
    }

}
