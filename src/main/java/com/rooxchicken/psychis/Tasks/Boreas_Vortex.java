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

public class Boreas_Vortex extends Task
{
    private Psychis plugin;
    private Player player;
    private Location start;

    private int t = 0;
    private int ticks = 0;

    private double size = 1;
    private int count = 10;

    private double[] cacheX;
    private double[] cacheZ;

    private double[] cacheYX;
    private double[] cacheYZ;

    private Color[] colors;

    public Boreas_Vortex(Psychis _plugin, Player _player)
    {
        super(_plugin);
        plugin = _plugin;
        player = _player;
        start = player.getLocation().clone().add(0, 1, 0);
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

        colors = new Color[] {Color.GRAY, Color.WHITE, Color.GRAY};
    }

    @Override
    public void run()
    {
        if(t % 3 == 0)
            player.getWorld().playSound(start, Sound.BLOCK_BIG_DRIPLEAF_FALL, 2, 0.2f);
        start.add(start.getDirection());

        for(Object e : Psychis.getNearbyEntities(start, (int)Math.ceil(size)*4))
        {
            if(e instanceof Entity && player != e)
            {
                Entity entity = (Entity)e;
                entity.setVelocity(start.clone().subtract(entity.getLocation()).toVector().multiply(0.3));
            }
        }
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

                player.getWorld().spawnParticle(Particle.DUST, particlePos.add(xOffset * sphereOffsetXZ * size, sphereOffset * size, zOffset * sphereOffsetXZ * size), 1, offset, offset, offset, new Particle.DustOptions(colors[(int)(Math.random()*3)], 2f));
            }
        }

        Psychis.tasks.add(new Boreas_VortexEffect(plugin, start, player));

        if(++t > 100)
            cancel = true;
        
    }

    @Override
    public void onCancel()
    {
        player.getWorld().playSound(start, Sound.BLOCK_BIG_DRIPLEAF_FALL, 1, 0.8f);
        //Agni_Cinder.stopGlow(player);
    }

    public void setPosition()
    {
        //Agni_Cinder.glow(player);
        double radius = Math.toRadians(start.getYaw()+90);
        start = player.getLocation().clone().add(Math.cos(radius), 1, Math.sin(radius));
    }

    public void resetTimer() { t = 80; //Agni_Cinder.stopGlow(player); 
        player.getWorld().playSound(start, Sound.ENTITY_BAT_TAKEOFF, 1, 1); }

    public Player getPlayer() { return player; }
}