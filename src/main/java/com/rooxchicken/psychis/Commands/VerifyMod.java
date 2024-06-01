package com.rooxchicken.psychis.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.rooxchicken.psychis.Psychis;
public class VerifyMod implements CommandExecutor
{
    private Psychis plugin;

    public VerifyMod(Psychis _plugin)
    {
        plugin = _plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        plugin.verifyMod(Bukkit.getPlayer(sender.getName()));

        return true;
    }

}
