package com.unclecole.mfvoucher.database;

import com.unclecole.mfvoucher.database.serializer.Serializer;

import java.util.HashMap;

public class VoucherData {

    public static transient VoucherData instance = new VoucherData();

    public static HashMap<String, Boolean> ID = new HashMap<>();

    public static void save() {
        new Serializer().save(instance);
    }

    public static void load() {
        new Serializer().load(instance, VoucherData.class, "voucherdata");
    }
}
