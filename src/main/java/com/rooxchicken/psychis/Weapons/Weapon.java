package com.rooxchicken.psychis.Weapons;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.rooxchicken.psychis.Psychis;

public abstract class Weapon implements Listener
{
    private Psychis plugin;
    public String itemName = "null";
    private Player player;
    public int type = -1;

    public Weapon(Psychis _plugin, Player _player) { plugin = _plugin; player = _player; Bukkit.getServer().getPluginManager().registerEvents(this, plugin);}

    public void passive() {}
    public void activateFirstAbility(int state) {}
    public void activateSecondAbility(int state) {}

    public void secondAbilityUnlock() {}
}
