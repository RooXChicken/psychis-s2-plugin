package com.rooxchicken.psychis.Tasks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.rooxchicken.psychis.Psychis;
import com.rooxchicken.psychis.Abilities.Agni;

public class Dolus_CrushArrow extends Task
{
    private Player player;
    private Location start;
    private int t;
    private double dmg = 1;
    private static double offset = 0.1;

    public Dolus_CrushArrow(Psychis _plugin, Player _player, Location _start)
    {
        super(_plugin);
        player = _player;
        start = _start;
        start.setPitch(90);
        start.setYaw(0);
        tickThreshold = 1;
    }

    @Override
    public void run()
    {
        Location particlePos = start;

        particlePos.add(particlePos.getDirection());
        particlePos = constructArrow(particlePos, dmg, player);

        if(particlePos.getBlock().getType().isSolid())
        {
            cancel = true;
            return;
        }
    }

    public static Location constructArrow(Location _pos, double dmg, Player player)
    {
        Location pos = _pos.clone();

        double rad = Math.toRadians(pos.getYaw()+90);
        double xOffset = Math.cos(rad);
        double zOffset = Math.sin(rad);

        for(int k = 0; k < 7; k++)
        {
            player.getWorld().spawnParticle(Particle.DUST, pos.clone().add(pos.getDirection().multiply(k*0.2)), 1, offset, offset, offset, new Particle.DustOptions(Color.PURPLE, 1f));
        }

        double yPosOffset = Math.cos(Math.toRadians(pos.getPitch()));
        double yDirOffset = Math.sin(Math.toRadians(pos.getPitch()));

        int s = 6;

        for(int k = s; k > 0; k--)
        {
            pos.add(pos.getDirection().multiply(0.2));

            double off = (s*k/4)/k * ((k-1)/s);
            player.getWorld().spawnParticle(Particle.DUST, pos.clone().add(pos.getDirection().add(new Vector(off * xOffset * yDirOffset, off * yPosOffset, off * zOffset * yDirOffset))), 1, offset, offset, offset, new Particle.DustOptions(Color.PURPLE, 1f));
            player.getWorld().spawnParticle(Particle.DUST, pos.clone().add(pos.getDirection().subtract(new Vector(off * xOffset * yDirOffset, off * yPosOffset, off * zOffset * yDirOffset))), 1, offset, offset, offset, new Particle.DustOptions(Color.PURPLE, 1f));
        }
        
        return pos;
        
    }
}
