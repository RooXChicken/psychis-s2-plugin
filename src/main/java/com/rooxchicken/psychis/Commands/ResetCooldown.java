package com.rooxchicken.psychis.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.rooxchicken.psychis.Psychis;
public class ResetCooldown implements CommandExecutor
{
    private Psychis plugin;

    public ResetCooldown(Psychis _plugin)
    {
        plugin = _plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!sender.isOp())
            return false;

        plugin.setCooldownForce(Bukkit.getPlayer(sender.getName()), 0, Psychis.ability1CooldownKey);
        plugin.setCooldownForce(Bukkit.getPlayer(sender.getName()), 0, Psychis.ability2CooldownKey);

        return true;
    }

}
