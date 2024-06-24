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

public class Varuna_Typhoon extends Task
{
    private Player player;
    private Location start;
    private int t;
    private double size = 6;

    private double[] cacheX;
    private double[] cacheZ;

    public Varuna_Typhoon(Psychis _plugin, Player _player)
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
            double rad = Math.toRadians(i*2);
            cacheX[i] = Math.sin(rad);
            cacheZ[i] = Math.cos(rad);
        }

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1, 0.2f);
    }

    @Override
    public void run()
    {
        t++;

        if(t % 2 == 0)
            player.getWorld().playSound(start, Sound.ITEM_BUCKET_FILL, 1, 1.2f);
        for(int i = 0; i < 180; i++)
        {
            Location particlePos = start.clone();
            double xOffset = cacheX[i] * size;
            double zOffset = cacheZ[i] * size;

            player.getWorld().spawnParticle(Particle.REDSTONE, particlePos.clone().add(xOffset, Math.sin(Math.toRadians(i*80+(t*8)))/2.0, zOffset), 2, 0, 0, 0, new Particle.DustOptions((Math.random() > 0.7) ? Color.TEAL : Color.BLUE, 1f));
            player.getWorld().spawnParticle(Particle.REDSTONE, particlePos.clone().add(xOffset, Math.sin(Math.toRadians(i*80-80+(t*8)))/2.0, zOffset), 2, 0, 0, 0, new Particle.DustOptions((Math.random() > 0.7) ? Color.TEAL : Color.BLUE, 1f));
        }

        if(t > 30)
        {
            for(Object o : Psychis.getNearbyEntities(start, (int)Math.ceil(size)))
            {
                if(o instanceof LivingEntity && !o.equals(player) && !(o instanceof Villager))
                {
                    LivingEntity entity = (LivingEntity)o;
                    entity.setVelocity(new Vector(0, 1.5, 0));
                    player.getWorld().playSound(start, Sound.ITEM_TRIDENT_HIT, 1, 1.2f);
                    entity.damage(16);

                    for(int i = 0; i < 20; i++)
                    {
                        for(int k = 0; k < 60; k++)
                            player.getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation().clone().add(cacheX[k*3]/(i*0.5), i/3.6, cacheZ[k*3]/(i*0.5)), 2, 0, 0, 0, new Particle.DustOptions((Math.random() > 0.7) ? Color.TEAL : Color.BLUE, 1f));
                    }
                }
            }
            cancel = true;
        }
    }
}
