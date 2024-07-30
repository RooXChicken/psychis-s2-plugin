package com.rooxchicken.psychis.Tasks;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleSwimEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.rooxchicken.psychis.Psychis;

public class Ichor_FangSlide extends Task implements Listener
{
    private Psychis plugin;
    private Player player;

    private ArrayList<Block> changedBlocks;

    public int t = 0;
    public Ichor_FangSlide(Psychis _plugin, Player _player)
    {
        super(_plugin);

        plugin = _plugin;
        player = _player;

        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        changedBlocks = new ArrayList<Block>();
        //Psychis.sendPlayerData(player, "4_true");

        tickThreshold = 1;
    }

    @Override
    public void run()
    {
        player.setSwimming(true);
        Location ahead = player.getLocation().clone();
        ahead.setPitch(0);

        for(Block block : changedBlocks)
        {
            player.sendBlockChange(block.getLocation(), block.getBlockData());
        }
        changedBlocks.clear();

        Block block = ahead.clone().add(ahead.getDirection()).getBlock();
        if(!block.isPassable())
        {
            Location blockLoc = block.getLocation();
            if(blockLoc.clone().add(0,1,0).getBlock().isPassable())
                player.teleport(player.getLocation().add(0,1,0));
        }

        BlockData blockData = Material.BARRIER.createBlockData();
        for(int i = 0; i < 4; i++)
        {
            for(int ii = 0; ii < 4; ii++)
            {
                Location loc = player.getLocation().clone().add(-2+i,1,-2+ii);
                if(loc.getBlock().getType().equals(Material.AIR))
                {
                    changedBlocks.add(loc.getBlock());
                    player.sendBlockChange(loc, blockData);
                }
            }
        }

        if(t % 3 == 0)
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_HONEY_BLOCK_SLIDE, 1, 1);

        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2, 3));
        player.setVelocity(ahead.getDirection().multiply(0.6).add(new Vector(0, player.getVelocity().getY() - 0.12, 0)));

        player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 40, 0.4, 0.2, 0.4, new Particle.DustOptions(Color.MAROON, 1.0f));
        
        if(t == -1 || ++t > 120)
            cancel = true;
    }

    @EventHandler
    public void preventUncrawl(EntityToggleSwimEvent event)
    {
        event.setCancelled(event.getEntity() == player && !event.isSwimming());
    }

    @Override
    public void onCancel()
    {
        player.setSwimming(false);
        HandlerList.unregisterAll(this);
        for(Block block : changedBlocks)
        {
            player.sendBlockChange(block.getLocation(), block.getBlockData());
        }
        //Psychis.sendPlayerData(player, "4_false");
    }
}
