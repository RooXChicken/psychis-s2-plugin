package com.rooxchicken.psychis.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.rooxchicken.psychis.Psychis;

public class Tideshaper_GroundPound extends Task
{
    private Player player;
    private int t;
    private int tt;

    private double[] cacheX;
    private double[] cacheZ;

    public Tideshaper_GroundPound(Psychis _plugin, Player _player)
    {
        super(_plugin);
        player = _player;
        tickThreshold = 1;

        cacheX = new double[90];
        cacheZ = new double[90];

        for(int i = 0; i < 90; i++)
        {
            double rad = Math.toRadians(i*4);
            cacheX[i] = Math.sin(rad);
            cacheZ[i] = Math.cos(rad);
        }

        player.getWorld().spawnParticle(Particle.WATER_SPLASH, player.getLocation().clone().add(0,1,0), 100, 0.6, 0.8, 0.6);
    }

    @Override
    public void run()
    {
        player.getWorld().spawnParticle(Particle.WATER_SPLASH, player.getLocation().clone().add(0,1,0), 3, 0, 0, 0);

        if(++tt < 7 || !player.isOnGround())
            return;

        if(t % 2 == 0)
            player.getWorld().playSound(player.getLocation(), Sound.ITEM_BUCKET_EMPTY, 1, 1);
        
        for(int i = 0; i < 90; i++)
        {
            Location particlePos = player.getLocation().clone();
            
            double xOffset = cacheX[i] * t/3.0;
            double zOffset = cacheZ[i] * t/3.0;

            //player.getWorld().spawnParticle(Particle.REDSTONE, particlePos.add(xOffset, 0, zOffset), 1, 0, 0, 0, new Particle.DustOptions(Color.BLUE, 1f));
            player.getWorld().spawnParticle(Particle.WATER_SPLASH, particlePos.add(xOffset, 1, zOffset), 3, 0, 0, 0);
        }

        for(Object o : Psychis.getNearbyEntities(player.getLocation(), t/3))
        {
            if(o instanceof LivingEntity && !o.equals(player) && !(o instanceof Villager))
            {
                LivingEntity entity = (LivingEntity)o;
                entity.setVelocity(new Vector(0,1,0));
                entity.damage(12);
            }
        }

        if(++t > 10)
            cancel = true;
    }
}
