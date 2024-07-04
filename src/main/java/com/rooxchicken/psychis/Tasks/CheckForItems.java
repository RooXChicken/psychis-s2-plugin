package com.rooxchicken.psychis.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.rooxchicken.psychis.Psychis;
import com.rooxchicken.psychis.Weapons.AgniAxe;
import com.rooxchicken.psychis.Weapons.Stormbringer;

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
                }
            }
        }
    }
}
