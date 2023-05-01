package de.marcey.hsmp.apis.customheads;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

public class CustomHeads {

    public static ItemStack getCustomHeadFromValue(String value) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        UUID hashAsId = new UUID(value.hashCode(), value.hashCode());
        headMeta.setOwningPlayer(Bukkit.getOfflinePlayer(hashAsId));

        String base64Encoded = Base64.getEncoder().encodeToString(("" + '{' + "textures" + ':' + '{' + "SKIN" + ':' + '{' + "url" + ':' + '"' + "http://textures.minecraft.net/texture/" + value + '"' + '}' + '}' + '}').getBytes());
        headMeta.setOwnerProfile(Bukkit.createPlayerProfile(hashAsId, base64Encoded));

        head.setItemMeta(headMeta);
        return head;
    }

}
