/*package com.unclecole.mfvoucher.commands;

import com.unclecole.mfvoucher.MFVoucher;
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

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class ClaimVoucherCmd implements CommandExecutor {

    private MFVoucher plugin;
    private Economy economy;

    public ClaimVoucherCmd() {
        plugin = MFVoucher.getInstance();
        economy = MFVoucher.getEcon();
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) {

        Player player = (Player) s;

        if(args.length < 1) {
            TL.INVALID_COMMAND_USAGE.send(player, new PlaceHolder("<command>", "/claimwithdraw <amount>"));
            return false;
        }

        PlayerData data = MFVoucher.getInstance().getApi().dataStore.getPlayerData(player.getUniqueId());


        if(!isInteger(args[0])) {
            TL.INVALID_COMMAND_USAGE.send(player, new PlaceHolder("<command>", "/claimwithdraw <amount>"));
            return false;
        }
        int amount = Integer.parseInt(args[0]);

        if(amount > data.getRemainingClaimBlocks()) {
            TL.NOT_ENOUGH_CLAIMS.send(player, new PlaceHolder("%total%", data.getRemainingClaimBlocks() - GriefPrevention.instance.dataStore.getGroupBonusBlocks(player.getUniqueId())));
            return false;
        }

        if(player.getInventory().firstEmpty() == -1) {
            TL.INVENTORY_FULL.send(player);
            return false;

        }

        note.setName(C.color(ColeClaimWithdraw.getInstance().getNoteName().replaceAll("%amount%", String.valueOf(amount))));

        ItemMeta noteMeta = note.getItemMeta();

        noteMeta.setLore(C.color(ColeClaimWithdraw.getInstance().getNoteLore(), new PlaceHolder("%amount%", amount), new PlaceHolder("%player%", player.getName())));

        noteMeta.getPersistentDataContainer().set(ColeClaimWithdraw.getInstance().getNoteKey(), PersistentDataType.INTEGER, amount);
        TL.SUCCESSFULLY_WITHDRAWED_CLAIMS.send(player, new PlaceHolder("%amount%", amount));

        if(data.getBonusClaimBlocks() < amount) {
            amount = amount - data.getBonusClaimBlocks();
            data.setBonusClaimBlocks(0);
            data.setAccruedClaimBlocks(data.getAccruedClaimBlocks() - amount);
        } else {
            data.setBonusClaimBlocks(data.getBonusClaimBlocks() - amount);
        }

        note.setItemMeta(noteMeta);
        player.getInventory().addItem(note);
        return false;
    }

    public void addBounty(Player player, Player bountyPlayer, long amount) {
        if(economy.getBalance(player) < amount) {
            TL.INSUFFICIENT_FUNDS.send(player);
            return;
        }

        BountyObject bountyObject = new BountyObject(player.getUniqueId(), bountyPlayer.getUniqueId(), amount, System.currentTimeMillis()/1000);

        if(BountyData.bountyData.containsKey(bountyPlayer.getUniqueId())) {
            if(BountyData.bountyData.get(bountyPlayer.getUniqueId()).getBounty() >= amount) {
                TL.ALREADY_HAS_BOUNTY.send(player,
                        new PlaceHolder("%player%", bountyPlayer.getName()),
                        new PlaceHolder("%amount%", C.getFormattedMoney(amount)));
                return;
            } else  {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(BountyData.bountyData.get(bountyPlayer.getUniqueId()).getOwnerUUID());
                MFBounty.getEcon().depositPlayer(offlinePlayer, BountyData.bountyData.get(bountyPlayer.getUniqueId()).getBounty());
                BountyData.bountyData.put(bountyPlayer.getUniqueId(), bountyObject);
            }
        } else BountyData.bountyData.put(bountyPlayer.getUniqueId(), bountyObject);

        economy.withdrawPlayer(player,amount);
        String amt = C.getFormattedMoney(amount);
        Bukkit.getOnlinePlayers().forEach(online -> {
            TL.BOUNTY_PLACED.send(online, new PlaceHolder("%player%", player.getName()), new PlaceHolder("%bounty%", bountyPlayer.getName()), new PlaceHolder("%amount%", amt));
        });
    }

    public void BountyGUI(Player player, int page) {
        InventoryGUI gui = new InventoryGUI(54, C.color("&fBounties " + page + "/" + "100"));

        ItemBuilder nextPage = new ItemBuilder(Material.ARROW);
        nextPage.setName(C.color("&a&lNEXT"));
        nextPage.addLore(C.color("&fClick to go forward a page"));

        ItemBuilder info = new ItemBuilder(Material.SPECTRAL_ARROW);
        info.setName(C.color("&c&lBOUNTIES"));
        info.addLore(C.color("&fType &c/bounty add (player) (amount)"));
        info.addLore(C.color("&fto put a bounty on someone."));
        info.addLore(C.color("&a"));
        info.addLore(C.color("&fIf you kill someone with"));
        info.addLore(C.color("&fa bounty, you get paid!"));

        ItemBuilder backPage = new ItemBuilder(Material.ARROW);
        backPage.setName(C.color("&c&lBACK"));
        backPage.addLore(C.color("&fClick to go back a page"));

        gui.addButton(49, new ItemButton(info) {
            @Override
            public void onClick(InventoryClickEvent e) {

            }
        });

        gui.addButton(50, new ItemButton(nextPage) {
            @Override
            public void onClick(InventoryClickEvent e) {
                if(BountyData.bountyData.size() < page*45) return;
                BountyGUI(player, page + 1);
            }
        });
        gui.addButton(48, new ItemButton(backPage) {
            @Override
            public void onClick(InventoryClickEvent e) {
                if(page-1 < 1) return;
                BountyGUI(player, page - 1);
            }
        });

        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                int amt = (45*(page-1))+ (BountyData.bountyData.size() - ((page-1) * 45));
                if((BountyData.bountyData.size() - ((page-1) * 45)) > 45 ) amt = 45*page;
                int i = 0;

                for(Map.Entry<UUID, BountyObject> set : BountyData.bountyData.entrySet()) {
                    if(i >= amt) break;

                    UUID uuid = set.getKey();
                    BountyObject object = set.getValue();

                    ItemBuilder playerSkull = new ItemBuilder(Material.PLAYER_HEAD);
                    Player skullPlayer = Bukkit.getPlayer(uuid);
                    if(uuid.equals(object.getOwnerUUID())) playerSkull.setName(C.color("&6&l" + skullPlayer.getName().toUpperCase(Locale.ROOT)));
                    else playerSkull.setName(C.color("&f&l" + skullPlayer.getName().toUpperCase(Locale.ROOT)));
                    playerSkull.addLore(C.color("&fBounty: " + C.getFormattedMoney(object.getBounty())));
                    if(uuid.equals(object.getOwnerUUID())) {
                        playerSkull.addLore(C.color("&a"));
                        playerSkull.addLore(C.color("&c&lCLICK TOO REMOVE BOUNTY FROM PLAYER!"));
                    }
                    NBTItem nbtSkull = new NBTItem(playerSkull);

                    NBTCompound skull = nbtSkull.addCompound("SkullOwner");

                    skull.setString("Name", skullPlayer.getName());
                    skull.setString("Id", uuid.toString());

                    nbtSkull.applyNBT(playerSkull);
                    gui.addButton(i - ((page - 1) * 45), new ItemButton(playerSkull) {
                        @Override
                        public void onClick(InventoryClickEvent e) {
                            if(uuid.equals(object.getOwnerUUID())) {

                                BountyGUI(player, page);
                            }
                        }
                    });
                    i++;
                }
            }
        });
        gui.open(player);
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

    public boolean validPlayer(String string) {
        return (Bukkit.getPlayer(string) != null);
    }
}*/
