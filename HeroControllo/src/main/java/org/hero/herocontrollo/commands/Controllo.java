package org.hero.herocontrollo.commands;

import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.hero.herocontrollo.HeroControllo;
import org.hero.herocontrollo.events.InventoryEvent;
import org.hero.herocontrollo.inventories.InventoryGui;
import org.hero.herocontrollo.thread.Task;
import org.hero.herocontrollo.utils.ColorAPI;
import org.hero.herocontrollo.utils.CountDownAPI;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Controllo implements CommandExecutor, Listener {

    @Getter private static final LinkedHashMap<UUID, List<UUID>> map = new LinkedHashMap<>();
    @Getter private static final List<UUID> playerList = new ArrayList<>();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        HeroControllo plugin = HeroControllo.getPlugin(HeroControllo.class);
        if(!(sender instanceof Player)){
            sender.sendMessage(Objects.requireNonNull(ColorAPI.color(plugin.getConfig().getString("wrong-messages.consoleInstance"))));
            return true;
        }
        Player player = (Player) sender;
        if(args.length != 1){
            player.sendMessage(ColorAPI.color(plugin.getConfig().getString("wrong-messages.length")));
            return true;
        }
        if(args[0].equalsIgnoreCase("accetta")){
            if(!getMap().containsKey(player.getUniqueId())){
                player.sendMessage(ColorAPI.color(plugin.getConfig().getString("wrong-messages.noRequest")));
                return true;
            }
            if(!player.hasPermission("controllo.accetta")){
                getMap().remove(player.getUniqueId());
                player.sendMessage(ColorAPI.color(plugin.getConfig().getString("wrong-messages.noPermission")));
                return true;
            }
            Player p = Bukkit.getPlayer(getMap().keySet().stream().filter(pl -> pl.equals(player.getUniqueId())).findAny().get());
            if(p == null) return true;
            Objects.requireNonNull(p).playSound(p.getLocation(), Sound.valueOf(plugin.getConfig().getString("sounds.accepted.sound")),
                    plugin.getConfig().getInt("sounds.accepted.sound"),
                    plugin.getConfig().getInt("sounds.accepted.sound"));
            p.sendMessage(ColorAPI.color(plugin.getConfig().getString("messages.acceptedToPlayer"))
                    .replace("%target%", player.getName()));
            player.sendMessage(ColorAPI.color(plugin.getConfig().getString("messages.acceptedToTarget")));
            InventoryEvent.getMap().put(p.getUniqueId(), player.getUniqueId());
            new InventoryGui(player).openInventory(p);
            getMap().remove(player.getUniqueId());
        }else if (args[0].equalsIgnoreCase("rifiuta")){
            if(!getMap().containsKey(player.getUniqueId())){
                player.sendMessage(ColorAPI.color(plugin.getConfig().getString("wrong-messages.noRequest")));
                return true;
            }
            if(!player.hasPermission("controllo.rifiuta")){
                getMap().remove(player.getUniqueId());
                player.sendMessage(ColorAPI.color(plugin.getConfig().getString("wrong-messages.noPermission")));
                return true;
            }
            Player p = Bukkit.getPlayer(getMap().keySet().stream().filter(pl -> pl.equals(player.getUniqueId())).findAny().get());
            if(p == null) return true;
            Objects.requireNonNull(p).playSound(p.getLocation(), Sound.valueOf(plugin.getConfig().getString("sounds.rejected.sound")),
                    plugin.getConfig().getInt("sounds.rejected.sound"),
                    plugin.getConfig().getInt("sounds.rejected.sound"));
            p.sendMessage(ColorAPI.color(plugin.getConfig().getString("messages.rejectedToPlayer"))
                    .replace("%target%", player.getName()));
            player.sendMessage(ColorAPI.color(plugin.getConfig().getString("messages.rejectedToTarget")));
            if(getMap().get(p.getUniqueId()).size() == 1){
                getMap().remove(p.getUniqueId());
            }else{
                getMap().get(p.getUniqueId()).remove(player.getUniqueId());
            }
        }else{
            if(!player.hasPermission("controllo.richiedi")){
                player.sendMessage(ColorAPI.color(plugin.getConfig().getString("wrong-messages.noPermission")));
                return true;
            }
            if(Bukkit.getPlayerExact(args[0]) == null){
                player.sendMessage(ColorAPI.color(plugin.getConfig().getString("wrong-messages.noTarget"))
                        .replace("%target%", args[0]));
                return true;
            }
            Player target = Bukkit.getPlayerExact(args[0]);
            if(player == target){
                player.sendMessage(ColorAPI.color(plugin.getConfig().getString("wrong-messages.selfCmd")));
                return true;
            }
            if(CountDownAPI.isInCooldown(player, player.getName())){
                player.sendMessage(ColorAPI.color(plugin.getConfig().getString("cooldown.inCooldownMessage"))
                        .replace("%time%", String.valueOf(CountDownAPI.getTimeLeft(player, player.getName()))));
                return true;
            }
            if(player.getLocation().distance(Objects.requireNonNull(target).getLocation()) > plugin.getConfig().getInt("distance")){
                player.sendMessage(ColorAPI.color(plugin.getConfig().getString("wrong-messages.wayToFar"))
                        .replace("%target%", args[0]));
                return true;
            }
            if(player.getInventory().getItemInMainHand().getType() != Material.CROSSBOW && !player.hasPermission("controllo.bypass")){
                player.sendMessage(ColorAPI.color(plugin.getConfig().getString("wrong-messages.noWeaponInHand")));
                return true;
            }
            String name = player.getName();
            if(player.getInventory().getHelmet() != null){
                if(player.getInventory().getHelmet().hasItemMeta()){
                    if(player.getInventory().getHelmet().getItemMeta().getCustomModelData() == 57){
                        name = ColorAPI.color(plugin.getConfig().getString("anonymousName"));
                    }
                }
            }
            if(!getMap().containsKey(target.getUniqueId())){
                playerList.add(player.getUniqueId());
                getMap().put(Objects.requireNonNull(target).getUniqueId(), playerList);
                playerList.remove(player.getUniqueId());
            }else{
                getMap().get(target.getUniqueId()).add(player.getUniqueId());
            }
            player.sendMessage(ColorAPI.color(plugin.getConfig().getString("messages.sentRequest"))
                    .replace("%target%", args[0]));
            target.sendMessage(ColorAPI.color(plugin.getConfig().getString("messages.receivedRequest"))
                    .replace("%player%", name));
            TextComponent yesMessage = new TextComponent(ColorAPI.color(plugin.getConfig().getString("messages.clickableButtons.yesMessage")));
            yesMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new Text(ColorAPI.color(Objects.requireNonNull(plugin.getConfig().getString("messages.clickableButtons.yesHover")))
                            .replace("%player%", name))));
            yesMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/controllo accetta"));
            TextComponent noMessage = new TextComponent(ColorAPI.color(plugin.getConfig().getString("messages.clickableButtons.noMessage")));
            noMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new Text(ColorAPI.color(Objects.requireNonNull(plugin.getConfig().getString("messages.clickableButtons.noHover")))
                            .replace("%player%", name))));
            noMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/controllo rifiuta"));
            target.spigot().sendMessage(yesMessage, new TextComponent(" "), noMessage);
            Task.expireTask(target);
            new CountDownAPI(player, player.getName(), plugin.getConfig().getInt("cooldown.duration")).start();
        }
        return true;
    }
}
