package com.rooxchicken.psychis.Tasks;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleSwimEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.rooxchicken.psychis.Psychis;

public class Ichor_BloodRay extends Task implements Listener
{
    private Psychis plugin;
    private Player player;

    public int t = 0;
    private int size = 60;
    private float particleSize = 0.3f;

    private Location[] points;

    public Ichor_BloodRay(Psychis _plugin, Player _player)
    {
        super(_plugin);

        plugin = _plugin;
        player = _player;

        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        points = new Location[size];

        Location loc = player.getEyeLocation().clone();
        for(int i = 0; i < size; i++)
        {
            loc.add(loc.getDirection().multiply(0.5));
            points[i] = loc.clone();
        }

        tickThreshold = 1;
    }

    @Override
    public void run()
    {
        if(t == 200)
        {
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_STONE_BREAK, 1, 1);
            for(int i = 0; i < size; i++)
            {
                player.getWorld().spawnParticle(Particle.BLOCK_DUST, points[i], 20, 0.5, 0.5, 0.5, Material.REDSTONE_BLOCK.createBlockData());
            }
        }
        if(t < 200)
        {
            for(int i = 0; i < 3; i++)
                updatePierce();
            
            for(int i = 0; i < size; i++)
            {
                player.getWorld().spawnParticle(Particle.REDSTONE, points[i], 1, 0.1*particleSize, 0.1*particleSize, 0.1*particleSize, new Particle.DustOptions(Color.RED, particleSize));
                for(Object o : Psychis.getNearbyEntities(points[i], 1))
                {
                    if(o instanceof LivingEntity)
                    {
                        ((LivingEntity)o).damage(3*particleSize);
                    }
                }
            }

            if(t % (9 - (int)(particleSize*2)) == 0)
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, particleSize/2, 1);

            particleSize += 0.007f;
        }
        else
            plugin.setCooldownForce(player, 0, Psychis.ability1CooldownKey);

        if(t == -1 || ++t > 400)
            cancel = true;
    }

    public void updatePierce()
    {
        Location[] oldPoints = points.clone();

        for(int i = 1; i < size; i++)
        {
            Location mid = oldPoints[i-1].clone();
            points[i] = midpoint(mid, oldPoints[i].clone().add(mid.getDirection().multiply(0.6))); //mid.clone().add(mid.getDirection().multiply(0.6))
        }
        points[0] = player.getEyeLocation().clone();

        //points = oldPoints;
    }

    private Location midpoint(Location loc1, Location loc2)
    {
        Location loc = new Location(loc1.getWorld(), 0, 0, 0);
        loc.setX((loc1.getX() + loc2.getX())/2);
        loc.setY((loc1.getY() + loc2.getY())/2);
        loc.setZ((loc1.getZ() + loc2.getZ())/2);
        loc.setPitch(loc1.getPitch());
        loc.setYaw(loc1.getYaw());
        // loc.setPitch((loc1.getPitch() + loc2.getPitch())/2);
        // loc.setY((loc1.getYaw() + loc2.getYaw())/2);

        return loc;
    }

    @Override
    public void onCancel()
    {
        HandlerList.unregisterAll(this);
        //Psychis.sendPlayerData(player, "4_false");
    }
}
