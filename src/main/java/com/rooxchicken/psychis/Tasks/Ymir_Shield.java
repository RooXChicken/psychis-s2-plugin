package com.rooxchicken.psychis.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.rooxchicken.psychis.Psychis;

public class Ymir_Shield extends Task
{
    private Player player;
    private int t;

    public int count = 3;
    private double size = 1.6;
    private float yaw = -180;

    public Ymir_Shield(Psychis _plugin, Player _player)
    {
        super(_plugin);
        player = _player;
        tickThreshold = 1;
    }

    @Override
    public void run()
    {
        if(!player.isValid())
            cancel = true;
        
        spawnShields();

        if(count == 0)
            cancel = true;
    }

    private void spawnShields()
    {
        for(int i = 1; i <= count; i++)
        {
            double _yaw = (yaw + 120 * (3.0/count) * i);
            double rad = Math.toRadians(_yaw);
            double xOffset = Math.cos(rad);
            double zOffset = Math.sin(rad);

            makeShield(xOffset, zOffset, _yaw);
        }
        
        yaw += 2 * (3.0/count);
        if(yaw >= 180)
            yaw = -180;
    }

    private void makeShield(double _xOffset, double _zOffset, double _yaw)
    {
        Location loc = player.getLocation().clone().add(_xOffset*size, 0.8, _zOffset*size);
        player.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, 0, 0, 0, new Particle.DustOptions(Color.AQUA, 1f));

        double rad = Math.toRadians(_yaw+90);
        double xOffset = Math.cos(rad);
        double zOffset = Math.sin(rad);

        for(int i = 1; i <= 3; i++)
        {
            loc.add(xOffset*-0.2, 0.2, zOffset*-0.2);
            player.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, 0, 0, 0, new Particle.DustOptions(Color.AQUA, 1f));
            loc.add(xOffset*0.2, 0, zOffset*0.2);
            player.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, 0, 0, 0, new Particle.DustOptions(Color.AQUA, 1f));
            loc.add(xOffset*0.2, 0, zOffset*0.2);
            player.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, 0, 0, 0, new Particle.DustOptions(Color.AQUA, 1f));
            loc.add(xOffset*-0.2, 0, zOffset*-0.2);
        }


        // player.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, 0, 0, 0, new Particle.DustOptions(Color.AQUA, 1f));
        // player.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, 0, 0, 0, new Particle.DustOptions(Color.AQUA, 1f));
        // player.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, 0, 0, 0, new Particle.DustOptions(Color.AQUA, 1f));
    }
}
