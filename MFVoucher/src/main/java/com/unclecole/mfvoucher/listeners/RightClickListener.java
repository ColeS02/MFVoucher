package com.unclecole.mfvoucher.listeners;

import com.unclecole.mfvoucher.MFVoucher;
import com.unclecole.mfvoucher.database.MoneyVoucherData;
import com.unclecole.mfvoucher.database.VoucherData;
import com.unclecole.mfvoucher.objects.Voucher;
import com.unclecole.mfvoucher.utils.C;
import com.unclecole.mfvoucher.utils.ConfigItems;
import com.unclecole.mfvoucher.utils.PlaceHolder;
import com.unclecole.mfvoucher.utils.TL;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class RightClickListener implements Listener {

    private ConfigItems config;

    public RightClickListener() {
        config = MFVoucher.getInstance().getConfigItems();;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {

        Material material = event.getPlayer().getInventory().getItemInMainHand().getType();

        if(!material.equals(config.getMoneyNote().getType()) || !material.equals(config.getClaimNote().getType())) return;
        if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

            NBTItem nbtItem = new NBTItem(item);
            Player player = event.getPlayer();

            if(nbtItem.hasTag("money-note")) {
                if(nbtItem.hasTag("money-id") && MoneyVoucherData.ID.containsKey(nbtItem.getString("money-id")) && !MoneyVoucherData.ID.get(nbtItem.getString("money-id"))) {
                    long amount = nbtItem.getLong("money-note");
                    MFVoucher.getEcon().depositPlayer(event.getPlayer(), amount);
                    item.setAmount(item.getAmount() - 1);
                    TL.SUCCESSFULLY_DEPOSITED_MONEY.send(event.getPlayer(), new PlaceHolder("%amount%", amount));
                    MoneyVoucherData.ID.put(nbtItem.getString("money-id"), true);
                } else {
                    TL.VOUCHER_CLAIMED.send(player);
                    MFVoucher.getInstance().getStaffNotification().forEach(uuid -> {
                        Player staff = Bukkit.getPlayer(uuid);

                        Location location = player.getLocation();

                        staff.sendMessage(C.color("&8&m-----------"));
                        staff.sendMessage(C.color("&c&oPossible Duper"));
                        staff.sendMessage(C.color("&aPlayer: &f" + player.getName()));
                        staff.sendMessage(C.color("&aLocation: &f" + location.getX() + "X " + location.getY() + "Y " + location.getZ() + "Z"));
                        staff.sendMessage(C.color("&aWorld: &f" + location.getWorld().getName()));
                        staff.sendMessage(C.color("&aType: &fMoney Voucher"));
                        staff.sendMessage(C.color("&8&m-----------"));
                    });
                }
            }
            if(nbtItem.hasTag("xp-note")) {
                if(nbtItem.hasTag("xp-id") && MoneyVoucherData.ID.containsKey(nbtItem.getString("xp-id")) && !MoneyVoucherData.ID.get(nbtItem.getString("xp-id"))) {
                    long amount = nbtItem.getLong("xp-note");
                    MFVoucher.getEcon().depositPlayer(event.getPlayer(), amount);
                    item.setAmount(item.getAmount() - 1);
                    TL.SUCCESSFULLY_DEPOSITED_MONEY.send(event.getPlayer(), new PlaceHolder("%amount%", amount));
                    MoneyVoucherData.ID.put(nbtItem.getString("xp-id"), true);
                } else {
                    TL.VOUCHER_CLAIMED.send(player);
                    MFVoucher.getInstance().getStaffNotification().forEach(uuid -> {
                        Player staff = Bukkit.getPlayer(uuid);

                        Location location = player.getLocation();

                        staff.sendMessage(C.color("&8&m-----------"));
                        staff.sendMessage(C.color("&c&oPossible Duper"));
                        staff.sendMessage(C.color("&aPlayer: &f" + player.getName()));
                        staff.sendMessage(C.color("&aLocation: &f" + location.getX() + "X " + location.getY() + "Y " + location.getZ() + "Z"));
                        staff.sendMessage(C.color("&aWorld: &f" + location.getWorld().getName()));
                        staff.sendMessage(C.color("&aType: &fXP Voucher"));
                        staff.sendMessage(C.color("&8&m-----------"));
                    });
                }
            }

            if(nbtItem.hasTag("voucher-type")) {
                if(nbtItem.hasTag("voucher-id") && VoucherData.ID.containsKey(nbtItem.getString("voucher-id")) && !VoucherData.ID.get(nbtItem.getString("voucher-id"))) {
                    Voucher voucher = MFVoucher.getInstance().getConfigItems().getVouchers().get(nbtItem.getString("voucher-type"));

                    voucher.getCommands().forEach(command -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                    });
                    item.setAmount(item.getAmount() - 1);
                    VoucherData.ID.put(nbtItem.getString("voucher-id"), true);
                } else {
                    TL.VOUCHER_CLAIMED.send(player);
                    MFVoucher.getInstance().getStaffNotification().forEach(uuid -> {
                        Player staff = Bukkit.getPlayer(uuid);

                        Location location = player.getLocation();

                        staff.sendMessage(C.color("&8&m-----------"));
                        staff.sendMessage(C.color("&c&oPossible Duper"));
                        staff.sendMessage(C.color("&aPlayer: &f" + player.getName()));
                        staff.sendMessage(C.color("&aLocation: &f" + location.getX() + "X " + location.getY() + "Y " + location.getZ() + "Z"));
                        staff.sendMessage(C.color("&aWorld: &f" + location.getWorld().getName()));
                        staff.sendMessage(C.color("&aType: &fXP Voucher"));
                        staff.sendMessage(C.color("&8&m-----------"));
                    });
                }
            }
        }
    }
}
