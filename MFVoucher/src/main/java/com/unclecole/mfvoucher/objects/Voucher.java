package com.unclecole.mfvoucher.objects;

import lombok.Getter;
import redempt.redlib.itemutils.ItemBuilder;

import java.util.List;

public class Voucher {

    @Getter private ItemBuilder claimNote;
    @Getter private String claimNoteName;
    @Getter private List<String> claimNoteLore;
    @Getter private List<String> commands;

    public Voucher(ItemBuilder claimNote, String claimNoteName, List<String> claimNoteLore, List<String> commands) {
        this.claimNote = claimNote;
        this.claimNoteName = claimNoteName;
        this.claimNoteLore = claimNoteLore;
        this.commands = commands;
    }
}
