package com.rooxchicken.psychis.Tasks;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.rooxchicken.psychis.Psychis;
import com.rooxchicken.psychis.Abilities.Dolus;

public class Dolus_Retract extends Task
{
    private Psychis plugin;
    private Player player;
    private Dolus dolus;

    private int t = 0;

    public Dolus_Retract(Psychis _plugin, Player _player, Dolus _dolus)
    {
        super(_plugin);
        plugin = _plugin;
        player = _player;
        dolus = _dolus;
        tickThreshold = 1;



        //dolus.deadly = true;
    }

    @Override
    public void run()
    {
        //if(t % 4 == 0)
            Psychis.tasks.add(new Dolus_RetractParticle(plugin, player.getLocation(), player));
        for(Object e : Psychis.getNearbyEntities(player.getLocation(), 8))
        {
            if(e instanceof Entity && player != e)
            {
                Entity entity = (Entity)e;
                //double distance = 8-Psychis.ClampD(player.getLocation().distance(entity.getLocation()), 0, 8);
                if(e instanceof Player)
                    ((Player)e).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 10, 1));
                entity.setVelocity(entity.getLocation().clone().subtract(player.getLocation()).toVector().multiply(-0.1));
                player.getWorld().spawnParticle(Particle.REDSTONE, ((Entity)e).getLocation(), 3, 0, 0, 0, new Particle.DustOptions(Color.PURPLE, 1f));
            }
        }

        if(t++ > 120)
            cancel = true;
    }

    @Override
    public void onCancel()
    {
        dolus.deadly = false;
    }
}