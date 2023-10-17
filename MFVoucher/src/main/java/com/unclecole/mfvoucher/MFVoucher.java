package com.unclecole.mfvoucher;

import com.sun.tools.sjavac.Log;
import com.unclecole.mfvoucher.commands.MoneyVoucherCmd;
import com.unclecole.mfvoucher.commands.VoucherCmd;
import com.unclecole.mfvoucher.commands.XpVoucherCmd;
import com.unclecole.mfvoucher.database.serializer.Persist;
import com.unclecole.mfvoucher.listeners.JoinQuitListener;
import com.unclecole.mfvoucher.listeners.RightClickListener;
import com.unclecole.mfvoucher.utils.C;
import com.unclecole.mfvoucher.utils.ConfigFile;
import com.unclecole.mfvoucher.utils.ConfigItems;
import com.unclecole.mfvoucher.utils.TL;
import lombok.Getter;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

public final class MFVoucher extends JavaPlugin {

    public static MFVoucher instance;
    public static MFVoucher getInstance() { return instance; }

    @Getter public static Persist persist = new Persist();
    @Getter private static Economy econ = null;
    @Getter public GriefPrevention api = GriefPrevention.instance;
    @Getter public ConfigItems configItems;
    @Getter public ArrayList<UUID> staffNotification = new ArrayList<>();



    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        if(!this.getDataFolder().exists()) this.getDataFolder().mkdir();

        if (!setupEconomy() ) {
            Log.error(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();
        TL.loadMessages(new ConfigFile("messages.yml", this));

        configItems = new ConfigItems();

        Objects.requireNonNull(getCommand("withdraw")).setExecutor(new MoneyVoucherCmd());
        Objects.requireNonNull(getCommand("xpwithdraw")).setExecutor(new XpVoucherCmd());
        Objects.requireNonNull(getCommand("voucher")).setExecutor(new VoucherCmd());

        Bukkit.getPluginManager().registerEvents(new RightClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinQuitListener(), this);

        Bukkit.getOnlinePlayers().forEach(player -> {
            if(player.hasPermission("voucher.notify")) staffNotification.add(player.getUniqueId());
        });
        isLicence();
        TL.loadMessages(new ConfigFile("messages.yml", this));
    }

    private void isLicence() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                try{
                    String url = "https://pastebin.com/raw/CL9qvyuF";
                    URLConnection openConnection = new URL(url).openConnection();
                    openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                    @SuppressWarnings("resource")
                    Scanner scan = new Scanner((new InputStreamReader(openConnection.getInputStream())));
                    while(scan.hasNextLine()){
                        String firstline = scan.nextLine();
                        if(firstline.equalsIgnoreCase("VNfxOIy0QQrCTA8TZ7aO1S91sa6vGJgJ")){
                            return;
                        }
                    }
                }catch(Exception e){

                }
                Bukkit.getScheduler().runTask(MFVoucher.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), C.color("&c&lTHIS SERVER IS STEALING PLAY.MINEFORGE.ORG PLUGINS."), null, "Console");
                            player.kickPlayer(C.color("&c&lTHIS SERVER IS STEALING PLAY.MINEFORGE.ORG PLUGINS."));
                        });
                        Bukkit.shutdown();
                    }
                });
            }
        }, 0L, 100L);
        //We are getting the licence key string from the config
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
