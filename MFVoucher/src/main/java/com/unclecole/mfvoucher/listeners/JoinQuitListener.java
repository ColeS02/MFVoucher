package com.unclecole.mfvoucher.listeners;

import com.unclecole.mfvoucher.MFVoucher;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("voucher.notify")) MFVoucher.getInstance().getStaffNotification().add(player.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("voucher.notify")) MFVoucher.getInstance().getStaffNotification().remove(player.getUniqueId());
    }
}
