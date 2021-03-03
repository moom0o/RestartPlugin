package me.moomoo.restartplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class RestartPlugin extends JavaPlugin implements Listener {
    FileConfiguration config = getConfig();
    boolean playerslow = false;

    public void onEnable() {
        saveDefaultConfig();
        System.out.println("[ENABLED] moomoo's 2b2t restart notifications plugin");
        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(get("Timezone")));
        ZonedDateTime nextRun = now.withHour(config.getInt("Hour")).withMinute(config.getInt("Minute")).withSecond(config.getInt("Seconds"));
        if (now.compareTo(nextRun) > 0)
            nextRun = nextRun.plusDays(1);

        Duration duration = Duration.between(now, nextRun);
        long initalDelay = duration.getSeconds();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            Thread t = new Thread(() -> {
                try {
                    restart();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });

            t.start();
        }, initalDelay, TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);
    }

    public void restart() throws InterruptedException {
        String s = translate(get("string"));
        b(translate(s + " 15" + get("minutestring")));
        sleep(300000);
        b(translate(s + " 10" + get("minutestring")));
        sleep(300000);
        b(translate(s + " 5" + get("minutestring")));
        sleep(180000);
        b(translate(s + " 2" + get("minutestring")));
        sleep(105000);
        b(translate(s + " 15" + get("secondsstring")));
        sleep(1000);
        b(translate(s + " 14" + get("secondsstring")));
        sleep(1000);
        b(translate(s + " 13" + get("secondsstring")));
        sleep(1000);
        b(translate(s + " 12" + get("secondsstring")));
        sleep(1000);
        b(translate(s + " 11" + get("secondsstring")));
        sleep(1000);
        b(translate(s + " 10" + get("secondsstring")));
        sleep(1000);
        b(translate(s + " 9" + get("secondsstring")));
        sleep(1000);
        b(translate(s + " 8" + get("secondsstring")));
        sleep(1000);
        b(translate(s + " 7" + get("secondsstring")));
        sleep(1000);
        b(translate(s + " 6" + get("secondsstring")));
        sleep(1000);
        b(translate(s + " 5" + get("secondsstring")));
        sleep(1000);
        b(translate(s + " 4" + get("secondsstring")));
        sleep(1000);
        b(translate(s + " 3" + get("secondsstring")));
        sleep(1000);
        b(translate(s + " 2" + get("secondsstring")));
        sleep(1000);
        b(translate(s + " 1" + get("secondstring")));
        sleep(1000);
        b(translate(get("finalstring")));
        Bukkit.shutdown();
    }

    public String translate(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public String get(String s) {
        return config.getString(s);
    }

    public void b(String s) {
        Bukkit.broadcastMessage(s);
    }
}