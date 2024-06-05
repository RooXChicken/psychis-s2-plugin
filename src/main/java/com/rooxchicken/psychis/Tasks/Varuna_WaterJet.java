package com.rooxchicken.psychis.Tasks;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.rooxchicken.psychis.Psychis;
import com.rooxchicken.psychis.Abilities.Varuna;

import net.minecraft.server.network.TextFilter.e;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.world.level.block.SoundEffectType;

public class Varuna_WaterJet extends Task
{
    private Player player;
    private Location start;
    private Varuna varuna;
    private int t = 0;

    private double size = 1;
    private int count = 10;

    private double[] cacheX;
    private double[] cacheZ;

    private Color[] colors;
    private ArrayList<Entity> pushed;

    public Varuna_WaterJet(Psychis _plugin, Player _player, Varuna _varuna)
    {
        super(_plugin);
        player = _player;
        start = player.getLocation().clone().add(0, 1, 0);
        tickThreshold = 1;
        varuna = _varuna;
        varuna.deadly = true;

        pushed = new ArrayList<Entity>();
    }

    @Override
    public void run()
    {
        start.add(start.getDirection().multiply(1.5));

        if(t % 2 == 0)
            player.getWorld().playSound(start, Sound.ITEM_BUCKET_FILL, 1, 1.2f);

        for(Object e : Psychis.getNearbyEntities(start, 1))
        {
            if(e instanceof Entity && player != e)
            {
                Entity entity = (Entity)e;
                if(!pushed.contains(entity))
                {
                    pushed.add(entity);
                    Location mid = entity.getLocation().clone();
                    mid.setY(start.getY());
                    double distance = 3-Psychis.ClampD(start.distance(mid), 0, 3);
                    Vector move = start.clone().subtract(mid).toVector().multiply(distance*-2);
                    move.setY(0.2);
                    entity.setVelocity(move);
                    if(entity instanceof LivingEntity)
                        ((LivingEntity)entity).damage(10);

                        player.getWorld().playSound(start, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1, 1.2f);
                }
            }
        }

        player.getWorld().spawnParticle(Particle.WATER_SPLASH, start.clone().add(0, Math.sin(Math.toRadians(t*80)), 0), 60, 0, 0, 0);
        player.getWorld().spawnParticle(Particle.WATER_SPLASH, start.clone().add(0, Math.sin(Math.toRadians(t*80-80)), 0), 60, 0, 0, 0);

        player.getWorld().spawnParticle(Particle.REDSTONE, start.clone().add(0, Math.sin(Math.toRadians(t*80)), 0), 2, 0, 0, 0, new Particle.DustOptions(Color.BLUE, 1.5f));
        player.getWorld().spawnParticle(Particle.REDSTONE, start.clone().add(0, Math.sin(Math.toRadians(t*80-80)), 0), 2, 0, 0, 0, new Particle.DustOptions(Color.BLUE, 1.5f));

        t++;

        if(t > 20)
            cancel = true;
    }

    @Override
    public void onCancel()
    {
        varuna.deadly = false;
    }
}
