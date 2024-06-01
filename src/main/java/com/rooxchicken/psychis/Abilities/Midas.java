package com.rooxchicken.psychis.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
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
import com.rooxchicken.psychis.Tasks.Midas_Greed;

public class Midas extends Ability
{
    private Psychis plugin;
    private Player player;

    private double ticks = 0;

    public Midas(Psychis _plugin, Player _player)
    {
        super(_plugin, _player);
        plugin = _plugin;
        player = _player;
        type = 6;
    }

    @Override
    public void passive()
    {
        
    }

    @Override
    public void activateFirstAbility(int state)
    {
        
    }
    
    @Override
    public void activateSecondAbility(int state)
    {
        if(state == 0)
        {
            if(!plugin.setCooldown(player, cooldown1, Psychis.ability1CooldownKey))
                return;

            Psychis.tasks.add(new Midas_Greed(plugin, player));
        }
    }

    @EventHandler
    public void addExtraAbsorption(PlayerItemConsumeEvent event)
    {
        Player _player = event.getPlayer();
        if(_player != player)
            return;

        ItemStack item = event.getItem();

        if(item.getType() == Material.GOLDEN_APPLE)
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 1));
        }
    }
}
