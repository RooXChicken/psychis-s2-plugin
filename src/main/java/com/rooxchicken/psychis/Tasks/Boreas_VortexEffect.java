package com.rooxchicken.psychis.Tasks;

import java.util.List;
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

import net.minecraft.server.network.TextFilter.e;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.world.level.block.SoundEffectType;

public class Boreas_VortexEffect extends Task
{
    private Player player;
    private Location moveTo;
    private Location start;
    private int t = 0;

    public Boreas_VortexEffect(Psychis _plugin, Location _moveTo, Player _player)
    {
        super(_plugin);
        moveTo = _moveTo;
        double rad = 8;
        player = _player;
        //if(Math.random() > 0.5)
            start = moveTo.clone().add((rad/2)-Math.random()*rad, (rad/2)-Math.random()*rad, (rad/2)-Math.random()*rad);
        // else
        //     start = moveTo.clone().subtract((rad/2)-Math.random()*rad, (rad/2)-Math.random()*rad, (rad/2)-Math.random()*rad);

        tickThreshold = 1;
    }

    @Override
    public void run()
    {
        double off = 0.0;
        start.add(start.clone().subtract(moveTo).multiply(0.2).add(Math.random()*off, Math.random()*off, Math.random()*off).multiply(-1));
        player.getWorld().spawnParticle(Particle.DUST, start, 1, 0, 0, 0, new Particle.DustOptions(Color.WHITE, 1f));

        t++;

        if(t > 10)
            cancel = true;
    }

    public Player getPlayer() { return player; }
    
}
