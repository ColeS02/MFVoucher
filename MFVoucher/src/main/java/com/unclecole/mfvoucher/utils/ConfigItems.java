package com.unclecole.mfvoucher.utils;

import com.unclecole.mfvoucher.MFVoucher;
import com.unclecole.mfvoucher.objects.Voucher;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import redempt.redlib.itemutils.ItemBuilder;

import java.util.HashMap;
import java.util.List;

public class ConfigItems {

    @Getter public static ConfigItems configItems;

    public ConfigItems() {
        loadClaimNote();
        loadMoneyNote();
        loadXPNote();
        loadVouchers();
    }

    @Getter private ItemBuilder claimNote;
    @Getter private String claimNoteName;
    @Getter private List<String> claimNoteLore;

    public void loadClaimNote() {
        claimNote = new ItemBuilder(Material.getMaterial(MFVoucher.getInstance().getConfig().getString("ClaimWithdraw.Material")), 1);
        claimNote.addItemFlags(ItemFlag.HIDE_DYE,ItemFlag.HIDE_PLACED_ON,ItemFlag.HIDE_UNBREAKABLE,ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_POTION_EFFECTS,ItemFlag.HIDE_ATTRIBUTES);
        if(MFVoucher.getInstance().getConfig().getInt("ClaimWithdraw.CustomModelData") < 0) {
            claimNote.setCustomModelData(MFVoucher.getInstance().getConfig().getInt("ClaimWithdraw.CustomModelData"));
        }
        if(MFVoucher.getInstance().getConfig().getInt("ClaimWithdraw.Data") < 0) {
            claimNote.setDurability(MFVoucher.getInstance().getConfig().getInt("ClaimWithdraw.Data"));
        }
        if(MFVoucher.getInstance().getConfig().getBoolean("ClaimWithdraw.Enchanted")) {
            claimNote.addEnchant(Enchantment.DIG_SPEED,1);
        }
        claimNoteName = MFVoucher.getInstance().getConfig().getString("ClaimWithdraw.Name");
        claimNoteLore = MFVoucher.getInstance().getConfig().getStringList("ClaimWithdraw.Lore");
    }

    @Getter private ItemBuilder moneyNote;
    @Getter private String moneyNoteName;
    @Getter private List<String> moneyNoteLore;

    public void loadMoneyNote() {
        moneyNote = new ItemBuilder(Material.getMaterial(MFVoucher.getInstance().getConfig().getString("MoneyWithdraw.Material")), 1);
        moneyNote.addItemFlags(ItemFlag.HIDE_DYE,ItemFlag.HIDE_PLACED_ON,ItemFlag.HIDE_UNBREAKABLE,ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_POTION_EFFECTS,ItemFlag.HIDE_ATTRIBUTES);
        if(MFVoucher.getInstance().getConfig().getInt("MoneyWithdraw.CustomModelData") < 0) {
            moneyNote.setCustomModelData(MFVoucher.getInstance().getConfig().getInt("MoneyWithdraw.CustomModelData"));
        }
        if(MFVoucher.getInstance().getConfig().getInt("MoneyWithdraw.Data") < 0) {
            moneyNote.setDurability(MFVoucher.getInstance().getConfig().getInt("MoneyWithdraw.Data"));
        }
        if(MFVoucher.getInstance().getConfig().getBoolean("MoneyWithdraw.Enchanted")) {
            moneyNote.addEnchant(Enchantment.DIG_SPEED,1);
        }
        moneyNoteName = MFVoucher.getInstance().getConfig().getString("MoneyWithdraw.Name");
        moneyNoteLore = MFVoucher.getInstance().getConfig().getStringList("MoneyWithdraw.Lore");
    }

    @Getter private ItemBuilder XPNote;
    @Getter private String XPNoteName;
    @Getter private List<String> XPNoteLore;

    public void loadXPNote() {
        XPNote = new ItemBuilder(Material.getMaterial(MFVoucher.getInstance().getConfig().getString("XPWithdraw.Material")), 1);
        XPNote.addItemFlags(ItemFlag.HIDE_DYE,ItemFlag.HIDE_PLACED_ON,ItemFlag.HIDE_UNBREAKABLE,ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_POTION_EFFECTS,ItemFlag.HIDE_ATTRIBUTES);
        if(MFVoucher.getInstance().getConfig().getInt("XPWithdraw.CustomModelData") < 0) {
            XPNote.setCustomModelData(MFVoucher.getInstance().getConfig().getInt("XPWithdraw.CustomModelData"));
        }
        if(MFVoucher.getInstance().getConfig().getInt("XPWithdraw.Data") < 0) {
            XPNote.setDurability(MFVoucher.getInstance().getConfig().getInt("XPWithdraw.Data"));
        }
        if(MFVoucher.getInstance().getConfig().getBoolean("XPWithdraw.Enchanted")) {
            XPNote.addEnchant(Enchantment.DIG_SPEED,1);
        }
        XPNoteName = MFVoucher.getInstance().getConfig().getString("XPWithdraw.Name");
        XPNoteLore = MFVoucher.getInstance().getConfig().getStringList("XPWithdraw.Lore");
    }

    @Getter private HashMap<String, Voucher> vouchers = new HashMap<>();

    public void loadVouchers() {
        for(String key : MFVoucher.getInstance().getConfig().getConfigurationSection("Vouchers").getKeys(false)) {


            ItemBuilder item = new ItemBuilder(Material.getMaterial(MFVoucher.getInstance().getConfig().getString("Vouchers." + key +".Material")), 1);
            item.addItemFlags(ItemFlag.HIDE_DYE,ItemFlag.HIDE_PLACED_ON,ItemFlag.HIDE_UNBREAKABLE,ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_POTION_EFFECTS,ItemFlag.HIDE_ATTRIBUTES);
            if(MFVoucher.getInstance().getConfig().getInt("Vouchers." + key +".CustomModelData") < 0) {
                item.setCustomModelData(MFVoucher.getInstance().getConfig().getInt("Vouchers." + key + ".CustomModelData"));
            }
            if(MFVoucher.getInstance().getConfig().getInt("Vouchers." + key +".Data") < 0) {
                item.setDurability(MFVoucher.getInstance().getConfig().getInt("Vouchers." + key +".Data"));
            }
            if(MFVoucher.getInstance().getConfig().getBoolean("Vouchers." + key +".Enchanted")) {
                item.addEnchant(Enchantment.DIG_SPEED,1);
            }

            vouchers.put(key, new Voucher(item,
                    MFVoucher.getInstance().getConfig().getString("Vouchers." + key +".Name"),
                    MFVoucher.getInstance().getConfig().getStringList("Vouchers." + key +".Lore"),
                    MFVoucher.getInstance().getConfig().getStringList("Vouchers." + key +".commands")));
        }
    }
}
