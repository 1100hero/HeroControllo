package org.hero.herocontrollo.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.hero.herocontrollo.HeroControllo;
import org.hero.herocontrollo.utils.ColorAPI;

import java.util.Objects;

public class ShulkerGui {

    private final Inventory inventory;
    private final HeroControllo plugin = HeroControllo.getPlugin(HeroControllo.class);
    public ShulkerGui(Player player, ItemStack item){
        BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
        ShulkerBox box = (ShulkerBox) meta.getBlockState();
        inventory = Bukkit.createInventory(null, 36, ColorAPI.color(Objects.requireNonNull(plugin.getConfig().getString("gui.shulkerInv.name"))));
        putContents(box);
        setHomeItem();
    }
    private void putContents(ShulkerBox box){
        inventory.setContents(box.getInventory().getContents());
    }
    private void setHomeItem(){
        ItemStack item = new ItemStack(Material.valueOf(plugin.getConfig().getString("gui.shulkerInv.block")));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ColorAPI.color(plugin.getConfig().getString("gui.shulkerInv.homeButtonName")));
        meta.setLore(ColorAPI.color(plugin.getConfig().getStringList("gui.shulkerInv.homeLoreButton")));
        meta.setCustomModelData(plugin.getConfig().getInt("gui.shulkerInv.data"));
        item.setItemMeta(meta);
        inventory.setItem(plugin.getConfig().getInt("gui.shulkerInv.position"), item);
    }
    public void openInventory(Player player){
        player.openInventory(inventory);
    }
}
