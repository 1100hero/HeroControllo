package org.hero.herocontrollo.thread;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hero.herocontrollo.HeroControllo;
import org.hero.herocontrollo.commands.Controllo;
import org.hero.herocontrollo.utils.ColorAPI;

public class Task {
    public static void expireTask(Player player){
        HeroControllo plugin = HeroControllo.getPlugin(HeroControllo.class);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if(!Controllo.getMap().containsKey(player.getUniqueId())) return;
                Player p = Bukkit.getPlayer(Controllo.getMap().keySet().stream().filter(pl -> pl.equals(player.getUniqueId())).findAny().get());
                if(p != null)
                    p.sendMessage(ColorAPI.color(plugin.getConfig().getString("messages.expiredRequest")));
                if(player != null)
                    player.sendMessage(ColorAPI.color(plugin.getConfig().getString("messages.expiredRequest")));
                Controllo.getMap().remove(player.getUniqueId());
        },plugin.getConfig().getInt("cooldown.durationExpire")*20L);
    }
}
