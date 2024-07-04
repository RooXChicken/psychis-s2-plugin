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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.rooxchicken.psychis.Psychis;
import com.rooxchicken.psychis.Tasks.AgniAxe_FieryBlast;

public class AgniAxe extends Weapon
{
    public static ArrayList<Player> players;
    private HashMap<Player, Double> playerDamageMap;

    private Psychis plugin;

    public AgniAxe(Psychis _plugin)
    {
        super(_plugin);

        plugin = _plugin;
        itemName = "§x§F§F§8§0§2§2§lAgni Axe";
        type = 4;

        playerDamageMap = new HashMap<Player, Double>();
        
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
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 21, 0));
        }
    }

    @EventHandler
    public void handleMacePhysics(EntityDamageByEntityEvent event)
    {
        if(!(event.getEntity() instanceof LivingEntity && event.getDamager() instanceof Player))
            return;

        LivingEntity entity = (LivingEntity)event.getEntity();
        Player damager = (Player)event.getDamager();

        if(!players.contains(damager))
            return;

        ItemStack agniAxe = damager.getInventory().getItemInMainHand();
        if(agniAxe != null && agniAxe.hasItemMeta() && agniAxe.getItemMeta().getDisplayName().equals(itemName))
        {
            double damage = 0;
            if(playerDamageMap.containsKey(damager))
                damage = playerDamageMap.get(damager);

            playerDamageMap.remove(damager);
            damage += event.getDamage();

            entity.getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation().clone().add(0,1,0), (int)damage/2, 0.3, 0.4, 0.3, new Particle.DustOptions(Color.ORANGE, 1f));

            if(damage > 200)
            {
                Psychis.tasks.add(new AgniAxe_FieryBlast(plugin, damager));
                return;
            }

            playerDamageMap.put(damager, damage);
        }
    }
}
