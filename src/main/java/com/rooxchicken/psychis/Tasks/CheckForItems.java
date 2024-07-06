package com.rooxchicken.psychis.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.rooxchicken.psychis.Psychis;
import com.rooxchicken.psychis.Weapons.AgniAxe;
import com.rooxchicken.psychis.Weapons.Charger;
import com.rooxchicken.psychis.Weapons.Frostfang;
import com.rooxchicken.psychis.Weapons.Stormbringer;
import com.rooxchicken.psychis.Weapons.Tideshaper;
import com.rooxchicken.psychis.Weapons.VoidPiercer;

public class CheckForItems extends Task
{
    private Psychis plugin;

    public CheckForItems(Psychis _plugin)
    {
        super(_plugin);
        plugin = _plugin;

        tickThreshold = 20;
    }

    @Override
    public void run()
    {
        Stormbringer.players.clear();
        AgniAxe.players.clear();
        VoidPiercer.players.clear();
        Charger.players.clear();
        Frostfang.players.clear();
        Tideshaper.players.clear();
        for(Player player : Bukkit.getOnlinePlayers())
        {
            for(ItemStack item : player.getInventory())
            {
                if(item != null && item.hasItemMeta())
                {
                    if(item.getItemMeta().getDisplayName().equals(plugin.stormbringer.itemName))
                        Stormbringer.addPlayer(player);
                    if(item.getItemMeta().getDisplayName().equals(plugin.agniAxe.itemName))
                        AgniAxe.addPlayer(player);
                    if(item.getItemMeta().getDisplayName().equals(plugin.voidPiercer.itemName))
                        VoidPiercer.addPlayer(player);
                    if(item.getItemMeta().getDisplayName().equals(plugin.charger.itemName))
                        Charger.addPlayer(player);
                    if(item.getItemMeta().getDisplayName().equals(plugin.frostfang.itemName))
                        Frostfang.addPlayer(player);
                    if(item.getItemMeta().getDisplayName().equals(plugin.tideshaper.itemName))
                        Tideshaper.addPlayer(player);
                }
            }
        }
    }
}
