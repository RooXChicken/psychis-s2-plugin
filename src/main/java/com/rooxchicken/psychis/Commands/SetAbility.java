package com.rooxchicken.psychis.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import com.rooxchicken.psychis.Psychis;
public class SetAbility implements CommandExecutor
{
    private Psychis plugin;

    public SetAbility(Psychis _plugin)
    {
        plugin = _plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!sender.isOp())
            return false;

        Player player = Bukkit.getPlayer(sender.getName());
        player.getPersistentDataContainer().set(Psychis.abilityKey, PersistentDataType.INTEGER, Integer.parseInt(args[0]));
        plugin.addPlayerAbility(player);

        return true;
    }

}
