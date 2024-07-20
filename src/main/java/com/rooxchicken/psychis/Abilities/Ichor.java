package com.rooxchicken.psychis.Abilities;

import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityToggleSwimEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.rooxchicken.psychis.Psychis;
import com.rooxchicken.psychis.Tasks.Boreas_Vortex;
import com.rooxchicken.psychis.Tasks.Ichor_BloodRay;
import com.rooxchicken.psychis.Tasks.Ichor_FangSlide;
import com.rooxchicken.psychis.Tasks.Task;

public class Ichor extends Ability
{
    private Psychis plugin;
    private Player player;

    private double ticks = 0;
    private boolean jump;
    private boolean jumpEffect;

    private Boreas_Vortex vortex;
    private boolean launched = false;

    private NamespacedKey mobsKilledKey;
    private boolean shouldCrawl = false;

    private Ichor_FangSlide fangSlide;
    private Ichor_BloodRay bloodRay;

    public Ichor(Psychis _plugin, Player _player)
    {
        super(_plugin, _player);
        plugin = _plugin;
        player = _player;

        type = 7;
        name = "Ichor";

        cooldown1 = 45;
        cooldown2 = 80;

        mobsKilledKey = new NamespacedKey(_plugin, "ichor_mobsKilled");
        PersistentDataContainer data = player.getPersistentDataContainer();

        if(!data.has(mobsKilledKey, PersistentDataType.INTEGER))
            data.set(mobsKilledKey, PersistentDataType.INTEGER, 0);
    }

    @Override
    public void passive()
    {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2, 0));
        if(player.getWorld().getTime() % 24000 < 13000)
            player.getPersistentDataContainer().set(mobsKilledKey, PersistentDataType.INTEGER, 0);

        if(fangSlide != null && fangSlide.cancel)
        {
            plugin.setCooldown(player, cooldown1, Psychis.ability1CooldownKey);
            fangSlide = null;
        }

        if(bloodRay != null && bloodRay.cancel)
        {
            bloodRay = null;
        }
    }

    @Override
    public void activateFirstAbility(int state)
    {
        if(state == 0)
        {
            if(fangSlide != null)
            {
                fangSlide.cancel = true;
                return;
            }

            if(plugin.getCooldown(player, Psychis.ability1CooldownKey) <= 0)
            {
                fangSlide = new Ichor_FangSlide(plugin, player);
                Psychis.tasks.add(fangSlide);
            }
        }
    }
    
    @Override
    public void activateSecondAbility(int state)
    {
        if(!plugin.secondUnlocked(player))
            return;

        if(bloodRay == null && plugin.getCooldown(player, Psychis.ability2CooldownKey) <= 0)
        {
            bloodRay = new Ichor_BloodRay(plugin, player);
            Psychis.tasks.add(bloodRay);
        }
    }

    @EventHandler
    public void checkSecondUnlock(EntityDeathEvent event)
    {
        if(plugin.secondUnlocked(player))
            return;

        if(!(event.getEntity() instanceof LivingEntity))
            return;

        LivingEntity entity = event.getEntity();
        PersistentDataContainer data = player.getPersistentDataContainer();

        if(entity.getKiller() != player)
            return;

        int kills = data.get(mobsKilledKey, PersistentDataType.INTEGER) + 1;
        if(kills >= 40)
        {
            plugin.unlockSecondAbility(player);
            data.set(mobsKilledKey, PersistentDataType.INTEGER, 0);
            return;
        }

        data.set(mobsKilledKey, PersistentDataType.INTEGER, kills);
    }
}
