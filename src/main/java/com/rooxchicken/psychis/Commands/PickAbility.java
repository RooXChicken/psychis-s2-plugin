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
public class PickAbility implements CommandExecutor
{
    private Psychis plugin;

    public PickAbility(Psychis _plugin)
    {
        plugin = _plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        Player player = Bukkit.getPlayer(sender.getName());

        PersistentDataContainer data = player.getPersistentDataContainer();
        if(data.has(Psychis.abilityKey, PersistentDataType.INTEGER) && data.get(Psychis.abilityKey, PersistentDataType.INTEGER) != -1)
        {
            if(!sender.isOp())
                return true;
        }

        int ability = Integer.parseInt(args[0]);
        if(ability != 6)
            data.set(Psychis.abilityKey, PersistentDataType.INTEGER, ability);
        else
            return true;
        
        plugin.addPlayerAbility(player);

        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1, 1);

        return true;
    }

}
