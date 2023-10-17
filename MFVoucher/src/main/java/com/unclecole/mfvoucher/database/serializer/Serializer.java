package com.unclecole.mfvoucher.database.serializer;

import com.unclecole.mfvoucher.MFVoucher;

public class Serializer {


    /**
     * Saves your class to a .json file.
     */
    public void save(Object instance) {
        MFVoucher.getPersist().save(instance);
    }

    /**
     * Loads your class from a json file
     *
   */
    public <T> T load(T def, Class<T> clazz, String name) {
        return MFVoucher.getPersist().loadOrSaveDefault(def, clazz, name);
    }



}
