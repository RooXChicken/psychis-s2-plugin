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
    private double size = 0;

    private double[] cacheX;
    private double[] cacheZ;

    public Dolus_Crush(Psychis _plugin, Player _player, Dolus _dolus)
    {
        super(_plugin);
        plugin = _plugin;
        player = _player;
        dolus = _dolus;
        start = player.getEyeLocation();
        tickThreshold = 1;

        cacheX = new double[180];
        cacheZ = new double[180];

        for(int i = 0; i < 180; i++)
        {
            cacheX[i] = -1;
            cacheZ[i] = -1;
        }

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1, 0.2f);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 0.8f);

        dolus.deadly = true;
    }

    @Override
    public void run()
    {
        double blockY = Psychis.getBlock(player, 40, 90).getLocation().getY() + 1.1;
        start = player.getEyeLocation();
        start.setY(blockY);
        if(size < 8)
            size += 0.3;
        if(t % 8 == 0)
            Psychis.tasks.add(new Dolus_CrushArrow(plugin, player, new Location(player.getWorld(), start.getX() + ((Math.random()-0.5) * size*2), start.getY() + 8, start.getZ() + ((Math.random()-0.5) * size*2))));
        for(int i = 0; i < 180; i++)
        {
            Location particlePos = start.clone();
            double xOffset = 0;
            double zOffset = 0;

            if(cacheX[i] == -1)
            {
                double rad = Math.toRadians(i*2);
                cacheX[i] = Math.sin(rad);
                cacheZ[i] = Math.cos(rad);
            }

            xOffset = cacheX[i] * size;
            zOffset = cacheZ[i] * size;

            player.getWorld().spawnParticle(Particle.REDSTONE, particlePos.add(xOffset, 0, zOffset), 1, 0, 0, 0, new Particle.DustOptions(Color.PURPLE, 1f));
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
