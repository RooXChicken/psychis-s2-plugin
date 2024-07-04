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

public class Dolus_Spiral extends Task
{
    private Player player;
    private Location start;
    private double speed;

    public Dolus_Spiral(Psychis _plugin, Player _player, Location _start, double _speed)
    {
        super(_plugin);
        player = _player;
        start = _start;
        tickThreshold = 1;
        speed = _speed;
    }

    @Override
    public void run()
    {
        start.subtract(0, speed, 0);

        player.getWorld().spawnParticle(Particle.REDSTONE, start, 1, 0, 0, 0, new Particle.DustOptions(Color.PURPLE, 1f));

        if(start.getBlock() != null && !start.getBlock().isPassable())
            cancel = true;
    }
}
