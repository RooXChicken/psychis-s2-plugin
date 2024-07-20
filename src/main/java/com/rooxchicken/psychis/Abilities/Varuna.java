package com.rooxchicken.psychis.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Conduit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent.Cause;
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
import com.rooxchicken.psychis.Tasks.Varuna_Typhoon;
import com.rooxchicken.psychis.Tasks.Varuna_WaterJet;

public class Varuna extends Ability
{
    private Psychis plugin;
    private Player player;

    private double ticks = 0;
    private Block condiut;

    public Varuna(Psychis _plugin, Player _player)
    {
        super(_plugin, _player);
        plugin = _plugin;
        player = _player;
        type = 0;

        cooldown1 = 90;
        cooldown2 = 60;

        name = "Varuna";
    }

    @Override
    public void passive()
    {
        player.addPotionEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, 21, 0, true));
    }

    @Override
    public void activateFirstAbility(int state)
    {
        if(state == 0)
        {
            if(!plugin.setCooldown(player, cooldown1, Psychis.ability1CooldownKey))
                return;

            Psychis.tasks.add(new Varuna_Typhoon(plugin, player));
        }
    }
    
    @Override
    public void activateSecondAbility(int state)
    {
        if(!plugin.secondUnlocked(player))
            return;

        if(!plugin.setCooldown(player, cooldown2, Psychis.ability2CooldownKey))
            return;

        if(state == 0)
        {
            Psychis.tasks.add(new Varuna_WaterJet(plugin, player, this));
        }
    }

    @Override
    public void secondAbilityUnlock()
    {
        if(condiut != null && condiut.getType() == Material.CONDUIT)
        {
            if(player.hasPotionEffect(PotionEffectType.CONDUIT_POWER))
            {
                if(player.getPotionEffect(PotionEffectType.CONDUIT_POWER).getDuration() > 21)
                {
                    plugin.unlockSecondAbility(player);
                    condiut.setType(Material.WATER);
                }
            }
        }
    }

    @EventHandler
    public void checkConduit(BlockPlaceEvent event)
    {
        if(event.getPlayer() != player)
            return;

        if(plugin.secondUnlocked(player))
            return;

        Block block = event.getBlock();
        if(block.getType() == Material.CONDUIT)
            condiut = block;
        else
            return;
    }
}
