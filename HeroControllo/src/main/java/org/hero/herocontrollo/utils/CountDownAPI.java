package org.hero.herocontrollo.utils;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
public class CountDownAPI {

    private final static Map<String, CountDownAPI> cooldowns = new HashMap<>();

    private UUID uuid;
    private String cooldownName;
    private long start;
    private int timeInSeconds;

    public CountDownAPI(Player player, String cooldownName, int timeInSeconds) {
        this.uuid = player.getUniqueId();
        this.cooldownName = cooldownName;
        this.timeInSeconds = timeInSeconds;
    }

    public void start() {
        this.start = System.currentTimeMillis();
        cooldowns.put(uuid + cooldownName, this);
    }

    //

    private static void stop(Player player, String cooldownName) {
        cooldowns.remove(player.getUniqueId() + cooldownName);
    }

    private static CountDownAPI getCooldown(Player player, String cooldownName) {
        return cooldowns.get(player.getUniqueId() + cooldownName);
    }

    public static int getTimeLeft(Player player, String cooldownName) {
        CountDownAPI cooldown = getCooldown(player, cooldownName);
        int f = -1;
        if (cooldown != null) {
            long now = System.currentTimeMillis();
            long cooldownTime = cooldown.start;
            int totalTime = cooldown.timeInSeconds;
            int r = (int) (now - cooldownTime) / 1000;
            f = (r - totalTime) * -1;
        }
        return f;
    }

    public static boolean isInCooldown(Player player, String cooldownName) {
        if (getTimeLeft(player, cooldownName) >= 1) {
            return true;
        }
        stop(player, cooldownName);
        return false;
    }
}