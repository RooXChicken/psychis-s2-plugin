package com.rooxchicken.psychis.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.rooxchicken.psychis.Psychis;
public class ParticleTest implements CommandExecutor
{
    private Psychis plugin;

    public ParticleTest(Psychis _plugin)
    {
        plugin = _plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        switch(args[0])
        {
            case "sphere":
                //plugin.tasks.add(new ParticleSphere(plugin, Bukkit.getPlayer(sender.getName())));
                break;
        }

        return true;
    }

}
