package com.protoxon.promenu.controller;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetSlot;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

public class PacketListener implements com.github.retrooper.packetevents.event.PacketListener {

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW) {
            MenuListener.onClickWindow(event);
        }

        if (event.getPacketType() == PacketType.Play.Client.CLOSE_WINDOW) {
            MenuListener.onCloseWindow(event);
        }
    }

}
