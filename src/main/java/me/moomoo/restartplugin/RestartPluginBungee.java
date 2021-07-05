package me.moomoo.restartplugin;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class RestartPluginBungee extends Plugin implements Listener {
    private Configuration config;
    private boolean serverIsRestarting = false;

    @Override
    public void onEnable() {
        Logger log = getLogger();

        log.info(ChatColor.AQUA + "Loading config");
        File file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info(ChatColor.AQUA + "Registering listener");
        getProxy().getPluginManager().registerListener(this, this);

        log.info(ChatColor.AQUA + "Scheduling restarts");
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(get("Timezone")));
        config.getStringList("RestartTimes").forEach(b -> {
            String[] numbers = b.split(":");
            int hour = Integer.parseInt(numbers[0]);
            int minute = Integer.parseInt(numbers[1]);
            int second = Integer.parseInt(numbers[2]);

            ZonedDateTime nextRun = now.withHour(hour).withMinute(minute).withSecond(second);
            if (now.compareTo(nextRun) > 0)
                nextRun = nextRun.plusDays(1);

            Duration duration = Duration.between(now, nextRun);
            long initialDelay = duration.getSeconds();

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
            }, initialDelay, TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);
        });

        log.info(ChatColor.AQUA + "Done! :D");
    }

    public void restart() throws InterruptedException {
        if (serverIsRestarting) {
            return;
        }

        serverIsRestarting = true;
        sleep(900000);
        getProxy().stop(translate(get("finalstring")));
    }

    public String translate(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public String get(String s) {
        return config.getString(s);
    }

    public void b(String s) {
        getProxy().getPlayers().forEach(p -> p.sendMessage(TextComponent.fromLegacyText(s)));
    }
}
