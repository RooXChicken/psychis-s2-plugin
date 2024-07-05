package com.rooxchicken.psychis.Weapons;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.rooxchicken.psychis.Psychis;
import com.rooxchicken.psychis.Tasks.AgniAxe_FieryBlast;
import com.rooxchicken.psychis.Tasks.VoidPiercer_GravityWave;

public class Frostfang extends Weapon
{
    public static ArrayList<Player> players;
    private HashMap<Player, Integer> playerHitCount;

    private Psychis plugin;

    public Frostfang(Psychis _plugin)
    {
        super(_plugin);

        plugin = _plugin;
        itemName = "§x§2§B§D§8§F§F§lFrostfang";
        type = 4;
        
        playerHitCount = new HashMap<Player, Integer>();
        players = new ArrayList<Player>();
    }

    public static void addPlayer(Player player)
    {
        if(!players.contains(player))
            players.add(player);
    }

    public void passive()
    {
        
    }

    @EventHandler
    public void handleDamageBuildup(EntityDamageByEntityEvent event)
    {
        if(!(event.getEntity() instanceof LivingEntity && event.getDamager() instanceof Player))
            return;

        LivingEntity entity = (LivingEntity)event.getEntity();
        Player damager = (Player)event.getDamager();

        if(!players.contains(damager))
            return;

        ItemStack frostfang = damager.getInventory().getItemInMainHand();
        if(frostfang != null && frostfang.hasItemMeta() && frostfang.getItemMeta().getDisplayName().equals(itemName))
        {
            int count = 0;
            if(playerHitCount.containsKey(damager))
                count = playerHitCount.get(damager);

            playerHitCount.remove(damager);
            count++;

            if(count > 14)
            {
                Location start = damager.getLocation().clone().add(0,1,0);
                double rad = Math.toRadians(start.getYaw());
                double xOffset = Math.cos(rad);
                double zOffset = Math.sin(rad);
                start.add(start.getDirection().multiply(0.5));
                for(int i = 0; i < 7; i++)
                {
                    spawnParticle(start.clone().add(xOffset * (-3+i)/2, 0, zOffset * (-3+i)/2));
                }
                start.add(start.getDirection().multiply(0.5));
                for(int i = 0; i < 5; i++)
                {
                    spawnParticle(start.clone().add(xOffset * (-2+i)/2, 0, zOffset * (-2+i)/2));
                }
                start.add(start.getDirection().multiply(0.5));
                for(int i = 0; i < 3; i++)
                {
                    spawnParticle(start.clone().add(xOffset * (-1+i)/2, 0, zOffset * (-1+i)/2));
                }
                event.setDamage(event.getDamage()*2);
                entity.setFreezeTicks(200);
                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 200, 0));

                entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
                entity.getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation().clone().add(0,1,0), 100, 0.6, 0.8, 0.6, new Particle.DustOptions(Color.fromRGB(0x6ab0ff), 1.0f));

                return;
            }

            playerHitCount.put(damager, count);
        }
    }

    private void spawnParticle(Location loc)
    {
        loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, 0, 0, 0, new Particle.DustOptions(Color.fromRGB(0x6ab0ff), 2.0f));
    }
}
