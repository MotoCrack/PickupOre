package me.devnatan.pickupore;

import me.devnatan.pickupore.autosell.AutoItem;
import me.devnatan.pickupore.listener.BlockBreakListener;
import me.devnatan.pickupore.listener.EntityDeathListener;
import me.devnatan.pickupore.listener.InventoryClickListener;
import me.devnatan.pickupore.listener.PlayerCommandListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2018 DevNatan. Todos os direitos reservados.
 * @author DevNatan
 * @version 1.0
 */
public class PickupOre extends JavaPlugin {

    private static Economy economy;
    private static final List<AutoItem> autoSell = new ArrayList<>();
    private static final Map<String, Long> cooldown = new HashMap<>();
    private static final List<String> autoSellToggle = new ArrayList<>();
    private static final List<String> autoBlockToggle = new ArrayList<>();

    public void onEnable() {
        if(!Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            getLogger().severe("Falha ao encontrar o plugin Vault");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if(rsp != null) {
            economy = rsp.getProvider();
        }

        if(economy == null) {
            getLogger().severe("Falha ao encontrar provedor de economia.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if(!getDataFolder().exists()) getDataFolder().mkdir();
        if(!new File(getDataFolder(), "config.yml").exists()) {
            saveResource("config.yml", false);
        }

        ConfigurationSection cs = getConfig().getConfigurationSection("autosell");
        cs.getKeys(false).forEach(s -> {
            ConfigurationSection sec = cs.getConfigurationSection(s);
            ItemStack is = new ItemStack(Material.BARRIER);

            try {
                is.setType(Material.getMaterial(sec.getInt("id")));
            } catch (NumberFormatException e) {
                is.setType(Material.getMaterial(sec.getString("id")));
            }

            Integer price = sec.contains("price") ? sec.getInt("price") : 0;
            Integer slot = sec.contains("slot") ? sec.getInt("slot") : autoSell.size() + 1;
            autoSell.add(new AutoItem(is, slot, price));
        });

        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerCommandListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDeathListener(), this);
    }

    /**
     * Retorna a economia providenciada pelo plugin "Vault".
     * É obrigatório ter o plugin e um provedor de economia.
     * @return Eoonomy
     */
    public static Economy getEconomy() {
        return economy;
    }

    /**
     * Lista de itens que podem ser vendidos automáticamente.
     * @return List
     */
    public static List<AutoItem> getAutoSell() {
        return autoSell;
    }

    /**
     * Cooldown para usar o comando "/autovender".
     * @return Map
     */
    public static Map<String, Long> getCooldown() {
        return cooldown;
    }

    /**
     * Lista de de quem usará a venda automática.
     * @return List
     */
    public static List<String> getAutoSellToggle() {
        return autoSellToggle;
    }

    /**
     * Lista de quem usará a conversão automática de blocos.
     * @return List
     */
    public static List<String> getAutoBlockToggle() {
        return autoBlockToggle;
    }
}
