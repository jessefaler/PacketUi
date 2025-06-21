package com.protoxon.promenu.controller;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPositionAndRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSlotStateChange;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetSlot;

public class PacketListener implements com.github.retrooper.packetevents.event.PacketListener {

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW) {
            MenuListener.onClickWindow(event);
        }

        if (event.getPacketType() == PacketType.Play.Client.CLOSE_WINDOW) {
            MenuListener.onCloseWindow(event);
        }

        if (event.getPacketType() == PacketType.Play.Client.NAME_ITEM) {
            MenuListener.onNameItem(event);
        }

    }
}
