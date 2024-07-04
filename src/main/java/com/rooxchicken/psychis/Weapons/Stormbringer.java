package com.rooxchicken.psychis.Weapons;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.rooxchicken.psychis.Psychis;

public class Stormbringer extends Weapon
{
    public static ArrayList<Player> players;
    private ArrayList<Player> jumps;

    private Psychis plugin;

    public Stormbringer(Psychis _plugin)
    {
        super(_plugin);

        plugin = _plugin;
        itemName = "§x§D§9§E§E§F§3§lStormbringer";
        type = 4;

        jumps = new ArrayList<Player>();
        
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
        {
            if(player.isOnGround())
            {
                player.setAllowFlight(true);
                if(jumps.contains(player))
                    jumps.remove(player);
            }
        }
    }

    @EventHandler
    public void handleMacePhysics(EntityDamageByEntityEvent event)
    {
        if(!(event.getEntity() instanceof LivingEntity && event.getDamager() instanceof Player))
            return;

        LivingEntity entity = (LivingEntity)event.getEntity();
        Player damager = (Player)event.getDamager();

        if(!players.contains(damager) || damager.getFallDistance() < 2)
            return;

        ItemStack stormBringer = damager.getInventory().getItemInMainHand();
        if(stormBringer != null && stormBringer.hasItemMeta() && stormBringer.getItemMeta().getDisplayName().equals(itemName))
        {
            event.setDamage(event.getDamage() + damager.getFallDistance()*2);
            damager.setVelocity(new Vector(damager.getVelocity().getX(), damager.getFallDistance()/6.0, damager.getVelocity().getZ()));

            jumpEffect(damager);
            entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_ANVIL_LAND, 0.7f, 0.8f);
        }
    }

    @EventHandler
    public void activateDoubleJump(PlayerToggleFlightEvent event)
    {
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE || !players.contains(event.getPlayer()))
            return;

        event.setCancelled(true);
        Player player = event.getPlayer();
        player.setFlying(false);
        player.setAllowFlight(false);

        Vector direction = player.getLocation().getDirection().clone();
        player.setVelocity(player.getVelocity().add(direction));

        jumpEffect(player);

        jumps.add(player);
    }

    private void jumpEffect(LivingEntity entity)
    {
        entity.getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation(), 100, 0.5f, 0.2f, 0.5f, new Particle.DustOptions(Color.WHITE, 1f));
        entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_BIG_DRIPLEAF_FALL, 1, 1);
    }
}
