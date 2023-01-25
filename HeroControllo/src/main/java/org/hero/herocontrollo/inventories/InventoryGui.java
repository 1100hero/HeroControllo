package org.hero.herocontrollo.inventories;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.hero.herocontrollo.HeroControllo;
import org.hero.herocontrollo.utils.ColorAPI;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InventoryGui {
     private final Inventory inventory;
     private HeroControllo plugin = HeroControllo.getPlugin(HeroControllo.class);
     public InventoryGui(Player player){
         inventory = Bukkit.createInventory(null, 36,
                 ColorAPI.color(Objects.requireNonNull(plugin.getConfig().getString("gui.playerInvName")).replace("%target%", player.getName())));
         putItems(player);
     }
     private void putItems(@NotNull Player player){
         this.inventory.setContents(player.getInventory().getContents());
     }
     public void openInventory(final HumanEntity entity){
         entity.openInventory(inventory);
     }
}
