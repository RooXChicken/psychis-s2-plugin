package com.rooxchicken.psychis.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
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
import com.rooxchicken.psychis.Tasks.Midas_Jackpot;

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

        cooldown1 = 90;
        cooldown2 = 180;

        name = "Midas";
    }

    @Override
    public void passive()
    {
        
    }

    @Override
    public void activateFirstAbility(int state)
    {
        if(state == 0)
        {
            if(!plugin.setCooldown(player, cooldown1, Psychis.ability1CooldownKey))
                return;

            Psychis.tasks.add(new Midas_Jackpot(plugin, player, this));
        }
    }
    
    @Override
    public void activateSecondAbility(int state)
    {
        if(state == 0)
        {
            if(!plugin.setCooldown(player, cooldown2, Psychis.ability2CooldownKey))
                return;

            Midas_Greed greed = new Midas_Greed(plugin, player);
            Bukkit.getPluginManager().registerEvents(greed, plugin);
            Psychis.tasks.add(greed);
        }
    }

    @Override
    public void secondAbilityUnlock()
    {
        if(plugin.secondUnlocked(player))
            return;

        PersistentDataContainer data = player.getPersistentDataContainer();
        data.set(Psychis.secondUnlockedKey, PersistentDataType.BOOLEAN, true);
        Psychis.sendPlayerData(player, "0_" + data.get(Psychis.abilityKey, PersistentDataType.INTEGER) + "_" + data.get(Psychis.secondUnlockedKey, PersistentDataType.BOOLEAN));
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
