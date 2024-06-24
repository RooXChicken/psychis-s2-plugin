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

import com.rooxchicken.psychis.Psychis;

public class Agni_HeatSeek extends Task
{
    private Player player;
    private Location start;
    private int t;

    private double[] cacheX;
    private double[] cacheZ;

    public Agni_HeatSeek(Psychis _plugin, Player _player)
    {
        super(_plugin);
        player = _player;
        double blockY = Psychis.getBlock(player, 40, 90).getLocation().getY() + 1.1;
        start = player.getEyeLocation().clone();
        start.setY(blockY);
        tickThreshold = 1;

        cacheX = new double[180];
        cacheZ = new double[180];

        for(int i = 0; i < 180; i++)
        {
            cacheX[i] = -1;
            cacheZ[i] = -1;
        }

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1, 0.2f);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_SMOKER_SMOKE, 1, 1);
    }

    @Override
    public void run()
    {
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

            xOffset = cacheX[i] * t/10;
            zOffset = cacheZ[i] * t/10;

            player.getWorld().spawnParticle(Particle.REDSTONE, particlePos.add(xOffset, 0, zOffset), 1, 0, 0, 0, new Particle.DustOptions(Color.ORANGE, 1f));
        }

        for(Object o : Psychis.getNearbyEntities(start, t/10))
        {
            if(o instanceof LivingEntity && !o.equals(player) && !(o instanceof Villager))
            {
                LivingEntity entity = (LivingEntity)o;
                entity.setFireTicks(40);
                entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 40, 0));
            }
        }

        if(++t > 100)
            cancel = true;
    }
}
