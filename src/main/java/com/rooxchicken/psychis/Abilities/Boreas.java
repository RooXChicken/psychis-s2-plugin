package com.rooxchicken.psychis.Abilities;

import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.rooxchicken.psychis.Psychis;
import com.rooxchicken.psychis.Tasks.Boreas_Vortex;

public class Boreas extends Ability
{
    private Psychis plugin;
    private Player player;

    private double ticks = 0;
    private boolean jump;
    private boolean jumpEffect;

    private Boreas_Vortex vortex;
    private boolean launched = false;

    private NamespacedKey sniperDuelKey;
    private NamespacedKey cavesAndCliffsKey;
    private NamespacedKey starTraderKey;

    public Boreas(Psychis _plugin, Player _player)
    {
        super(_plugin, _player);
        plugin = _plugin;
        player = _player;

        type = 3;
        name = "Boreas";

        cooldown1 = 30;
        cooldown2 = 60;

        sniperDuelKey = new NamespacedKey(_plugin, "boreas_advSniperDuel");
        cavesAndCliffsKey = new NamespacedKey(_plugin, "boreas_advCavesAndCliffs");
        starTraderKey = new NamespacedKey(_plugin, "boreas_advStarTrader");

        PersistentDataContainer data = player.getPersistentDataContainer();

        if(!data.has(cavesAndCliffsKey, PersistentDataType.BOOLEAN))
            data.set(cavesAndCliffsKey, PersistentDataType.BOOLEAN, false);

        if(!data.has(sniperDuelKey, PersistentDataType.BOOLEAN))
            data.set(sniperDuelKey, PersistentDataType.BOOLEAN, false);

        if(!data.has(starTraderKey, PersistentDataType.BOOLEAN))
            data.set(starTraderKey, PersistentDataType.BOOLEAN, false);
    }

    @Override
    public void passive()
    {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 21, 0, true));
        if(jumpEffect)
        {
            player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 5, 0.05, 0.05, 0.05, new Particle.DustOptions(Color.WHITE, 1f));
            if(player.getVelocity().getY() < 0)
                jumpEffect = false;
        }
    }

    @Override
    public void activateFirstAbility(int state)
    {
        if(state == 0)
        {
            if(!plugin.setCooldown(player, cooldown1, Psychis.ability1CooldownKey))
                return;
            player.setVelocity(player.getLocation().getDirection().multiply(-2.1));
            player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 100, 0.5f, 0.2f, 0.5f, new Particle.DustOptions(Color.WHITE, 1f));
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BIG_DRIPLEAF_FALL, 1, 1);
            jump = true;
            jumpEffect = true;
        }
    }
    
    @Override
    public void activateSecondAbility(int state)
    {
        if(!plugin.secondUnlocked(player))
            return;
        if(vortex != null && vortex.cancel)
            vortex = null;
        
        if(state == 0)
        {
            if(!plugin.setCooldown(player, cooldown2, Psychis.ability2CooldownKey))
                return;

            vortex = new Boreas_Vortex(plugin, player);
            Psychis.tasks.add(vortex);
            launched = false;
        }
        if(launched)
            return;
        
        if(vortex != null && state == 1)
            vortex.setPosition();
        else if(vortex != null && state == 2)
        {
            vortex.resetTimer();
            launched = true;
        }
    }

    @EventHandler
    public void cancelFallIfBoots(EntityDamageEvent event)
    {
        if(event.getCause() != DamageCause.FALL || event.getEntityType() != EntityType.PLAYER)
            return;

        if(jump)
        {
            jump = false;
            jumpEffect = false;
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void checkSecondUnlock(PlayerAdvancementDoneEvent event)
    {
        if(event.getPlayer() != player)
            return;

        PersistentDataContainer data = player.getPersistentDataContainer();

        NamespacedKey key = event.getAdvancement().getKey();
        if(key.getNamespace().equals(NamespacedKey.MINECRAFT))
        {
            if(key.getKey().equals("adventure/fall_from_world_height"))
                data.set(cavesAndCliffsKey, PersistentDataType.BOOLEAN, true);
            else if(key.getKey().equals("adventure/sniper_duel"))
                data.set(sniperDuelKey, PersistentDataType.BOOLEAN, true);
            else if(key.getKey().equals("adventure/trade_at_world_height"))
                data.set(starTraderKey, PersistentDataType.BOOLEAN, true);
        }

        if(data.get(cavesAndCliffsKey, PersistentDataType.BOOLEAN) && data.get(sniperDuelKey, PersistentDataType.BOOLEAN) && data.get(starTraderKey, PersistentDataType.BOOLEAN))
        {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement revoke " + player.getName() + " only minecraft:adventure/fall_from_world_height");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement revoke " + player.getName() + " only minecraft:adventure/sniper_duel");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement revoke " + player.getName() + " only minecraft:adventure/trade_at_world_height");

            data.set(cavesAndCliffsKey, PersistentDataType.BOOLEAN, false);
            data.set(sniperDuelKey, PersistentDataType.BOOLEAN, false);
            data.set(starTraderKey, PersistentDataType.BOOLEAN, false);
            
            if(!plugin.secondUnlocked(player))
                plugin.unlockSecondAbility(player);
        }
    }

    public Advancement getAdvancement(String name)
    {
        Iterator<Advancement> it = Bukkit.getServer().advancementIterator();
        // gets all 'registered' advancements on the server.
        while (it.hasNext()) {
            // loops through these.
            Advancement a = it.next();
            if (a.getKey().toString().equalsIgnoreCase(name)) {
                //checks if one of these has the same name as the one you asked for. If so, this is the one it will return.
                return a;
            }
        }
        return null;
    }
}
