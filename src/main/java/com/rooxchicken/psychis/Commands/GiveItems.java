package com.rooxchicken.psychis.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

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
        
        Bukkit.dispatchCommand(sender, "give " + sender.getName() + " compass{display:{Name:'{\"text\":\"Choice Compass\",\"color\":\"white\",\"bold\":true,\"italic\":true}',Lore:['{\"text\":\"Allows you to reroll your variant\"}']},Enchantments:[{}]} 1");

        return true;
    }

}
