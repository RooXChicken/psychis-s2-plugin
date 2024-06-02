package com.rooxchicken.psychis.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.rooxchicken.psychis.Psychis;

public class CooldownTask extends Task
{
    Psychis plugin;

    public CooldownTask(Psychis _plugin)
    {
        super(_plugin);
        plugin = _plugin;
        tickThreshold = 10;
    }

    @Override
    public void run()
    {
        for(Player player : plugin.hasMod)
        {
            if(plugin.getPlayerAbility(player) != null)
            {
                PersistentDataContainer data = player.getPersistentDataContainer();

                plugin.checkHasCooldown(data);

                int cooldown1 = data.get(Psychis.ability1CooldownKey, PersistentDataType.INTEGER);
                int cooldown2 = data.get(Psychis.ability2CooldownKey, PersistentDataType.INTEGER);

                if(cooldown1 > 0)
                    cooldown1--;
                if(cooldown2 > 0)
                    cooldown2--;

                data.set(Psychis.ability1CooldownKey, PersistentDataType.INTEGER, cooldown1);
                data.set(Psychis.ability2CooldownKey, PersistentDataType.INTEGER, cooldown2);

                Psychis.sendPlayerData(player, "1_0_" + (cooldown1/2) + "_" + plugin.getPlayerAbility(player).cooldown1);
                Psychis.sendPlayerData(player, "1_1_" + (cooldown2/2) + "_" + plugin.getPlayerAbility(player).cooldown2);
            }
        }
    }
}
