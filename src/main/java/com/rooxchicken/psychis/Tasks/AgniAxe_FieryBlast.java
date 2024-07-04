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
import org.bukkit.entity.Villager;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.rooxchicken.psychis.Psychis;
import com.rooxchicken.psychis.Abilities.Enil;

public class AgniAxe_FieryBlast extends Task
{
    private Psychis plugin;
    private Player player;
    private Location start;

    private ArrayList<LivingEntity> blazed;

    private int t = 0;
    private int ticks = 0;

    private double size = 0.8;
    private int count = 30;

    private double[] cacheX;
    private double[] cacheZ;

    private double[] cacheYX;
    private double[] cacheYZ;

    private Color[] colors;

    public AgniAxe_FieryBlast(Psychis _plugin, Player _player)
    {
        super(_plugin);
        plugin = _plugin;
        player = _player;
        start = player.getLocation().clone();
        tickThreshold = 1;

        cacheX = new double[count];
        cacheZ = new double[count];

        cacheYX = new double[count];
        cacheYZ = new double[count];

        for(int i = 0; i < count; i++)
        {
            cacheX[i] = -1;
            cacheZ[i] = -1;

            cacheYX[i] = -1;
            cacheYZ[i] = -1;
        }

        colors = new Color[] {Color.ORANGE, Color.RED, Color.ORANGE};
        blazed = new ArrayList<LivingEntity>();
        player.getWorld().playSound(start, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
        player.getWorld().playSound(start, Sound.BLOCK_FIRE_AMBIENT, 1, 1);
    }

    @Override
    public void run()
    {
        if(t % 5 == 0)
            player.getWorld().playSound(start, Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
        for(Object e : Psychis.getNearbyEntities(start, (int)Math.ceil(size)))
        {
            if(e instanceof LivingEntity && player != e && !(e instanceof Villager))
            {
                if(!blazed.contains(e))
                {
                    LivingEntity entity = (LivingEntity)e;
                    entity.damage(14, player);
                    entity.setFireTicks(200);
                    blazed.add(entity);
                }
            }
        }
        size += 0.3;
        double offset = 0.02;
        for(int i = 0; i < count; i++)
        {
            double sphereOffset = 0;
            double sphereOffsetXZ = 0;
            if(cacheYX[i] == -1)
            {
                cacheYX[i] = Math.sin(Math.toRadians((i * (90.0/count) ) * 2 - 90.0));
                cacheYZ[i] = Math.sin(Math.toRadians((i * (90.0/count) ) * 2));
            }

            sphereOffset = cacheYX[i];
            sphereOffsetXZ = cacheYZ[i];
            //double yDirOffset = Math.sin(Math.toRadians((i*(180.0/count))) * (90.0/count));
            for(int k = 0; k < count; k++)
            {
                Location particlePos = start.clone();
                double xOffset = 0;
                double zOffset = 0;

                if(cacheX[k] == -1)
                {
                    double rad = Math.toRadians(k*(360.0/count));
                    cacheX[k] = Math.sin(rad);
                    cacheZ[k] = Math.cos(rad);
                }

                xOffset = cacheX[k];
                zOffset = cacheZ[k];

                player.getWorld().spawnParticle(Particle.REDSTONE, particlePos.add(xOffset * sphereOffsetXZ * size, sphereOffset * size, zOffset * sphereOffsetXZ * size), 1, offset, offset, offset, new Particle.DustOptions(colors[(int)(Math.random()*3)], 1f));
            }
        }

        if(++t > 30)
            cancel = true;
        
    }

    @Override
    public void onCancel()
    {
        
    }
}