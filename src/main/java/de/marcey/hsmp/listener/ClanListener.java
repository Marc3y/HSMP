package de.marcey.hsmp.listener;

import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.clansystem.ChunkProvider;
import de.marcey.hsmp.clansystem.Clan;
import de.marcey.hsmp.clansystem.ClanProvider;
import de.marcey.hsmp.clansystem.gui.SettingsGUI;
import de.marcey.hsmp.objects.Rang;
import de.marcey.hsmp.scoreboard.ScoreboardManager;
import de.marcey.hsmp.utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClanListener implements Listener {

    private static List<UUID> messageCooldown = new ArrayList<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        reloadPlayer(p);
        Rang rang = Rang.getRang(p);
        Clan clan = ClanProvider.getClan(p);
        e.setJoinMessage("§a» §r" + (clan != null ? "§f[§r" + clan.getDisplayname().replace('&', '§') + "§r§f] §r" : "") + rang.getFormattedDisplayname() + rang.getColor() + p.getName() + " §r§7hat das HSMP betreten");
        RangListener.setTablist();
        ScoreboardManager.setScoreboard(p);
    }

    public static void reloadPlayer(Player p){
        if(ClanProvider.getClan(p) == null){
            for(Clan c : HSMP.getClans().getAllClans()){
                if(c.getMitglieder().contains(p.getUniqueId())){
                    if(!ClanProvider.cs.containsKey(c.getUniqueId())){
                        ClanProvider.cs.put(c.getUniqueId(), c);
                    }
                    ClanProvider.ids.put(p.getUniqueId(), c.getUniqueId());
                }
            }
        }
    }

    public static void reloadPlayer(UUID uuid){
        if(ClanProvider.getClan(uuid) == null){
            for(Clan c : HSMP.getClans().getAllClans()){
                if(c.getMitglieder().contains(uuid)){
                    if(!ClanProvider.cs.containsKey(c.getUniqueId())){
                        ClanProvider.cs.put(c.getUniqueId(), c);
                    }
                    ClanProvider.ids.put(uuid, c.getUniqueId());
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        Location to = e.getTo();
        Location from = e.getFrom();
        Clan clan = ClanProvider.getClan(p);
        if(ChunkProvider.isInGegnerChunk(clan, to)){
            e.setCancelled(true);
            if(!messageCooldown.contains(p.getUniqueId())){
                messageCooldown.add(p.getUniqueId());
                p.sendMessage(Strings.prefix + " §cDieser Chunk ist geschützt. Hier darfst du nicht reinlaufen.");
                ChunkListener.spawnParticlesWholeChunk(p, e.getTo(), Color.RED, 3);
                Bukkit.getScheduler().runTaskLater(HSMP.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        messageCooldown.remove(p.getUniqueId());
                    }
                }, 40);
            }
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e){
        if(!(e.getWhoClicked() instanceof Player)){
            return;
        }
        Player p = (Player) e.getWhoClicked();
        if(e.getCurrentItem() == null){
            return;
        }
        if(!e.getView().getTitle().startsWith("§8Settings")){
            return;
        }
        Clan clan = ClanProvider.getClan(p);
        if(clan == null){
            e.setCancelled(true);
            return;
        }
        if(!clan.getOps().contains(p.getUniqueId())){
            e.setCancelled(true);
            return;
        }
        if(e.getCurrentItem().getType().equals(Material.LIME_CONCRETE)){
            Material m = e.getClickedInventory().getItem(e.getSlot()-9).getType();
            ItemStack item = e.getClickedInventory().getItem(e.getSlot()-9);
            boolean is = false;
            if(m.equals(Material.WRITABLE_BOOK)){
                clan.setClanChatEnabled(is);
                ClanProvider.updateClan(clan);
                SettingsGUI.openGUI(p);
            } else if(m.equals(Material.PAPER)){
                clan.setJoinRequestsEnabled(is);
                ClanProvider.updateClan(clan);
                SettingsGUI.openGUI(p);
            } else if(m.equals(Material.DIAMOND_SWORD)){
                clan.setStatsForClanMembersEnabled(is);
                ClanProvider.updateClan(clan);
                SettingsGUI.openGUI(p);
            } else if(m.equals(Material.GRASS_BLOCK)){
                clan.setStatsPublic(is);
                ClanProvider.updateClan(clan);
                SettingsGUI.openGUI(p);
            } else if(m.equals(Material.NAME_TAG)){
                clan.setNormalMembersAllowedToInvitePlayers(is);
                ClanProvider.updateClan(clan);
                SettingsGUI.openGUI(p);
            }
            sendClanMessage(clan, Strings.prefix + " §7Der Spieler " + Rang.getRang(p).getFormattedDisplayname() + p.getName() + " §7hat die Einstellung " + item.getItemMeta().getDisplayName() + " §cdeaktiviert§7.");
        } else if(e.getCurrentItem().getType().equals(Material.RED_CONCRETE)){
            Material m = e.getClickedInventory().getItem(e.getSlot()-9).getType();
            ItemStack item = e.getClickedInventory().getItem(e.getSlot()-9);
            boolean is = true;
            if(m.equals(Material.WRITABLE_BOOK)){
                clan.setClanChatEnabled(is);
                ClanProvider.updateClan(clan);
                SettingsGUI.openGUI(p);
            } else if(m.equals(Material.PAPER)){
                clan.setJoinRequestsEnabled(is);
                ClanProvider.updateClan(clan);
                SettingsGUI.openGUI(p);
            } else if(m.equals(Material.DIAMOND_SWORD)){
                clan.setStatsForClanMembersEnabled(is);
                ClanProvider.updateClan(clan);
                SettingsGUI.openGUI(p);
            } else if(m.equals(Material.GRASS_BLOCK)){
                clan.setStatsPublic(is);
                ClanProvider.updateClan(clan);
                SettingsGUI.openGUI(p);
            } else if(m.equals(Material.NAME_TAG)){
                clan.setNormalMembersAllowedToInvitePlayers(is);
                ClanProvider.updateClan(clan);
                SettingsGUI.openGUI(p);
            }
            sendClanMessage(clan, Strings.prefix + " §7Der Spieler " + Rang.getRang(p).getFormattedDisplayname() + p.getName() + " §7hat die Einstellung " + item.getItemMeta().getDisplayName() + " §aaktiviert§7.");
        }
        e.setCancelled(true);
    }

    public static void sendClanMessage(Clan clan, String message){
        if(clan == null) return;
        for(Player current : Bukkit.getOnlinePlayers()){
            if(clan.getMitglieder().contains(current.getUniqueId())){
                current.sendMessage(message);
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        Location to = e.getBlock().getLocation();
        Clan clan = ClanProvider.getClan(p);
        if(ChunkProvider.isInGegnerChunk(clan, to)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        Location to = e.getBlock().getLocation();
        Clan clan = ClanProvider.getClan(p);
        if(ChunkProvider.isInGegnerChunk(clan, to)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(e.getClickedBlock() == null){
            return;
        }
        Location to = e.getClickedBlock().getLocation();
        Clan clan = ClanProvider.getClan(p);
        if(ChunkProvider.isInGegnerChunk(clan, to)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent e){
        Player p = e.getPlayer();
        Location to = e.getTo();
        Location from = e.getFrom();
        Clan clan = ClanProvider.getClan(p);
        if(ChunkProvider.isInGegnerChunk(clan, to)){
            e.setCancelled(true);
            p.sendMessage(Strings.prefix + " §cDieses Portal ist geschützt. Hier darfst du nicht durch.");
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e){
        Player p = e.getPlayer();
        Clan clan = ClanProvider.getClan(p);
        if(ChunkProvider.isInGegnerChunk(clan, e.getTo())){
            e.setCancelled(true);
            p.sendMessage(Strings.prefix + " §cDer Teleport wurde abgebrochen, da du probiert hast, dich in ein Gegner-Chunk zu teleportieren.");
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e){
        if(e.getItem() == null){
            return;
        }
        Player p = e.getPlayer();
        Location to = e.getItem().getLocation();
        Clan clan = ClanProvider.getClan(p);
        if(ChunkProvider.isInGegnerChunk(clan, to)){
            e.setCancelled(true);
        }
    }

}
