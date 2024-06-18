package com.rooxchicken.psychis.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.EnumWrappers.Hand;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.rooxchicken.psychis.Psychis;
import com.rooxchicken.psychis.Tasks.Agni_Cinder;
import com.rooxchicken.psychis.Tasks.Agni_HeatSeek;
import com.rooxchicken.psychis.Tasks.Ymir_Biome;
import com.rooxchicken.psychis.Tasks.Ymir_Shield;

public class Ymir extends Ability implements Listener
{
    private Psychis plugin;
    private Player player;

    private double ticks = 0;
    private Ymir_Shield shield;

    private List<BlockData> oldBlocks;
    private Location frozenBiome;

    public Ymir(Psychis _plugin, Player _player)
    {
        super(_plugin, _player);
        plugin = _plugin;
        player = _player;

        type = 4;
        name = "Ymir";

        cooldown1 = 60;
        cooldown2 = 120;
    }

    @Override
    public void passive()
    {
        Material blockType = player.getLocation().subtract(0, 0.5, 0).getBlock().getType();
        if(blockType == Material.ICE || blockType == Material.BLUE_ICE || blockType == Material.PACKED_ICE || blockType == Material.FROSTED_ICE)
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
        else
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1);

        if(shield != null)
        {
            if(shield.cancel)
            {
                plugin.setCooldownForce(player, cooldown1, Psychis.ability1CooldownKey);
                shield = null;
            }
        }

    }

    @Override
    public void activateFirstAbility(int state)
    {
        if(state == 0)
        {
            if(shield != null || !plugin.checkCooldown(player, Psychis.ability1CooldownKey))
                return;

            shield = new Ymir_Shield(plugin, player);
            Psychis.tasks.add(shield);
        }
    }
    
    @Override
    public void activateSecondAbility(int state)
    {
        if(!plugin.secondUnlocked(player))
            return;
        if(state == 0)
        {
            if(!plugin.setCooldown(player, cooldown2, Psychis.ability2CooldownKey))
                return;

            Psychis.tasks.add(new Ymir_Biome(plugin, player, this));
        }
    }

    private void onShield()
    {
        shield.count--;
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_FREEZE, 1, 1);
    }

    // @EventHandler
    // public void blockDamage(EntityDamageEvent event)
    // {
    //     if(event.getEntity() != player || (shield == null && shield.count == 0))
    //         return;

    //     event.setCancelled(true);
    //     onShield();
    // }

    @EventHandler
    public void blockDamageAndFreeze(EntityDamageByEntityEvent event)
    {
        if(shield == null)
            return;
        if(event.getEntity() != player || shield.count == 0)
            return;

        onShield();
        event.getDamager().setFreezeTicks(420);
        
        event.setCancelled(true);
    }

    @EventHandler
    public void checkSecondUnlock(EntityDeathEvent event)
    {
        if(plugin.secondUnlocked(player))
            return;

        LivingEntity entity = event.getEntity();

        if(entity.getType() == EntityType.BLAZE)
        {
            if(entity.getKiller() == player)
            {
                Biome biome = player.getWorld().getBiome(player.getLocation());
                if(biome == Biome.ICE_SPIKES || biome == Biome.SNOWY_BEACH || biome == Biome.SNOWY_PLAINS || biome == Biome.SNOWY_SLOPES || biome == Biome.SNOWY_TAIGA)
                    plugin.unlockSecondAbility(player);
            }
        }
    }
}
