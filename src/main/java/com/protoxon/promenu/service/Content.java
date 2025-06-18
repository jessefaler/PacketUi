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

    public void add(int slot, Item item) {
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
    public void update(User user) {
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

    public List<Item> getItems() {
        return Arrays.asList(items);
    }

    public Item getItem(int slot) {
        return items[slot];
    }

}
