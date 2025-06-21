package com.protoxon.promenu.menus;

import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.sound.Sounds;
import com.protoxon.promenu.Item;
import com.protoxon.promenu.menus.pagination.AdventureMapsPaginationMenu;
import com.protoxon.promenu.menus.pagination.MinigamesPaginationMenu;
import com.protoxon.promenu.packets.Packet;
import com.protoxon.promenu.service.MenuService;
import com.protoxon.promenu.types.InventoryType;
import com.protoxon.promenu.service.Content;
import com.protoxon.promenu.service.Menu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.List;

public class SelectionMenu extends Menu {

    private static final Component TITLE = Component.text("Game Menu").decoration(TextDecoration.BOLD, true).color(NamedTextColor.DARK_GREEN);
    private static final InventoryType INVENTORY_TYPE = InventoryType.GENERIC9X3;

    public SelectionMenu(User user) {
        super(TITLE, INVENTORY_TYPE, user);
    }

    @Override
    public void onOpen() {
        setContent(pageContent());
        update();
    }

    @Override
    public void onClose() {
        MenuService.removeMenu(user);
    }

    public Content pageContent() {

        Content content = new Content(INVENTORY_TYPE);

        content.add(
                Item.slot(2, 2),
                Item.builder()
                        .setType(ItemTypes.CRAFTING_TABLE)
                        .setName(Component.text("Minigames").color(NamedTextColor.GOLD))
                        .setClickAction(clickData -> {
                            Packet.sound().play(Sounds.UI_TOAST_IN, clickData.user);
                            new MinigamesPaginationMenu(user).open();
                            clickData.user.sendMessage(Component.text("Opening minigames menu").color(NamedTextColor.GREEN));
                        })
        );

        content.add(
                Item.slot(2, 4),
                Item.builder()
                        .setType(ItemTypes.BEACON)
                        .setName(Component.text("Adventure Maps").color(NamedTextColor.DARK_AQUA))
                        .setClickAction(clickData -> {
                            Packet.sound().play(Sounds.UI_TOAST_IN, clickData.user);
                            new AdventureMapsPaginationMenu(user).open();
                            clickData.user.sendMessage(Component.text("Opening adventure maps menu").color(NamedTextColor.GOLD));
                        })
        );

        content.add(
                Item.slot(2, 6),
                Item.builder()
                        .setType(ItemTypes.GRASS_BLOCK)
                        .setName(Component.text("Survival").color(NamedTextColor.GREEN))
                        .setClickAction(clickData -> {
                            clickData.user.sendMessage(Component.text("Joining Survival").color(NamedTextColor.GOLD));
                        })
        );

        content.add(
                Item.slot(2, 8),
                Item.builder()
                        .setType(ItemTypes.IRON_SWORD)
                        .setName(Component.text("Pvp").color(NamedTextColor.RED))
                        .setLore(List.of(Component.text("Coming Soon!").color(NamedTextColor.DARK_RED)))
                        .setClickAction(clickData -> {
                            clickData.user.sendMessage(Component.text("Pvp coming soon!").color(NamedTextColor.GOLD));
                        })
        );

        return content;
    }
}
