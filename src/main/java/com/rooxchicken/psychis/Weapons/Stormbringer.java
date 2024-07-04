package com.rooxchicken.psychis.Weapons;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
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

        player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 100, 0.5f, 0.2f, 0.5f, new Particle.DustOptions(Color.WHITE, 1f));
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BIG_DRIPLEAF_FALL, 1, 1);

        jumps.add(player);
    }
}
