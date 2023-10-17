package com.unclecole.mfvoucher.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.logging.Level;

public enum TL {
	NO_PERMISSION("messages.no-permission", "&c&lERROR! &fYou don't have the permission to do that."),
	INVALID_COMMAND_USAGE("messages.invalid-command-usage", "&cIncorrect Usage: &7<command>"),
	PLAYER_ONLY("messages.player-only", "&cThis command is for players only!"),
	NOT_ENOUGH_CLAIMS("messages.not-enough-claims", "&c&lERROR: &fYou don't have enough claims to withdraw! (%total%)"),
	SUCCESSFULLY_DEPOSITED_CLAIMS("messages.successfully-deposited-claims", "&a&lSUCCESS! &fYou successfully deposited %amount% claims!"),
	SUCCESSFULLY_WITHDRAWED_CLAIMS("messages.successfully-withdrawed-claims", "&a&lSUCCESS! &fYou successfully withdrew %amount% claims!"),
	NOT_ENOUGH_MONEY("messages.not-enough-money", "&c&lERROR: &fYou don't have enough money to withdraw! (%total%)"),
	SUCCESSFULLY_DEPOSITED_MONEY("messages.successfully-deposited-money", "&a&lSUCCESS! &fYou successfully deposited $%amount%!"),
	SUCCESSFULLY_WITHDRAWED_MONEY("messages.successfully-withdrawed-money", "&a&lSUCCESS! &fYou successfully withdrew $%amount%!"),
	NOT_ENOUGH_EXP("messages.not-enough-exp", "&c&lERROR: &fYou don't have enough EXP to withdraw! (%total%)"),
	SUCCESSFULLY_DEPOSITED_EXP("messages.successfully-deposited-exp", "&a&lSUCCESS! &fYou successfully deposited %amount% EXP!"),
	SUCCESSFULLY_WITHDRAWED_EXP("messages.successfully-withdrawed-exp", "&a&lSUCCESS! &fYou successfully withdrew %amount% EXP!"),
	INVENTORY_FULL("messages.inventory-full", "&c&lERROR! &fInventory full!"),
	RECEIVED_VOUCHER("messages.received-voucher", "&a&lSUCCESS! &fYou have received &e%amount%x &a%type% &fVouchers!"),
	GAVE_VOUCHER("messages.gave-voucher", "&a&lSUCCESS! &fYou have given %player% %amount%x %type% Vouchers!"),
	VOUCHER_CLAIMED("messages.voucher-claimed", "&c&lERROR! &fVoucher has already been claimed! Contact Staff if this is an Error!");
	private final String path;

	private String def;
	private static ConfigFile config;

	TL(String path, String start) {
		this.path = path;
		this.def = start;
	}

	public String getDefault() {
		return this.def;
	}

	public String getPath() {
		return this.path;
	}

	public void setDefault(String message) {
		this.def = message;
	}

	public void broadcast(PlaceHolder... placeHolders) {
		Bukkit.getOnlinePlayers().forEach(player -> {
			send(player, placeHolders);
		});
	}

	public void send(CommandSender sender) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			sender.sendMessage(PlaceholderAPI.setPlaceholders(player, C.color(getDefault())));
		} else {
			sender.sendMessage(C.strip(getDefault()));
		}
	}

	public static void loadMessages(ConfigFile configFile) {
		config = configFile;
		FileConfiguration data = configFile.getConfig();
		for (TL message : values()) {
			if (!data.contains(message.getPath())) {
				data.set(message.getPath(), message.getDefault());
			}
		}
		configFile.save();
	}


	public void send(CommandSender sender, PlaceHolder... placeHolders) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			sender.sendMessage(PlaceholderAPI.setPlaceholders(player, C.color(getDefault(), placeHolders)));
		} else {
			sender.sendMessage(C.strip(getDefault(), placeHolders));
		}
	}

	public static void message(CommandSender sender, String message) {
		sender.sendMessage(C.color(message));
	}

	public static void message(CommandSender sender, String message, PlaceHolder... placeHolders) {
		sender.sendMessage(C.color(message, placeHolders));
	}

	public static void message(CommandSender sender, List<String> message) {
		message.forEach(m -> sender.sendMessage(C.color(m)));
	}

	public static void message(CommandSender sender, List<String> message, PlaceHolder... placeHolders) {
		message.forEach(m -> sender.sendMessage(C.color(m, placeHolders)));
	}

	public static void log(Level lvl, String message) {
		Bukkit.getLogger().log(lvl, message);
	}
}
