package com.rooxchicken.psychis.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.rooxchicken.psychis.Psychis;
public class FirstAbility implements CommandExecutor
{
    private Psychis plugin;
    private int state = -1;

    public FirstAbility(Psychis _plugin, int _state)
    {
        plugin = _plugin;
        state = _state;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        plugin.getPlayerAbility(Bukkit.getPlayer(sender.getName())).activateFirstAbility(state);

        return true;
    }

}
