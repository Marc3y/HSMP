package de.marcey.hsmp.apis.colorapi;

import de.marcey.hsmp.HSMP;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ColorAnimation {
    private String message;
    private final int delay;
    private List<ChatColor> colors;
    private int currentIndex = 0;
    private boolean bold;

    private String animatedText;

    public ColorAnimation(String message, int delay, boolean bold, List<ChatColor> colors) {
        this.message = message;
        this.delay = delay;
        this.bold = bold;
        this.colors = colors;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public List<ChatColor> getColors() {
        return colors;
    }

    public void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < message.length(); i++) {
                    int colorIndex = (i - currentIndex) % colors.size();
                    if (colorIndex < 0) {
                        colorIndex += colors.size();
                    }
                    ChatColor currentColor = colors.get(colorIndex);

                    if(bold){
                        sb.append(currentColor).append(ChatColor.BOLD).append(message.charAt(i));
                    } else sb.append(currentColor).append(message.charAt(i));
                }
                animatedText = sb.toString();
                currentIndex++;
                if (currentIndex == colors.size()) {
                    currentIndex = 0;
                }
            }
        }.runTaskTimer(HSMP.getInstance(), 0, delay);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNextAnimatedText() {
        return animatedText;
    }
}

// player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(sb.toString()));
