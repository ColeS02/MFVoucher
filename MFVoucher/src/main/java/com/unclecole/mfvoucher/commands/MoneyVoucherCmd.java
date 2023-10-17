package com.unclecole.mfvoucher.commands;

import com.unclecole.mfvoucher.MFVoucher;
import com.unclecole.mfvoucher.database.MoneyVoucherData;
import com.unclecole.mfvoucher.utils.C;
import com.unclecole.mfvoucher.utils.ConfigItems;
import com.unclecole.mfvoucher.utils.PlaceHolder;
import com.unclecole.mfvoucher.utils.TL;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import me.ryanhamshire.GriefPrevention.PlayerData;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import redempt.redlib.itemutils.ItemBuilder;

import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class MoneyVoucherCmd implements CommandExecutor {

    private MFVoucher plugin;
    private Economy economy;
    private ConfigItems config;
    private ItemBuilder moneyNote;

    public MoneyVoucherCmd() {
        plugin = MFVoucher.getInstance();
        economy = MFVoucher.getEcon();
        config = MFVoucher.getInstance().getConfigItems();
        moneyNote = config.getMoneyNote();
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) {

        Player player = (Player) s;

        if(args.length < 1) {
            TL.INVALID_COMMAND_USAGE.send(player, new PlaceHolder("<command>", "/withdraw <amount>"));
            return false;
        }

        if(!isInteger(args[0])) {
            TL.INVALID_COMMAND_USAGE.send(player, new PlaceHolder("<command>", "/withdraw <amount>"));
            return false;
        }
        long amount = Long.parseLong(args[0]);

        if(amount >= economy.getBalance(player)) {
            TL.NOT_ENOUGH_MONEY.send(player, new PlaceHolder("%total%", economy.getBalance(player)));
            return false;
        }

        if(player.getInventory().firstEmpty() == -1) {
            TL.INVENTORY_FULL.send(player);
            return false;

        }

        moneyNote.setName(C.color(config.getMoneyNoteName().replaceAll("%amount%", String.valueOf(amount))));

        NBTItem nbtItem = new NBTItem(moneyNote);
        nbtItem.setLong("money-note", amount);

        String ID;

        do {
            ID = randomString();
        } while(MoneyVoucherData.ID.containsKey(ID));

        MoneyVoucherData.ID.put(ID, false);

        nbtItem.setString("money-id", ID);
        nbtItem.applyNBT(moneyNote);


        ItemMeta noteMeta = moneyNote.getItemMeta();

        noteMeta.setLore(C.color(config.getMoneyNoteLore(), new PlaceHolder("%amount%", amount), new PlaceHolder("%player%", player.getName())));

        TL.SUCCESSFULLY_WITHDRAWED_MONEY.send(player, new PlaceHolder("%amount%", amount));

        economy.withdrawPlayer(player, amount);

        moneyNote.setItemMeta(noteMeta);

        player.getInventory().addItem(moneyNote);
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
