package com.rooxchicken.psychis.Tasks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.rooxchicken.psychis.Psychis;
import com.rooxchicken.psychis.Abilities.Midas;
import com.rooxchicken.psychis.Abilities.Ymir;

public class Midas_Jackpot extends Task implements Listener
{
    private Player player;
    private Location start;
    private Midas midas;
    private int t;

    private int size = 0;
    private ArrayList<Block> converted;
    private ArrayList<BlockData> oldBlocks;
    List<Block> blocks;

    public Midas_Jackpot(Psychis _plugin, Player _player, Midas _midas)
    {
        super(_plugin);
        player = _player;
        midas = _midas;
        start = player.getLocation().clone();
        tickThreshold = 1;

        Bukkit.getPluginManager().registerEvents(this, _plugin);

        converted = new ArrayList<Block>();
        oldBlocks = new ArrayList<BlockData>();

        midas.deadly = true;
    }

    @Override
    public void run()
    {
        spread();

        for(Object e : Psychis.getNearbyEntities(start, size))
        {
            if(e instanceof LivingEntity && !(e instanceof Villager))
            {
                LivingEntity entity = (LivingEntity)e;
                if(entity.getLocation().clone().subtract(0, 0.1, 0).getBlock().getType() == Material.GOLD_BLOCK)
                {
                    if(entity != player)
                    {
                        if(t % 6 == 0)
                            entity.damage(2.6);
                    }
                    else if(entity == player)
                    {
                        if(t % 48 == 0)
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 0));
                    }
                }
            }
        }

        if(++t > 180 && !converted.isEmpty())
        {
            for(int i = 0; i < converted.size()/10; i++)
            {
                int index = (int)(Math.random() * converted.size());
                converted.get(index).setBlockData(oldBlocks.get(index));
                converted.remove(index);
                oldBlocks.remove(index);
            }
        }

        if(t > 240)
        {
            for(int i = 0; i < converted.size(); i++)
            {
                converted.get(i).setBlockData(oldBlocks.get(i));
            }

            cancel = true;
        }
    }

    private void spread()
    {
        if(size > 10)
            return;
        size++;

        blocks = Psychis.getSphereBlocks(start, size);
        List<Block> gold = new ArrayList<Block>();
        for(int i = 0; i < blocks.size(); i++)
        {
            Block block = blocks.get(i);
            if(!converted.contains(block) && !(block.getState() instanceof Container) && !block.getType().equals(Material.DRAGON_EGG))
            {
                oldBlocks.add(block.getBlockData());
                converted.add(block);

                if(block.isPassable() && block.getType() != Material.AIR)
                {
                    block.setType(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
                }

                if(!block.isPassable())
                gold.add(block);
            }
        }

        for(Block block : gold)
            block.setType(Material.GOLD_BLOCK);

        start.getWorld().playSound(start, Sound.BLOCK_NETHER_GOLD_ORE_BREAK, 2, 1);
    }

    @EventHandler
    public void preventGoldMining(BlockBreakEvent event)
    {
        event.setCancelled(event.getBlock().getType().equals(Material.GOLD_BLOCK));
    }

    @Override
    public void onCancel()
    {
        midas.deadly = false;
        HandlerList.unregisterAll(this);
    }
}
