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
public class SelectAbility implements CommandExecutor
{
    private Psychis plugin;

    public SelectAbility(Psychis _plugin)
    {
        plugin = _plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        Player player = Bukkit.getPlayer(sender.getName());

        PersistentDataContainer data = player.getPersistentDataContainer();
        if(data.has(Psychis.abilityKey, PersistentDataType.INTEGER))
        {
            if(sender.isOp())
                sender.sendMessage("This menu was force opened because you are opped. This menu would not have opened if you weren't op.");
            else
                return true;
        }

        Psychis.sendPlayerData(player, "3");

        return true;
    }

}
