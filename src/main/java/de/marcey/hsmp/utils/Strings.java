package de.marcey.hsmp.utils;

import de.marcey.hsmp.apis.colorapi.ColorGradient;
import net.md_5.bungee.api.ChatColor;

public class Strings {

    public static String prefix = new ColorGradient("HardcoreSMP").withFrom(ChatColor.of("#ff4d4d")).withTo(ChatColor.of("#eb9b19")).withBold(true).execute() + " §r§8>>";
    public static String smpname = new ColorGradient("Kenjih x Tjan HSMP").withFrom(ChatColor.of("#e43a3a")).withTo(ChatColor.of("#ffe200")).withBold(true).execute();

    public static String openendtitle = new ColorGradient("Das Ende").withFrom(ChatColor.of("#e43a3a")).withTo(ChatColor.of("#ffa600")).withBold(true).execute();
    public static String openendsubtitle = new ColorGradient("wurde geöffnet").withFrom(ChatColor.of("#474c54")).withTo(ChatColor.of("#8f9899")).withBold(true).execute();

}
