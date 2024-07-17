package com.rooxchicken.psychis.Abilities;

import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
        name = "Agni";

        cooldown1 = 30;
        cooldown2 = 60;
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
        if(!plugin.secondUnlocked(player))
            return;
        if(ticks == -1)
            if(!plugin.setCooldown(player, cooldown2, Psychis.ability2CooldownKey))
                return;

        double dmg = ticks/6.0;
        if(dmg > 12)
            dmg = 16;
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

    @EventHandler
    public void checkGhast(EntityPortalEnterEvent event)
    {
        if(plugin.secondUnlocked(player))
            return;

        if(event.getEntity() == player)
        {
            if(player.getWorld().getEnvironment() == Environment.NORMAL)
                for(Object o : Psychis.getNearbyEntities(player.getLocation(), 40))
                {
                    if(((Entity)o).getType() == EntityType.GHAST)
                    {
                        plugin.unlockSecondAbility(player);
                        ((LivingEntity)o).damage(100);
                        return;
                    }
                }
        }
    }
}
