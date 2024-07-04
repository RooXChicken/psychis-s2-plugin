package com.rooxchicken.psychis.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
        agniMeta.setDisplayName("§xF§F§8§0§2§2§lAgni Axe");
        agniAxe.setItemMeta(agniMeta);

        player.getInventory().addItem(agniAxe);

        return true;
    }

}
