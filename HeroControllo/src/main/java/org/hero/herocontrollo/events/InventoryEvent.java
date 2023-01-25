package org.hero.herocontrollo.events;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.hero.herocontrollo.HeroControllo;
import org.hero.herocontrollo.inventories.InventoryGui;
import org.hero.herocontrollo.inventories.ShulkerGui;
import org.hero.herocontrollo.utils.ColorAPI;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class InventoryEvent implements Listener {

    private final HeroControllo plugin = HeroControllo.getPlugin(HeroControllo.class);
    @Getter private static final HashMap<UUID, UUID> map = new HashMap<>();
    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventory(final InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if(!getMap().containsKey(player.getUniqueId())) return;
        String targetName = Objects.requireNonNull(Bukkit.getPlayer(getMap().get(player.getUniqueId()))).getName();
        if(player.getOpenInventory().getTitle().equalsIgnoreCase(ColorAPI.color(Objects.requireNonNull(plugin.getConfig().getString("gui.playerInvName"))
                .replace("%target%", targetName)))
                || player.getOpenInventory().getTitle().equalsIgnoreCase(ColorAPI.color(Objects.requireNonNull(plugin.getConfig().getString("gui.shulkerInv.name"))
                .replace("%target%", targetName)))) {
            event.setCancelled(true);
            if(event.getCurrentItem() == null) return;
            if(Objects.requireNonNull(event.getCurrentItem()).getType() != Material.SHULKER_BOX) return;

            new ShulkerGui(player, event.getCurrentItem()).openInventory(player);
            return;
        }
        getMap().remove(player.getUniqueId());
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onMenuclick(final InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if(event.getCurrentItem() == null) return;
        if(!Objects.requireNonNull(event.getCurrentItem()).hasItemMeta()) return;
        if(!event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ColorAPI.color(plugin.getConfig().getString("gui.shulkerInv.homeButtonName")))) return;
        if(!getMap().containsKey(player.getUniqueId())) return;
        player.closeInventory();
        new InventoryGui(Objects.requireNonNull(Bukkit.getPlayer(getMap().get(player.getUniqueId())))).openInventory(player);
    }
}
