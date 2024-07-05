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

public class VoidPiercer extends Weapon
{
    public static ArrayList<Player> players;

    private Psychis plugin;

    public VoidPiercer(Psychis _plugin)
    {
        super(_plugin);

        plugin = _plugin;
        itemName = "§x§C§C§3§9§F§F§lVoid Piercer";
        type = 4;
        
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
    public void handleWaveLaunch(PlayerInteractEvent event)
    {
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        Player player = event.getPlayer();

        if(!players.contains(player) || player.getCooldown(Material.NETHERITE_AXE) > 0)
            return;

        ItemStack voidPiercer = player.getInventory().getItemInMainHand();
        if(voidPiercer != null && voidPiercer.hasItemMeta() && voidPiercer.getItemMeta().getDisplayName().equals(itemName))
        {
            player.setCooldown(Material.NETHERITE_AXE, 200);
            Psychis.tasks.add(new VoidPiercer_GravityWave(plugin, player));
        }
    }
}
