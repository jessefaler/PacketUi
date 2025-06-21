package com.protoxon.promenu.service;

import com.github.retrooper.packetevents.protocol.player.User;
import com.protoxon.promenu.Item;
import com.protoxon.promenu.packets.Packet;
import com.protoxon.promenu.types.InventoryType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.*;

public class Content {

    private final Item[] items;
    // Holds menu modifications that have not been updated yet
    private final Set<Integer> modifications = new HashSet<>();

    public Content(InventoryType inventoryType) {
        items = new Item[inventoryType.getSize()];
    }

    public synchronized void add(int slot, Item item) {
        modifications.add(slot);
        items[slot] = item;
    }

    /**
     * Updates the inventory for the given user based on the number of item modifications.
     * <p>
     * If there are 5 or more modified slots, a full inventory update is sent using a single
     * {@code WINDOW_ITEMS} packet. Otherwise, individual {@code SET_SLOT} packets are sent for
     * each modified slot.
     * </p>
     *
     * @param user the target user to receive the inventory update
     */
    public synchronized void update(User user) {
        if(modifications.size() >= 5) {
            user.sendMessage(Component.text("Performed full inventory update.").color(NamedTextColor.GRAY));
            Packet.inventory().putItems(user, getItems());
            modifications.clear();
            return;
        }
        for(Integer index : modifications) {
            Packet.inventory().putItem(user, items[index], index);
        }
        user.sendMessage(Component.text("Performed partial inventory update. (" + modifications.size() + " changes)").color(NamedTextColor.GRAY));
        modifications.clear();
    }

    public void resend(User user) {
        Packet.inventory().putItems(user, getItems());
    }

    public List<Item> getItems() {
        return Arrays.asList(items);
    }

    public Item getItem(int slot) {
        return items[slot];
    }

    /**
     * Fills a row in the inventory with the given item.
     * @param row the 1-based row index (1 = top row)
     * @param item the item to place in the row
     */
    public synchronized void fillRow(int row, Item item) {
        int columns = 9; // standard row size
        int start = (row - 1) * columns;
        for (int i = 0; i < columns; i++) {
            int slot = start + i;
            if (slot >= items.length) break;
            add(slot, item);
        }
    }

    /**
     * Fills a column in the inventory with the given item.
     * @param column the 1-based column index (1 = leftmost column)
     * @param item the item to place in the column
     */
    public synchronized void fillColumn(int column, Item item) {
        int columns = 9; // standard column count
        int col = column - 1;
        for (int row = 0; row < items.length / columns; row++) {
            int slot = row * columns + col;
            if (slot >= items.length) break;
            add(slot, item);
        }
    }
}
