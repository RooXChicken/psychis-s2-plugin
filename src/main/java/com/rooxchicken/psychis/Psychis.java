package com.rooxchicken.psychis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.filefilter.CanExecuteFileFilter;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.google.common.base.Predicate;
import com.rooxchicken.psychis.Abilities.Ability;
import com.rooxchicken.psychis.Abilities.Agni;
import com.rooxchicken.psychis.Abilities.Boreas;
import com.rooxchicken.psychis.Abilities.Dolus;
import com.rooxchicken.psychis.Abilities.Enil;
import com.rooxchicken.psychis.Abilities.Varuna;
import com.rooxchicken.psychis.Abilities.Ymir;
import com.rooxchicken.psychis.Commands.FirstAbility;
import com.rooxchicken.psychis.Commands.ParticleTest;
import com.rooxchicken.psychis.Commands.ResetCooldown;
import com.rooxchicken.psychis.Commands.SecondAbility;
import com.rooxchicken.psychis.Commands.SetAbility;
import com.rooxchicken.psychis.Tasks.CooldownTask;
import com.rooxchicken.psychis.Tasks.Task;

public class Psychis extends JavaPlugin implements Listener
{
    public static NamespacedKey abilityKey;

    public static NamespacedKey ability1CooldownKey;
    public static NamespacedKey ability2CooldownKey;

    private HashMap<Player, Ability> playerAbilities;
    private HashMap<Player, Player> damagedPlayers;

    public static ArrayList<Task> tasks;

    @Override
    public void onEnable()
    {
        tasks = new ArrayList<Task>();
        tasks.add(new CooldownTask(this));

        abilityKey = new NamespacedKey(this, "abilityKey");

        ability1CooldownKey = new NamespacedKey(this, "ability1Cooldown");
        ability2CooldownKey = new NamespacedKey(this, "ability2Cooldown");

        playerAbilities = new HashMap<Player, Ability>();
        damagedPlayers = new HashMap<Player, Player>();

        getServer().getPluginManager().registerEvents(this, this);
        
        this.getCommand("hdn_ability1_srt").setExecutor(new FirstAbility(this, 0));
        this.getCommand("hdn_ability2_srt").setExecutor(new SecondAbility(this, 0));

        this.getCommand("hdn_ability1_rpt").setExecutor(new FirstAbility(this, 1));
        this.getCommand("hdn_ability2_rpt").setExecutor(new SecondAbility(this, 1));

        this.getCommand("hdn_ability1_end").setExecutor(new FirstAbility(this, 2));
        this.getCommand("hdn_ability2_end").setExecutor(new SecondAbility(this, 2));

        this.getCommand("ptest").setExecutor(new ParticleTest(this));
        this.getCommand("resetcooldown").setExecutor(new ResetCooldown(this));
        this.getCommand("setability").setExecutor(new SetAbility(this));

        for(Player player : getServer().getOnlinePlayers())
        {
            addPlayerAbility(player);
        }

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
        {
            public void run()
            {
                ArrayList<Task> _tasks = new ArrayList<Task>();
                for(Task t : tasks)
                    _tasks.add(t);
                
                ArrayList<Task> toRemove = new ArrayList<Task>();

                for(Ability a : playerAbilities.values())
                    a.passive();

                for(Task t : _tasks)
                {
                    t.tick();

                    if(t.cancel)
                        toRemove.add(t);
                }

                for(Task t : toRemove)
                {
                    t.onCancel();
                    tasks.remove(t);
                }

                damagedPlayers.clear();
            }
        }, 0, 1);

        getLogger().info("Supports up to 1987 custom keybinds! (made by roo)");
    }

    @EventHandler
    private void addPlayerAbility(PlayerJoinEvent e)
    {
        addPlayerAbility(e.getPlayer());
    }

    public void addPlayerAbility(Player player)
    {
        if(playerAbilities.containsKey(player))
            playerAbilities.remove(player);
        PersistentDataContainer data = player.getPersistentDataContainer();

        if(!data.has(abilityKey, PersistentDataType.INTEGER))
            data.set(abilityKey, PersistentDataType.INTEGER, 1);
        
        playerAbilities.put(player, nameToAbility(player, data.get(abilityKey, PersistentDataType.INTEGER)));
        sendPlayerData(player, "0_" + data.get(abilityKey, PersistentDataType.INTEGER));
            
    }

    @EventHandler
    private void removePlayerAbility(PlayerQuitEvent e)
    {
        if(playerAbilities.containsKey(e.getPlayer()))
            playerAbilities.remove(e.getPlayer());
    }

    @EventHandler
	private void onPlayerTab(PlayerCommandSendEvent e)
    {
		List<String> blockedCommands = new ArrayList<>();
		blockedCommands.add("hdn_ability1");
		blockedCommands.add("hdn_ability2");
		e.getCommands().removeAll(blockedCommands);
	}
    
    @EventHandler
    private void PreventKick(PlayerKickEvent event)
    {
        if(event.getReason().equals("Kicked for spamming"))
            event.setCancelled(true);
    }

    @EventHandler
    public void addToDeathMessage(EntityDamageByEntityEvent event)
    {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player))
            return;

        damagedPlayers.put((Player)event.getEntity(), (Player)event.getDamager());
    }

    @EventHandler
    public void customDeathMessages(PlayerDeathEvent event)
    {
        if(!damagedPlayers.containsKey(event.getEntity()))
            return;

        if(getPlayerAbility(damagedPlayers.get(event.getEntity())).deadly)
        {
            switch(getPlayerAbility(damagedPlayers.get(event.getEntity())).type)
            {
                case 1:
                    event.setDeathMessage(event.getEntity().getName() + " got torched by " + damagedPlayers.get(event.getEntity()).getName());
                    break;
                case 2:
                    event.setDeathMessage(event.getEntity().getName() + " got shocked by " + damagedPlayers.get(event.getEntity()).getName());
                    break;
                case 4:
                    event.setDeathMessage(event.getEntity().getName() + " couldn't handle the cold");
                    break;
                case 5:
                    event.setDeathMessage(event.getEntity().getName() + " got crushed by " + damagedPlayers.get(event.getEntity()).getName());
                    break;
            }
        }
    }

    public Ability getPlayerAbility(Player player)
    {
        return playerAbilities.get(player);
    }

    public static Entity getTarget(Player player, int range)
    {
        Predicate<Entity> p = new Predicate<Entity>() {

            @Override
            public boolean apply(Entity input)
            {
                return(input != player);
            }
            
        };
        RayTraceResult ray = player.getWorld().rayTrace(player.getEyeLocation(), player.getLocation().getDirection(), range, FluidCollisionMode.NEVER, true, 0.2, p);
        
        if(ray != null)
            return ray.getHitEntity();
        else
            return null;
    }

    public static Block getBlock(Player player, int range)
    {
        return getBlock(player, range, player.getLocation().getPitch());
    }

    public static Block getBlock(Player player, int range, float pitch)
    {
        Predicate<Entity> p = new Predicate<Entity>() {

            @Override
            public boolean apply(Entity input)
            {
                return(input != player);
            }
            
        };

        Location dir = player.getLocation().clone();
        dir.setPitch(pitch);

        RayTraceResult ray = player.getWorld().rayTrace(player.getEyeLocation(), dir.getDirection(), range, FluidCollisionMode.NEVER, true, 0.2, p);
        
        if(ray != null)
            return ray.getHitBlock();
        else
            return null;
    }

    public static List<Block> getSphereBlocks(Location location, int radius)
    {
        List<Block> blocks = new ArrayList<>();

        int bx = location.getBlockX();
        int by = location.getBlockY();
        int bz = location.getBlockZ();

        for (int x = bx - radius; x <= bx + radius; x++)
        {
            for (int y = by - radius; y <= by + radius; y++)
            {
                for (int z = bz - radius; z <= bz + radius; z++)
                {
                    double distance = ((bx - x) * (bx - x) + (bz - z) * (bz - z) + (by - y) * (by - y));
                    if (distance < radius * radius && (distance < (radius - 1) * (radius - 1)))
                    {
                        Block block = new Location(location.getWorld(), x, y, z).getBlock();
                        if(block.getType() != Material.AIR && block.getType() != Material.BEDROCK)
                            blocks.add(block);
                    }
                }
            }
        }

        return blocks;
    }

    public static Object[] getNearbyEntities(Location where, int range)
    {
        return where.getWorld().getNearbyEntities(where, range, range, range).toArray();
    }

    private Ability nameToAbility(Player player, int abilityName)
    {
        switch(abilityName)
        {
            case 0:
                return new Varuna(this, player);
            case 1:
                return new Agni(this, player);
            case 2:
                return new Enil(this, player);
            case 3:
                return new Boreas(this, player);
            case 4:
                return new Ymir(this, player);
            case 5:
                return new Dolus(this, player);

            default:
                return null;
        }
    }

    /*
     * GUIDE:
     * _ seperates parts of data
     * 
     * 1: psyz91_ marks the signature for data
     * 2: mode: (0 is for login. marks ability type) (1 marks ability cooldown)
     * 2.0_1: marks ability type
     * 2.1_1: ability type (0 for primary, 1 for unlockable)
     * 2.1_2: cooldown length (in ticks)
     */
    public static void sendPlayerData(Player player, String data)
    {
        player.sendMessage("psyz91_" + data);
    }

    public void checkHasCooldown(PersistentDataContainer data)
    {
        if(!data.has(Psychis.ability1CooldownKey, PersistentDataType.INTEGER))
            data.set(Psychis.ability1CooldownKey, PersistentDataType.INTEGER, 0);

        if(!data.has(Psychis.ability2CooldownKey, PersistentDataType.INTEGER))
            data.set(Psychis.ability2CooldownKey, PersistentDataType.INTEGER, 0);
    }

    public boolean checkCooldown(Player player, NamespacedKey key)
    {
        PersistentDataContainer data = player.getPersistentDataContainer();
        checkHasCooldown(data);
        if(data.get(key, PersistentDataType.INTEGER) > 0)
            return false;

        return true;
    }

    public boolean setCooldown(Player player, int cooldown, NamespacedKey key)
    {
        PersistentDataContainer data = player.getPersistentDataContainer();
        checkHasCooldown(data);
        if(data.get(key, PersistentDataType.INTEGER) > 0)
            return false;

        setCooldownForce(player, cooldown, key);
        return true;
    }

    public void setCooldownForce(Player player, int cooldown, NamespacedKey key)
    {
        PersistentDataContainer data = player.getPersistentDataContainer();
        data.set(key, PersistentDataType.INTEGER, cooldown*2);
    }

    public static double ClampD(double v, double min, double max)
    {
        if(v < min)
            return min;
        else if(v > max)
            return max;
        else
            return v;
    }
}
