package com.unclecole.mfvoucher.database;

import com.unclecole.mfvoucher.database.serializer.Serializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ClaimVoucherData {

    public static transient ClaimVoucherData instance = new ClaimVoucherData();

    public static HashMap<String, Boolean> ID = new HashMap<>();

    public static void save() {
        new Serializer().save(instance);
    }

    public static void load() {
        new Serializer().load(instance, ClaimVoucherData.class, "claimvoucherdata");
    }
}
