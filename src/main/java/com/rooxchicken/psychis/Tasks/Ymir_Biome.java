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
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.rooxchicken.psychis.Psychis;

public class Ymir_Biome extends Task
{
    private Player player;
    private Location start;
    private int t;

    private int size = 0;
    private ArrayList<Block> converted;
    private ArrayList<BlockData> oldBlocks;
    List<Block> blocks;

    public Ymir_Biome(Psychis _plugin, Player _player)
    {
        super(_plugin);
        player = _player;
        start = player.getLocation().clone();
        tickThreshold = 1;

        converted = new ArrayList<Block>();
        oldBlocks = new ArrayList<BlockData>();
    }

    @Override
    public void run()
    {
        spread();

        for(Object e : Psychis.getNearbyEntities(start, size))
        {
            if(e instanceof LivingEntity && e != player)
            {
                LivingEntity entity = (LivingEntity)e;
                entity.setFreezeTicks(200);

                if(t % 20 == 0)
                    entity.damage(3, player);
            }
        }

        start.getWorld().spawnParticle(Particle.SNOWFLAKE, start, size*6, size/2.0, size/2.0, size/2.0, 0);

        if(++t > 180)
            cancel = true;
    }

    private void spread()
    {
        if(size > 12)
            return;
        size++;

        blocks = Psychis.getSphereBlocks(start, size);
        List<Block> snow = new ArrayList<Block>();
        for(int i = 0; i < blocks.size(); i++)
        {
            Block block = blocks.get(i);
            if(!converted.contains(block) && !(block.getState() instanceof Container))
            {
                oldBlocks.add(block.getBlockData());
                converted.add(block);

                if(block.isPassable() && block.getType() != Material.AIR)
                {
                    block.setType(Material.SNOW);
                }

                if(!block.isPassable())
                    snow.add(block);
            }
        }

        for(Block block : snow)
            block.setType(Material.SNOW_BLOCK);

        start.getWorld().playSound(start, Sound.BLOCK_AMETHYST_BLOCK_HIT, 2, 1);
    }

    @Override
    public void onCancel()
    {
        for(int i = 0; i < converted.size(); i++)
        {
            converted.get(i).setBlockData(oldBlocks.get(i));
        }
    }
}
