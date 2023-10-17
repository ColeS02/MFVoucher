package com.unclecole.mfvoucher.commands;

import com.unclecole.mfvoucher.MFVoucher;
import com.unclecole.mfvoucher.database.MoneyVoucherData;
import com.unclecole.mfvoucher.database.XpVoucherData;
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

import java.util.Random;

public class XpVoucherCmd implements CommandExecutor {

    private MFVoucher plugin;
    private Economy economy;
    private ConfigItems config;
    private ItemBuilder xpNote;

    public XpVoucherCmd() {
        plugin = MFVoucher.getInstance();
        economy = MFVoucher.getEcon();
        config = MFVoucher.getInstance().getConfigItems();
        xpNote = config.getXPNote();
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) {

        Player player = (Player) s;

        if(args.length < 1) {
            TL.INVALID_COMMAND_USAGE.send(player, new PlaceHolder("<command>", "/xpwithdraw <amount>"));
            return false;
        }

        String num;
        long amount;

        if(args[0].toLowerCase().contains("l")) {
            num = args[0].substring(0, args[0].toLowerCase().indexOf('l'));
            amount = Experience.getExpFromLevel(Integer.parseInt(num));
        } else {
            amount = Long.parseLong(args[0]);
        }

        player.getExp();



        if(amount >= player.getExp()) {
            TL.NOT_ENOUGH_EXP.send(player, new PlaceHolder("%total%", player.getExp()));
            return false;
        }

        if(player.getInventory().firstEmpty() == -1) {
            TL.INVENTORY_FULL.send(player);
            return false;

        }

        xpNote.setName(C.color(config.getXPNoteName().replaceAll("%amount%", String.valueOf(amount))));

        NBTItem nbtItem = new NBTItem(xpNote);
        nbtItem.setLong("money-note", amount);

        String ID;

        do {
            ID = randomString();
        } while(XpVoucherData.ID.containsKey(ID));

        XpVoucherData.ID.put(ID, false);

        nbtItem.setString("money-id", ID);
        nbtItem.applyNBT(xpNote);


        ItemMeta noteMeta = xpNote.getItemMeta();

        noteMeta.setLore(C.color(config.getXPNoteLore(), new PlaceHolder("%amount%", amount), new PlaceHolder("%player%", player.getName())));

        TL.SUCCESSFULLY_WITHDRAWED_MONEY.send(player, new PlaceHolder("%amount%", amount));

        economy.withdrawPlayer(player, amount);

        xpNote.setItemMeta(noteMeta);

        player.getInventory().addItem(xpNote);
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
