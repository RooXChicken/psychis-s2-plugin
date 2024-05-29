package com.rooxchicken.psychis.Abilities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.rooxchicken.psychis.Psychis;

public abstract class Ability implements Listener
{
    private Psychis plugin;
    private Player player;

    public Ability(Psychis _plugin, Player _player) { plugin = _plugin; player = _player; Bukkit.getServer().getPluginManager().registerEvents(this, plugin);}

    public void passive() {}
    public void activateFirstAbility(int state) {}
    public void activateSecondAbility(int state) {}
}
