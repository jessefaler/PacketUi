package com.protoxon.promenu.packets;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCloseWindow;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenWindow;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetSlot;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowItems;
import com.protoxon.promenu.Item;
import com.protoxon.promenu.service.MenuService;
import com.protoxon.promenu.types.InventoryType;
import net.kyori.adventure.text.Component;

import java.util.List;

public class InventoryPackets {

    public void open(User user, InventoryType inventoryType, Component title) {

        WrapperPlayServerOpenWindow openWindowPacket = new WrapperPlayServerOpenWindow(
                MenuService.getWindowId(user),
                inventoryType.id(),
                title
        );
        user.sendPacket(openWindowPacket);

    }

    public void close(User user) {
        WrapperPlayServerCloseWindow closePacket = new WrapperPlayServerCloseWindow(126);
        user.sendPacket(closePacket);
    }

    public void putItem(User user, Item item, int slot) {
        WrapperPlayServerSetSlot setSlotPacket = new WrapperPlayServerSetSlot(
                MenuService.getWindowId(user),
                0,
                slot,
                item.build()
        );
        user.sendPacket(setSlotPacket);
    }

    public void putItems(User user, List<Item> items) {
        List<ItemStack> stacks = items.stream()
                .map(item -> {
                    if (item == null) return ItemStack.builder().build();
                    return item.build();
                })
                .toList();

        WrapperPlayServerWindowItems updatePacket = new WrapperPlayServerWindowItems(
                MenuService.getWindowId(user),
                0,
                stacks,
                null
        );
        user.sendPacket(updatePacket);
    }
}
