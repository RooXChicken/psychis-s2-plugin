package com.rooxchicken.psychis.Tasks;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.rooxchicken.psychis.Psychis;

public class VoidPiercer_GravityWave extends Task
{
    private Psychis plugin;
    private Player player;

    private Location start;
    private double xOffset;
    private double zOffset;

    private ArrayList<Entity> frozen;

    private int t = 0;

    public VoidPiercer_GravityWave(Psychis _plugin, Player _player)
    {
        super(_plugin);

        plugin = _plugin;
        player = _player;

        start = player.getLocation().clone().add(0,1,0);

        double rad = Math.toRadians(start.getYaw());
        xOffset = Math.cos(rad);
        zOffset = Math.sin(rad);

        frozen = new ArrayList<Entity>();

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1, 0.2f);
        tickThreshold = 1;
    }

    @Override
    public void run()
    {
        //start.add(start.getDirection());
        for(int i = 0; i < 9; i++)
        {
            spawnParticle(start.clone().add(xOffset * (-4+i)/2, 0, zOffset * (-4+i)/2));
        }
        start.add(start.getDirection().multiply(0.6));
        for(int i = 0; i < 5; i++)
        {
            spawnParticle(start.clone().add(xOffset * (-2+i)/2, 0, zOffset * (-2+i)/2));
        }
        start.add(start.getDirection().multiply(0.6));
        for(int i = 0; i < 3; i++)
        {
            spawnParticle(start.clone().add(xOffset * (-1+i)/2, 0, zOffset * (-1+i)/2));
        }

        for(Object o : Psychis.getNearbyEntities(start, 4))
        {
            if(o instanceof Entity && o != player)
            {
                if(!frozen.contains(o))
                    frozen.add((Entity)o);
            }
        }

        for(Entity entity : frozen)
            entity.setVelocity(new Vector(0,0,0));

        if(++t > 50)
            cancel = true;
    }

    private void spawnParticle(Location loc)
    {
        loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, 0, 0, 0, new Particle.DustOptions(Color.PURPLE, 2.0f));
    }
}