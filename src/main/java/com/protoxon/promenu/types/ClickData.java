package com.protoxon.promenu.types;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.player.User;

public class ClickData {

    public final User user;
    public final int slot;
    public final ButtonType buttonType;
    public final ClickType clickType;
    public final ItemStack carriedItemStack;

    public ClickData(ButtonType buttonType, ClickType clickType, int slot, ItemStack carriedItemStack, User user) {
        this.clickType = clickType;
        this.buttonType = buttonType;
        this.slot = slot;
        this.user = user;
        this.carriedItemStack = carriedItemStack;
    }
}
