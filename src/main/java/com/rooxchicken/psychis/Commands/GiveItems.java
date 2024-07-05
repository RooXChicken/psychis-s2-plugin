package com.rooxchicken.psychis.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.rooxchicken.psychis.Psychis;
public class GiveItems implements CommandExecutor
{
    private Psychis plugin;

    public GiveItems(Psychis _plugin)
    {
        plugin = _plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!sender.isOp())
            return true;

        Player player = Bukkit.getPlayer(sender.getName());
        
        ItemStack agniAxe = new ItemStack(Material.NETHERITE_AXE);
        ItemMeta agniMeta = agniAxe.getItemMeta();
        agniMeta.setDisplayName("§x§F§F§8§0§2§2§lAgni Axe");
        agniMeta.setCustomModelData(1);
        agniMeta.addEnchant(Enchantment.DAMAGE_ALL, 3, true);
        agniMeta.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
        agniMeta.addEnchant(Enchantment.DURABILITY, 3, true);
        agniMeta.addEnchant(Enchantment.MENDING, 1, true);
        agniAxe.setItemMeta(agniMeta);

        player.getInventory().addItem(agniAxe);

        ItemStack charger = new ItemStack(Material.CROSSBOW);
        ItemMeta chargerMeta = charger.getItemMeta();
        chargerMeta.setDisplayName("§x§F§F§F§3§3§9§lCharger");
        chargerMeta.setCustomModelData(1);
        chargerMeta.addEnchant(Enchantment.MULTISHOT, 1, true);
        chargerMeta.addEnchant(Enchantment.MENDING, 1, true);
        chargerMeta.addEnchant(Enchantment.DURABILITY, 3, true);
        charger.setItemMeta(chargerMeta);

        player.getInventory().addItem(charger);

        ItemStack voidPiercer = new ItemStack(Material.NETHERITE_AXE);
        ItemMeta voidMeta = voidPiercer.getItemMeta();
        voidMeta.setDisplayName("§x§C§C§3§9§F§F§lVoid Piercer");
        voidMeta.setCustomModelData(2);
        voidMeta.addEnchant(Enchantment.DAMAGE_ALL, 3, true);
        voidMeta.addEnchant(Enchantment.DURABILITY, 3, true);
        voidMeta.addEnchant(Enchantment.MENDING, 1, true);
        voidPiercer.setItemMeta(voidMeta);

        player.getInventory().addItem(voidPiercer);

        ItemStack frostFang = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta frostMeta = frostFang.getItemMeta();
        frostMeta.setDisplayName("§x§2§B§D§8§F§F§lFrostfang");
        frostMeta.setCustomModelData(1);
        frostMeta.addEnchant(Enchantment.DAMAGE_ALL, 3, true);
        frostMeta.addEnchant(Enchantment.DURABILITY, 3, true);
        frostMeta.addEnchant(Enchantment.MENDING, 1, true);
        frostMeta.addEnchant(Enchantment.LOOT_BONUS_MOBS, 3, true);
        frostMeta.addEnchant(Enchantment.SWEEPING_EDGE, 3, true);
        frostFang.setItemMeta(frostMeta);

        player.getInventory().addItem(frostFang);

        ItemStack stormBringer = new ItemStack(Material.NETHERITE_SHOVEL);
        ItemMeta stormMeta = stormBringer.getItemMeta();
        stormMeta.setDisplayName("§x§D§9§E§E§F§3§lStormbringer");
        stormMeta.setCustomModelData(1);
        stormMeta.addEnchant(Enchantment.DAMAGE_ALL, 3, true);
        stormMeta.addEnchant(Enchantment.DURABILITY, 3, true);
        stormMeta.addEnchant(Enchantment.MENDING, 1, true);
        stormBringer.setItemMeta(stormMeta);

        player.getInventory().addItem(stormBringer);

        ItemStack tideShaper = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta tideMeta = tideShaper.getItemMeta();
        tideMeta.setDisplayName("§x§1§6§8§3§F§F§lTideshaper");
        tideMeta.setCustomModelData(2);
        tideMeta.addEnchant(Enchantment.DAMAGE_ALL, 3, true);
        tideMeta.addEnchant(Enchantment.DURABILITY, 3, true);
        tideMeta.addEnchant(Enchantment.MENDING, 1, true);
        tideMeta.addEnchant(Enchantment.LOOT_BONUS_MOBS, 3, true);
        tideMeta.addEnchant(Enchantment.SWEEPING_EDGE, 3, true);
        tideShaper.setItemMeta(tideMeta);

        player.getInventory().addItem(tideShaper);

        return true;
    }

}
