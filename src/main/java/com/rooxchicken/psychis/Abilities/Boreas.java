package com.rooxchicken.psychis.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
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
import com.rooxchicken.psychis.Tasks.Boreas_Vortex;

public class Boreas extends Ability implements Listener
{
    private Psychis plugin;
    private Player player;

    private double ticks = 0;
    private boolean jump;
    private boolean jumpEffect;

    private Boreas_Vortex vortex;
    private boolean launched = false;

    public Boreas(Psychis _plugin, Player _player)
    {
        super(_plugin, _player);
        plugin = _plugin;
        player = _player;

        type = 3;

        cooldown1 = 30;
        cooldown2 = 60;
    }

    @Override
    public void passive()
    {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 21, 1, true));
        if(jumpEffect)
        {
            player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 5, 0.05, 0.05, 0.05, new Particle.DustOptions(Color.WHITE, 1f));
            if(player.getVelocity().getY() < 0)
                jumpEffect = false;
        }
    }

    @Override
    public void activateFirstAbility(int state)
    {
        if(state == 0)
        {
            if(!plugin.setCooldown(player, cooldown1, Psychis.ability1CooldownKey))
                return;
            player.setVelocity(player.getLocation().getDirection().multiply(-2.1));
            player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 100, 0.5f, 0.2f, 0.5f, new Particle.DustOptions(Color.WHITE, 1f));
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BIG_DRIPLEAF_FALL, 1, 1);
            jump = true;
            jumpEffect = true;
        }
    }
    
    @Override
    public void activateSecondAbility(int state)
    {
        if(!plugin.secondUnlocked(player))
            return;
        if(vortex != null && vortex.cancel)
            vortex = null;
        
        if(state == 0)
        {
            if(!plugin.setCooldown(player, cooldown2, Psychis.ability2CooldownKey))
                return;

            vortex = new Boreas_Vortex(plugin, player);
            Psychis.tasks.add(vortex);
            launched = false;
        }
        if(launched)
            return;
        
        if(vortex != null && state == 1)
            vortex.setPosition();
        else if(vortex != null && state == 2)
        {
            vortex.resetTimer();
            launched = true;
        }
    }

    @EventHandler
    public void cancelFallIfBoots(EntityDamageEvent event)
    {
        if(event.getCause() != DamageCause.FALL || event.getEntityType() != EntityType.PLAYER)
            return;

        if(jump)
        {
            jump = false;
            jumpEffect = false;
            event.setCancelled(true);
        }
    }
}
