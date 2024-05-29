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

public class Dolus_Retract extends Task
{
    private Psychis plugin;
    private Player player;

    private int t = 0;

    public Dolus_Retract(Psychis _plugin, Player _player)
    {
        super(_plugin);
        plugin = _plugin;
        player = _player;
        tickThreshold = 1;
    }

    @Override
    public void run()
    {
        for(Object e : Psychis.getNearbyEntities(player.getLocation(), 8))
        {
            if(e instanceof Entity && player != e)
            {
                Entity entity = (Entity)e;
                //double distance = 8-Psychis.ClampD(player.getLocation().distance(entity.getLocation()), 0, 8);
                entity.setVelocity(entity.getLocation().clone().subtract(player.getLocation()).toVector().multiply(-0.1));
            }
        }

        if(t++ > 120)
            cancel = true;
    }

    @Override
    public void onCancel()
    {
        
    }
}