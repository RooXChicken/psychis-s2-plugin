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

public class Agni_Cinder extends Task
{
    private Player player;
    private Location start;
    private int t;
    private double dmg = 1;
    private static double offset = 0.1;

    public Agni_Cinder(Psychis _plugin, Player _player, double _dmg)
    {
        super(_plugin);
        player = _player;
        start = player.getEyeLocation().clone();
        tickThreshold = 1;

        dmg = _dmg;
    }

    @Override
    public void run()
    {
        Location particlePos = start.clone();

        particlePos.add(particlePos.getDirection().multiply(t + 0.1));
        particlePos = constructArrow(particlePos, dmg, player);

        if(particlePos.getBlock().getType().isSolid())
        {
            explode(particlePos);
            cancel = true;
            return;
        }

        for(Object o : Psychis.getNearbyEntities(particlePos, 1))
        {
            if(o instanceof Entity && !o.equals(player))
            {
                Entity entity = (Entity)o;
                entity.setFireTicks(200);

                if(entity instanceof LivingEntity)
                    ((LivingEntity)entity).damage(dmg*4.0, player);

                explode(particlePos);

                cancel = true;
                return;
            }
        }

        if(++t > 400)
            cancel = true;
    }

    public static Location constructArrow(Location _pos, double dmg, Player player)
    {
        Color color = Color.fromRGB(255, (int)(128*(1/(dmg+1))), (int)(34*(1/(dmg+1))));
        Location pos = _pos.clone();
        double max = Math.ceil(dmg) + (1-(dmg - (int)dmg));

        double rad = Math.toRadians(pos.getYaw()+90);
        double xOffset = Math.cos(rad);
        double zOffset = Math.sin(rad);

        for(int k = 0; k < 7; k++)
        {
            player.getWorld().spawnParticle(Particle.REDSTONE, pos.clone().add(pos.getDirection().multiply(k*0.2)), 1, offset, offset, offset, new Particle.DustOptions(color, 1f));
        }

        double yPosOffset = Math.cos(Math.toRadians(pos.getPitch()));
        double yDirOffset = Math.sin(Math.toRadians(pos.getPitch()));

        for(int k = (int)max+1; k > 0; k--)
        {
            pos.add(pos.getDirection().multiply(0.2));

            double off = (max*k/4)/k * ((k-1)/max);
            player.getWorld().spawnParticle(Particle.REDSTONE, pos.clone().add(pos.getDirection().add(new Vector(off * xOffset * yDirOffset, off * yPosOffset, off * zOffset * yDirOffset))), 1, offset, offset, offset, new Particle.DustOptions(color, 1f));
            player.getWorld().spawnParticle(Particle.REDSTONE, pos.clone().add(pos.getDirection().subtract(new Vector(off * xOffset * yDirOffset, off * yPosOffset, off * zOffset * yDirOffset))), 1, offset, offset, offset, new Particle.DustOptions(color, 1f));
        }
        
        player.getWorld().playSound(pos, Sound.BLOCK_SMOKER_SMOKE, 1, 1);
        return pos;
        
    }

    public static void glow(Player player)
    {
        glowPacket((byte) 0x40, player);
    }

    public static void stopGlow(Player player)
    {
        glowPacket((byte) 0x00, player);
    }

    private static void glowPacket(byte glowState, Player player)
    {
        for(Entity entity : player.getWorld().getEntities())
        {
            if(entity != player)
            {
                PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
                packet.getIntegers().write(0, entity.getEntityId()); //Set packet's entity id
    
                List<WrappedDataValue> wrappedDataValueList = new ArrayList<>();
                byte data = 0x0;
                if(entity.getFireTicks() > 0)
                    data += 0x01;
                if(entity instanceof Player && ((Player)entity).isSneaking())
                    data += 0x02;
                if(entity instanceof Player && ((Player)entity).isSprinting())
                    data += 0x08;
                if(entity instanceof Player && ((Player)entity).isSwimming())
                    data += 0x10;
                if(entity instanceof Player && ((Player)entity).isInvisible())
                    data += 0x20;
                if(entity instanceof Player && ((Player)entity).isFlying())
                    data += 0x80;

                data += glowState;
                wrappedDataValueList.add(new WrappedDataValue(0, Registry.get(Byte.class), data));
    
                packet.getDataValueCollectionModifier().write(0, wrappedDataValueList);
                
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);

            }
        }
    }

    private void explode(Location _pos)
    {
        Location pos = _pos.clone();

        player.getWorld().spawnParticle(Particle.FLAME, pos, (int)(60*dmg), 0.5, 0.5, 0.5, 0.2);
        player.getWorld().createExplosion(pos.getX(), pos.getY(), pos.getZ(), (float)Psychis.ClampD(dmg*1.2, 0, 2));

        for(Object o : Psychis.getNearbyEntities(pos, (int)dmg*3))
        {
            if(o instanceof Entity && !o.equals(player))
            {
                Entity entity = (Entity)o;
                entity.setFireTicks(100);

                if(entity instanceof ExplosiveMinecart)
                {
                    ((ExplosiveMinecart)entity).setFuseTicks(0);
                }
            }
        }
    }
}
