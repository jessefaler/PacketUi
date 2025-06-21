package com.protoxon.promenu.controller;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientNameItem;
import com.protoxon.promenu.Item;
import com.protoxon.promenu.menus.search.SearchMenu;
import com.protoxon.promenu.types.ClickData;
import com.protoxon.promenu.utils.ClickDataMapper;
import com.protoxon.promenu.types.ClickType;
import com.protoxon.promenu.packets.Packet;
import com.protoxon.promenu.service.Content;
import com.protoxon.promenu.service.Menu;
import com.protoxon.promenu.service.MenuService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class MenuListener {

    public static void onClickWindow(PacketReceiveEvent event) {
        User user = event.getUser();
        WrapperPlayClientClickWindow packet = new WrapperPlayClientClickWindow(event);
        Menu menu = MenuService.getMenu(user);
        if(menu == null || menu.windowId != packet.getWindowId()) {
            return;
        }
        ClickData clickData = ClickDataMapper.getClickData(packet, packet.getSlot(), user);

        user.sendMessage(Component.text("Click Type: " + clickData.clickType + ", Button Type: " + clickData.buttonType + ", Slot: " + clickData.slot + " Window Id: " + packet.getWindowId()).color(NamedTextColor.GOLD));

        if(clickData.clickType == ClickType.PICKUP_ALL) {
            return;
        }

        if(clickData.slot > menu.getInventoryType().getSize() - 1) {
            return;
        }

        handleItemClick(clickData, menu);

        if(packet.getWindowId() == MenuService.getWindowId(user)) { // Menu didnt change undo actions
            undoInteraction(user, menu, clickData);
        }

    }

    public static void onCloseWindow(PacketReceiveEvent event) {
        Menu menu = MenuService.getMenu(event.getUser());
        if(menu == null) return;
        menu.onClose();
        event.getUser().sendMessage("Closed Window");
    }

    public static void onNameItem(PacketReceiveEvent event) {
        WrapperPlayClientNameItem namePacket = new WrapperPlayClientNameItem(event);
        Menu menu = MenuService.getMenu(event.getUser());
        if(menu == null) return;
        if (menu instanceof SearchMenu searchMenu) {
            searchMenu.setInput(namePacket.getItemName());
        }
    }

    private static void undoInteraction(User user, Menu menu, ClickData clickData) {
        if (user.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_19_1)) {
            Packet.inventory().putItem(user, Item.builder(), -1);
        } else {
            Packet.inventory().putItems(user, menu.getContent().getItems());
        }

        if(clickData.slot < 0) return;
        Content content = menu.getContent();
        if(content == null) return;
        Item item = content.getItem(clickData.slot);
        if(item == null) return;
        Packet.inventory().putItem(clickData.user, item, clickData.slot);
    }

    private static void handleItemClick(ClickData clickData, Menu menu) {
        if(clickData.slot < 0) return;
        Content content = menu.getContent();
        if(content == null) return;
        Item item = content.getItem(clickData.slot);
        if(item == null) return;
        item.onClick(clickData);
    }

}
