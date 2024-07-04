package com.rooxchicken.psychis.Tasks;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.rooxchicken.psychis.Psychis;

public class Midas_Greed extends Task implements Listener
{
    private Player player;
    private Location start;
    private int t;

    public static ArrayList<Player> cantEat;

    public Midas_Greed(Psychis _plugin, Player _player)
    {
        super(_plugin);
        player = _player;
        tickThreshold = 1;

        cantEat = new ArrayList<Player>();
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1, 1);
    }

    @Override
    public void run()
    {
        for(Object o : Psychis.getNearbyEntities(player.getLocation(), 10))
        {
            if(o instanceof Player && o != player)
            {
                Player p = (Player)o;
                if(!cantEat.contains(p))
                {
                    cantEat.add(p);
                    p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation().clone().add(0, 1, 0), 40, 0.5, 0.5, 0.5, new Particle.DustOptions(Color.YELLOW, 1f));
                }
            }
        }

        if(++t > 100)
            cancel = true;
    }

    @Override
    public void onCancel()
    {
        cantEat.clear();
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 1.6f);
    }

    @EventHandler
    public void cancelEat(PlayerInteractEvent event)
    {
        Player p = event.getPlayer();
        if(!cantEat.contains(p))
            return;

        ItemStack item = event.getItem();
        if(item != null && item.getType() == Material.GOLDEN_APPLE)
        {
            event.setCancelled(true);
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 0.9f);
        }
    }
}
