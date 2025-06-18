package com.protoxon.promenu.packets;

public class Packet {

    private static final InventoryPackets inventoryPackets = new InventoryPackets();
    private static final SoundPackets soundPackets = new SoundPackets();

    public static InventoryPackets inventory() {
        return inventoryPackets;
    }

    public static SoundPackets sound() {
        return soundPackets;
    }
}
