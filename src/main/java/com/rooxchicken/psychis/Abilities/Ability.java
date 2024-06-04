package com.rooxchicken.psychis.Abilities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.rooxchicken.psychis.Psychis;

public abstract class Ability implements Listener
{
    private Psychis plugin;
    public String name = "null";
    private Player player;
    public int type = -1;

    public int cooldown1 = -1;
    public int cooldown2 = -1;
    
    public boolean deadly = false;

    public Ability(Psychis _plugin, Player _player) { plugin = _plugin; player = _player; Bukkit.getServer().getPluginManager().registerEvents(this, plugin);}

    public void passive() {}
    public void activateFirstAbility(int state) {}
    public void activateSecondAbility(int state) {}

    public void secondAbilityUnlock() {}
}
