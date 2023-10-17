package com.unclecole.mfvoucher.database;

import com.unclecole.mfvoucher.database.serializer.Serializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MoneyVoucherData {

    public static transient MoneyVoucherData instance = new MoneyVoucherData();

    public static HashMap<String, Boolean> ID = new HashMap<>();

    public static void save() {
        new Serializer().save(instance);
    }

    public static void load() {
        new Serializer().load(instance, MoneyVoucherData.class, "moneyvoucherdata");
    }
}
