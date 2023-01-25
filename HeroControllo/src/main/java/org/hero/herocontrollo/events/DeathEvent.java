package org.hero.herocontrollo.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.hero.herocontrollo.HeroControllo;
import org.hero.herocontrollo.commands.Controllo;
import org.hero.herocontrollo.utils.ColorAPI;

import java.util.Objects;

public class DeathEvent implements Listener {

    @EventHandler
    public void onDeath(final PlayerDeathEvent event){
        HeroControllo plugin = HeroControllo.getPlugin(HeroControllo.class);
        Player player = event.getEntity();
        if(!Controllo.getMap().containsKey(player.getUniqueId())) return;
        Player p = Bukkit.getPlayer(Controllo.getMap().keySet().stream().filter(pl -> pl.equals(player.getUniqueId())).findAny().get());
        if(p != null)
            Objects.requireNonNull(p).sendMessage(ColorAPI.color(plugin.getConfig().getString("messages.expiredRequest")));
        if(player != null)
            player.sendMessage(ColorAPI.color(plugin.getConfig().getString("messages.expiredRequest")));
        Controllo.getMap().remove(player.getUniqueId());
    }
}
