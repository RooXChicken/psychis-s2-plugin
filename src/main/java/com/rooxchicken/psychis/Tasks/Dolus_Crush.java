package com.rooxchicken.psychis.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.rooxchicken.psychis.Psychis;
import com.rooxchicken.psychis.Abilities.Dolus;

public class Dolus_Crush extends Task
{
    private Psychis plugin;
    private Player player;
    private Dolus dolus;
    private Location start;
    private int t;
    private double size = 8;

    private double[] cacheX;
    private double[] cacheZ;

    public Dolus_Crush(Psychis _plugin, Player _player, Dolus _dolus)
    {
        super(_plugin);
        plugin = _plugin;
        player = _player;
        dolus = _dolus;
        start = player.getEyeLocation();
        start.subtract(0, 3, 0);
        
        tickThreshold = 1;

        cacheX = new double[180];
        cacheZ = new double[180];

        for(int i = 0; i < 180; i++)
        {
            double rad = Math.toRadians(i*2);
            cacheX[i] = Math.sin(rad);
            cacheZ[i] = Math.cos(rad);
        }

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1, 0.2f);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 0.8f);

        dolus.deadly = true;
    }

    @Override
    public void run()
    {
        // double blockY = Psychis.getBlock(player, 40, 90).getLocation().getY() + 1.1;
        // start = player.getEyeLocation();
        // start.subtract(0, 3, 0);
        // int offset = (t*4) % 180;
        // // if(size < 8)
        // //     size += 0.3;
        // // if(t % 8 == 0)
        // //     Psychis.tasks.add(new Dolus_CrushArrow(plugin, player, new Location(player.getWorld(), start.getX() + ((Math.random()-0.5) * size*2), start.getY() + 8, start.getZ() + ((Math.random()-0.5) * size*2))));
        // for(int i = 0; i < 180; i++)
        // {
        //     Location particlePos = start.clone();
        //     double xOffset = 0;
        //     double zOffset = 0;

        //     double xOffset2 = 0;
        //     double zOffset2 = 0;

        //     if(i + offset > 179)
        //         offset -= 180;

        //     int kOffset = (t*4) % 180;

        //     int k = (i + 90) % 180;

        //     if(k + kOffset > 179)
        //         k -= 180;

        //     //Bukkit.getLogger().info(k + " " + (k+(t*4)));

        //     if(cacheX[i + offset] == -1)
        //     {
        //         double rad = Math.toRadians(i*2);
        //         cacheX[i + offset] = Math.sin(rad);
        //         cacheZ[i + offset] = Math.cos(rad);
        //     }

        //     xOffset = cacheX[i + offset] * size;
        //     zOffset = cacheZ[i + offset] * size;

        //     xOffset2 = cacheX[k + kOffset] * size;
        //     zOffset2 = cacheZ[k + kOffset] * size;

        //     player.getWorld().spawnParticle(Particle.REDSTONE, particlePos.clone().add(xOffset, i/16.0, zOffset), 1, 0, 0, 0, new Particle.DustOptions(Color.PURPLE, 1f));
        //     player.getWorld().spawnParticle(Particle.REDSTONE, particlePos.clone().add(xOffset2, i/16.0, zOffset2), 1, 0, 0, 0, new Particle.DustOptions(Color.PURPLE, 1f));
        // }

        //double blockY = Psychis.getBlock(player, 40, 90).getLocation().getY() + 1.1
        int offset = (t*4) % 180;
        // if(size < 8)
        //     size += 0.3;
        // if(t % 8 == 0)
        //     Psychis.tasks.add(new Dolus_CrushArrow(plugin, player, new Location(player.getWorld(), start.getX() + ((Math.random()-0.5) * size*2), start.getY() + 8, start.getZ() + ((Math.random()-0.5) * size*2))));
        //for(int i = 0; i < 180; i++)
        int ii = (t*2) % 180;
        for(int o = 0; o < 8; o++)
        {
            int i = ii + o;
            Location particlePos = start.clone();
            double xOffset = 0;
            double zOffset = 0;

            double xOffset2 = 0;
            double zOffset2 = 0;

            if(i + offset > 179)
                offset -= 180;

            int kOffset = (t*4) % 180;

            int k = (i + 90) % 180;

            if(k + kOffset > 179)
                k -= 180;

            xOffset = cacheX[i + offset] * size;
            zOffset = cacheZ[i + offset] * size;

            xOffset2 = cacheX[k + kOffset] * size;
            zOffset2 = cacheZ[k + kOffset] * size;

            Psychis.tasks.add(new Dolus_Spiral(plugin, player, particlePos.clone().add(xOffset, 18 + o/8.0, zOffset), 1));
            Psychis.tasks.add(new Dolus_Spiral(plugin, player, particlePos.clone().add(xOffset2, 18 + o/8.0, zOffset2), 1));

            //player.getWorld().spawnParticle(Particle.REDSTONE, particlePos.clone().add(xOffset, i/16.0, zOffset), 1, 0, 0, 0, new Particle.DustOptions(Color.PURPLE, 1f));
            //player.getWorld().spawnParticle(Particle.REDSTONE, particlePos.clone().add(xOffset2, i/16.0, zOffset2), 1, 0, 0, 0, new Particle.DustOptions(Color.PURPLE, 1f));
        }

        for(Object o : Psychis.getNearbyEntities(start, (int)Math.ceil(size)))
        {
            if(o instanceof Entity && o != player)
            {
                ((Entity)o).setVelocity(((Entity)o).getVelocity().multiply(0.4));
                if(o instanceof Player)
                    ((Player)o).addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 10, 2));
            }
        }

        if(++t > 200)
            cancel = true;
    }

    @Override
    public void onCancel()
    {
        dolus.deadly = false;
    }
}
