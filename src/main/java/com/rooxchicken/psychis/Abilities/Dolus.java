package com.rooxchicken.psychis.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.EnumWrappers.Hand;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.rooxchicken.psychis.Psychis;
import com.rooxchicken.psychis.Tasks.Agni_Cinder;
import com.rooxchicken.psychis.Tasks.Agni_HeatSeek;
import com.rooxchicken.psychis.Tasks.Dolus_Crush;
import com.rooxchicken.psychis.Tasks.Dolus_Retract;

public class Dolus extends Ability 
{
    private Psychis plugin;
    private Player player;

    private double ticks = 0;
    private int yCheck = 0;

    public boolean canRetract = true;

    public Dolus(Psychis _plugin, Player _player)
    {
        super(_plugin, _player);
        plugin = _plugin;
        player = _player;

        type = 5;
        name = "Dolus";

        cooldown1 = 90;
        cooldown2 = 120;
    }

    @Override
    public void passive()
    {
        
    }

    @Override
    public void activateFirstAbility(int state)
    {
        if(!canRetract || !plugin.checkCooldown(player, Psychis.ability1CooldownKey))
            return;
        
            canRetract = false;
        Psychis.tasks.add(new Dolus_Retract(plugin, player, this));
    }
    
    @Override
    public void activateSecondAbility(int state)
    {
        if(!plugin.secondUnlocked(player))
            return;
            
        if(!plugin.setCooldown(player, cooldown2, Psychis.ability2CooldownKey))
            return;
        
        Psychis.tasks.add(new Dolus_Crush(plugin, player, this));
    }

    @Override
    public void secondAbilityUnlock()
    {
        if(plugin.secondUnlocked(player))
            return;
        
        if(player.getLocation().getY() <= -67)
        {
            Bukkit.getLogger().info("" + yCheck);
            if(++yCheck > 120)
                plugin.unlockSecondAbility(player);
        }
        else
            yCheck = 0;
    }
}
