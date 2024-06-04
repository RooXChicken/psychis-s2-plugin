package com.rooxchicken.psychis.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
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
import com.rooxchicken.psychis.Tasks.Enil_Polarity;

public class Enil extends Ability implements Listener
{
    private Psychis plugin;
    private Player player;

    private double ticks = 0;
    private double damage = 0;
    private boolean charging = false;

    public Enil(Psychis _plugin, Player _player)
    {
        super(_plugin, _player);
        plugin = _plugin;
        player = _player;

        type = 2;
        name = "Enil";
        
        cooldown1 = 45;
        cooldown2 = 120;
    }

    @Override
    public void passive()
    {
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 21, 1));

        if(charging)
            player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, player.getEyeLocation().subtract(0, 1, 0), 1, 0, 1, 0);
    }

    @Override
    public void activateFirstAbility(int state)
    {
        if(state == 0)
        {
            if(!plugin.setCooldown(player, cooldown1, Psychis.ability1CooldownKey))
                return;
            Entity entity = plugin.getTarget(player, 50);
            deadly = true;
            if(entity != null)
            {
                if(entity instanceof LivingEntity)
                    ((LivingEntity)entity).damage(8, player);
                entity.getWorld().strikeLightning(entity.getLocation());
                if(entity instanceof Player)
                {
                    ((Player)entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 80, 2));
                    ((Player)entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 2));
                }

                if(!plugin.secondUnlocked(player))
                {
                    if(entity instanceof Creeper)
                    {
                        if(player.getWorld().isThundering())
                            plugin.unlockSecondAbility(player);
                    }
                }
            }
            else
            {
                Block block = plugin.getBlock(player, 50);
                if(block != null)
                    player.getWorld().strikeLightning(block.getLocation());
            }
        }
        if(state == 1)
            deadly = false;
    }
    
    @Override
    public void activateSecondAbility(int state)
    {
        if(!plugin.secondUnlocked(player))
            return;
        if(!plugin.setCooldown(player, cooldown2, Psychis.ability2CooldownKey))
            return;

        charging = true;
    }

    @EventHandler
    public void entityDamage(EntityDamageByEntityEvent event)
    {
        if(!charging)
            return;

        if(event.getDamager() == player)
        {
            damage += event.getDamage();
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.2f, 1);
            player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, event.getEntity().getLocation().clone().add(0, 1, 0), 150, 0, 1, 0);
        }

        if(damage > 100)
        {
            damage = 0;
            Psychis.tasks.add(new Enil_Polarity(plugin, player, this));
            charging = false;
        }
    }
}
