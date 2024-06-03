package com.rooxchicken.psychis.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
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

public class Varuna extends Ability implements Listener
{
    private Psychis plugin;
    private Player player;

    private double ticks = 0;

    public Varuna(Psychis _plugin, Player _player)
    {
        super(_plugin, _player);
        plugin = _plugin;
        player = _player;
        type = 0;
    }

    @Override
    public void passive()
    {
        player.addPotionEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, 21, 0, true));
    }

    @Override
    public void activateFirstAbility(int state)
    {
        
    }
    
    @Override
    public void activateSecondAbility(int state)
    {
        if(!plugin.secondUnlocked(player))
            return;
    }
}
