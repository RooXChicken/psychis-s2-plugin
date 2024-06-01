package com.rooxchicken.psychis.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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

public class Agni extends Ability 
{
    private Psychis plugin;
    private Player player;

    private double ticks = -1;

    public Agni(Psychis _plugin, Player _player)
    {
        super(_plugin, _player);
        plugin = _plugin;
        player = _player;

        type = 1;

        cooldown1 = 30;
        cooldown2 = 90;
    }

    @Override
    public void passive()
    {
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 21, 0));
    }

    @Override
    public void activateFirstAbility(int state)
    {
        if(state == 0)
        {
            if(!plugin.setCooldown(player, cooldown1, Psychis.ability1CooldownKey))
                return;
            plugin.tasks.add(new Agni_HeatSeek(plugin, player));
        }
    }
    
    @Override
    public void activateSecondAbility(int state)
    {
        if(ticks == -1)
            if(!plugin.setCooldown(player, cooldown2, Psychis.ability2CooldownKey))
                return;

        double dmg = ticks/6.0;
        if(player.getFireTicks() > 0 || player.getWorld().getEnvironment() == Environment.NETHER)
            dmg *= 1.5;
        switch(state)
        {
            case 0:
                ticks = 0;
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
                break;
            case 1:
                if(ticks == -1)
                    return;
                ticks += Math.pow(0.9, ticks);
                Agni_Cinder.constructArrow(player.getEyeLocation(), dmg, player);
                Agni_Cinder.glow(player);

                if(ticks % 5 == 0)
                {
                }
                break;
            case 2:
                if(ticks == -1)
                    return;
                plugin.tasks.add(new Agni_Cinder(plugin, player, this, dmg));
                Agni_Cinder.stopGlow(player);
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
                ticks = -1;
                break;
        }
    }
}
