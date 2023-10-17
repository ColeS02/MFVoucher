package com.unclecole.mfvoucher.commands;

import com.unclecole.mfvoucher.MFVoucher;
import com.unclecole.mfvoucher.database.ClaimVoucherData;
import com.unclecole.mfvoucher.database.MoneyVoucherData;
import com.unclecole.mfvoucher.database.VoucherData;
import com.unclecole.mfvoucher.database.XpVoucherData;
import com.unclecole.mfvoucher.objects.Voucher;
import com.unclecole.mfvoucher.utils.*;
import de.tr7zw.nbtapi.NBTItem;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import redempt.redlib.itemutils.ItemBuilder;

import java.util.Locale;
import java.util.Random;

public class VoucherCmd implements CommandExecutor {

    private MFVoucher plugin;
    private Economy economy;
    private ConfigItems config;
    private ItemBuilder xpNote;

    public VoucherCmd() {
        plugin = MFVoucher.getInstance();
        economy = MFVoucher.getEcon();
        config = MFVoucher.getInstance().getConfigItems();
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) {

        if(args.length < 4) {
            TL.INVALID_COMMAND_USAGE.send(s, new PlaceHolder("<command>", "/voucher give <player> <type> <amount>"));
            return false;
        }
        if(!args[0].equalsIgnoreCase("give")) {
            TL.INVALID_COMMAND_USAGE.send(s, new PlaceHolder("<command>", "/voucher give <player> <type> <amount>"));
        }

        if(Bukkit.getPlayer(args[1]) == null) {
            TL.INVALID_COMMAND_USAGE.send(s, new PlaceHolder("<command>", "/voucher give <player> <type> <amount>"));
            return false;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if(!MFVoucher.getInstance().getConfigItems().getVouchers().containsKey(args[2].toLowerCase())) {
            TL.INVALID_COMMAND_USAGE.send(s, new PlaceHolder("<command>", "/voucher give <player> <type> <amount>"));
            return false;
        }

        String voucher = args[2].toLowerCase();

        if(!isInteger(args[3])) {
            TL.INVALID_COMMAND_USAGE.send(s, new PlaceHolder("<command>", "/voucher give <player> <type> <amount>"));
            return false;
        }

        int amount = Integer.parseInt(args[3]);

        Voucher voucherObject = MFVoucher.getInstance().getConfigItems().getVouchers().get(voucher);
        ItemBuilder voucherItem = voucherObject.getClaimNote();

        voucherItem.setName(C.color(voucherObject.getClaimNoteName()));

        ItemMeta noteMeta = voucherItem.getItemMeta();

        noteMeta.setLore(C.color(voucherObject.getClaimNoteLore()));

        voucherItem.setItemMeta(noteMeta);

        NBTItem nbtItem = new NBTItem(voucherItem);
        nbtItem.setString("voucher-type", voucher);

        for(int i = 0; i < amount; i++) {

            String ID;

            do {
                ID = randomString();
            } while(VoucherData.ID.containsKey(ID));

            VoucherData.ID.put(ID, false);

            nbtItem.setString("voucher-id", ID);
            nbtItem.applyNBT(voucherItem);

            if(target.getInventory().firstEmpty() == -1) {
                target.getWorld().dropItem(target.getLocation(), voucherItem);
            } else target.getInventory().addItem(voucherItem);
        }

        TL.RECEIVED_VOUCHER.send(target,
                new PlaceHolder("%amount%", amount),
                new PlaceHolder("%type%", voucher));

        if(s instanceof Player && !s.equals(target)) {
            TL.GAVE_VOUCHER.send(s,
                    new PlaceHolder("%amount%", amount),
                    new PlaceHolder("%type%", voucher),
                    new PlaceHolder("%player%", target.getName()));
        }

        return false;
    }

    public String randomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    public ItemStack removeMeta(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(C.color("&a"));
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_DYE, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_UNBREAKABLE);

        itemStack.setItemMeta(meta);
        return itemStack;
    }



    public boolean isParsable(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    public boolean isInteger(String string) {
        try {
            Long.valueOf(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean validPlayer(String string) {
        return (Bukkit.getPlayer(string) != null);
    }
}
