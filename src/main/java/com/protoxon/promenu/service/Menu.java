package com.protoxon.promenu.service;

import com.github.retrooper.packetevents.protocol.player.User;
import com.protoxon.promenu.packets.Packet;
import com.protoxon.promenu.types.InventoryType;
import net.kyori.adventure.text.Component;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Menu {

    public final User user;
    private Content content;
    private Component title;
    protected final InventoryType inventoryType;
    public int windowId;

    public Menu(Component title, InventoryType inventoryType, User user) {
        this.user = user;
        this.windowId = getWindowId();
        MenuService.addMenu(user, this);
        this.inventoryType = inventoryType;
        this.title = title;
        open();
    }

    public void open() {
        Packet.inventory().open(user, inventoryType, title);
        onOpen();
    }

    private int getWindowId() {
        Menu menu = MenuService.getMenu(user);
        if (menu == null) return ThreadLocalRandom.current().nextInt(30, 126);

        int windowId;
        do {
            windowId = ThreadLocalRandom.current().nextInt(30, 126);
        } while (windowId == menu.windowId);
        return windowId;
    }

    public abstract void onOpen();

    public void close() {
        Packet.inventory().close(user);
    }

    public void setTitle(Component title) {
        this.title = title;
        close();
        open();
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public void update() {
        if(content == null) return;
        content.update(user);
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }

}
