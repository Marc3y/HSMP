package de.marcey.hsmp.listener;

import de.marcey.hsmp.objects.DiscordWebhook;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogListener implements Listener {

    private static List<String> urls = new ArrayList<>();

    public static void init(){
        urls.add("https://discord.com/api/webhooks/1085658511540756583/HDL9zn4jSUK02-BjltNrM7uDPrsHRk8M7O4ZVixDKXD6RdZWEdOsbQFem045TZo6RSqX");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        sendWebhook(Color.GREEN, e.getPlayer().getName() + " hat das HSMP betreten", "https://minotar.net/avatar/" + e.getPlayer().getName());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        sendWebhook(Color.RED, e.getPlayer().getName() + " hat das HSMP verlassen", "https://minotar.net/avatar/" + e.getPlayer().getName());
    }

    public static void sendWebhook(Color color, String message, String icon){
        DiscordWebhook hook = new DiscordWebhook(urls.get(0));
        hook.addEmbed(new DiscordWebhook.EmbedObject()
                .setColor(color)
                .setAuthor(message, "", icon));
        for(String url : urls){
            hook.setUrl(url);
            try {
                hook.execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
