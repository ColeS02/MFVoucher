package com.unclecole.mfvoucher.database;

import com.unclecole.mfvoucher.database.serializer.Serializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class XpVoucherData {

    public static transient XpVoucherData instance = new XpVoucherData();

    public static HashMap<String, Boolean> ID = new HashMap<>();

    public static void save() {
        new Serializer().save(instance);
    }

    public static void load() {
        new Serializer().load(instance, XpVoucherData.class, "xpvoucherdata");
    }
}
