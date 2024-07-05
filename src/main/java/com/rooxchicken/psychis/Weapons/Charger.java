package com.rooxchicken.psychis.Weapons;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.rooxchicken.psychis.Psychis;
import com.rooxchicken.psychis.Tasks.Charger_Polarity;

public class Charger extends Weapon
{
    public static ArrayList<Player> players;
    private ArrayList<Projectile> arrows;

    private Psychis plugin;

    public Charger(Psychis _plugin)
    {
        super(_plugin);

        plugin = _plugin;
        itemName = "§x§F§F§F§3§3§9§lCharger";
        type = 4;
        
        arrows = new ArrayList<Projectile>();
        players = new ArrayList<Player>();
    }

    public static void addPlayer(Player player)
    {
        if(!players.contains(player))
            players.add(player);
    }

    public void passive()
    {
        for(Player player : players)
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 21, 3));

        ArrayList<Projectile> toRemove = new ArrayList<Projectile>();


        for(Projectile arrow : arrows)
        {
            arrow.getWorld().spawnParticle(Particle.REDSTONE, arrow.getLocation(), 16, 0.1, 0.1, 0.1, new Particle.DustOptions(Color.YELLOW, 0.4f));
            if(arrow instanceof Arrow && ((Arrow)arrow).isInBlock())
            {
                arrow.getWorld().strikeLightning(arrow.getLocation());
                toRemove.add(arrow);
            }

            if(arrow instanceof Firework && (!((Firework)arrow).isValid() || ((Firework)arrow).isDetonated()))
            {
                Psychis.tasks.add(new Charger_Polarity(plugin, (Player)arrow.getShooter(), arrow));
                toRemove.add(arrow);
            }
        }

        for(Projectile arrow : toRemove)
        {
            arrow.remove();
            arrows.remove(arrow);
        }
    }

    @EventHandler
    public void handleShootingEntity(EntityDamageByEntityEvent event)
    {
        if(!(event.getEntity() instanceof LivingEntity && event.getDamager() instanceof Projectile))
            return;

        LivingEntity entity = (LivingEntity)event.getEntity();
        Projectile damager = (Projectile)event.getDamager();
        if(!players.contains(damager.getShooter()))
            return;

        Player player = (Player)damager.getShooter();

        ItemStack charger = player.getInventory().getItemInMainHand();
        if(charger != null && charger.hasItemMeta() && charger.getItemMeta().getDisplayName().equals(itemName))
        {
            damager.getWorld().spawnParticle(Particle.REDSTONE, damager.getLocation(), 32, 0.1, 0.1, 0.1, new Particle.DustOptions(Color.YELLOW, 0.4f));
            
            if(damager instanceof Arrow)
            {
                damager.getWorld().strikeLightning(damager.getLocation());
                event.setDamage(event.getDamage()*2);
                arrows.remove(damager);

                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 0));
                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 60, 0));
            }
            if(damager instanceof Firework)
            {
                Psychis.tasks.add(new Charger_Polarity(plugin, (Player)damager.getShooter(), damager));
            }
        }
    }

    @EventHandler
    public void shootCrossbow(ProjectileLaunchEvent event)
    {
        if(event.getEntity() instanceof Projectile && !(event.getEntity().getShooter() instanceof Player))
            return;
        
        Player player = (Player)event.getEntity().getShooter();
        if(players.contains(player))
        {
            ItemStack charger = player.getInventory().getItemInMainHand();
            if(charger != null && charger.hasItemMeta() && charger.getItemMeta().getDisplayName().equals(itemName))
            {
                player.getWorld().playSound(player.getLocation(), Sound.ITEM_TRIDENT_HIT, 1, 1);
                arrows.add((Projectile)event.getEntity());
            }
        }
    }
}
