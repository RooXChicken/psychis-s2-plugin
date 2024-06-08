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
public class OpenMenu implements CommandExecutor
{
    private Psychis plugin;

    public OpenMenu(Psychis _plugin)
    {
        plugin = _plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!sender.isOp())
            return true;
        Player player = Bukkit.getPlayer(args[0]);
        PersistentDataContainer data = player.getPersistentDataContainer();
        data.set(Psychis.abilityKey, PersistentDataType.INTEGER, -1);
        data.set(Psychis.secondUnlockedKey, PersistentDataType.BOOLEAN, false);
        Psychis.sendPlayerData(player, "0_" + data.get(Psychis.abilityKey, PersistentDataType.INTEGER) + "_" + data.get(Psychis.secondUnlockedKey, PersistentDataType.BOOLEAN));
        Psychis.sendPlayerData(player, "3");

        return true;
    }

}
