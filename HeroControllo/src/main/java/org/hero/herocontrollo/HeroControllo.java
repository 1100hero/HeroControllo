package org.hero.herocontrollo;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.hero.herocontrollo.commands.Controllo;
import org.hero.herocontrollo.events.DeathEvent;
import org.hero.herocontrollo.events.InventoryEvent;

import java.util.Objects;

public final class HeroControllo extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Objects.requireNonNull(this.getCommand("controllo")).setExecutor(new Controllo());
        this.getServer().getPluginManager().registerEvents(new InventoryEvent(), this);
        this.getServer().getPluginManager().registerEvents(new DeathEvent(), this);
        Bukkit.getLogger().info("HeroControllo abilitato.");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("HeroControllo disabilitato.");
    }
}
