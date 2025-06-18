package com.protoxon.promenu.menus.pagination;

import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.sound.Sounds;
import com.protoxon.promenu.Item;
import com.protoxon.promenu.menus.SelectionMenu;
import com.protoxon.promenu.packets.Packet;
import com.protoxon.promenu.service.Content;
import com.protoxon.promenu.service.Menu;
import com.protoxon.promenu.service.MenuService;
import com.protoxon.promenu.types.InventoryType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.HashMap;
import java.util.List;

public abstract class PaginationMenu extends Menu {

    private HashMap<User, Integer> page = new HashMap<User, Integer>();

    public PaginationMenu(Component title, InventoryType inventoryType, User user) {
        super(title, inventoryType, user);
    }

    public void onOpen() {
        setContent(toolbarContent());
        update();
    }

    //public abstract void onNext();

    //public abstract void onPrevious();

    //public abstract void onPageNavigation();


    public Content toolbarContent() {

        Content content = new Content(inventoryType);

        content.add(
                Item.slot(6, 5),
                Item.builder()
                        .setType(ItemTypes.NAME_TAG)
                        .setName(Component.text("Search").color(NamedTextColor.GOLD).decoration(TextDecoration.UNDERLINED, true))
                        .setLore(List.of(Component.text("Click to search for maps.").color(NamedTextColor.GRAY)))
                        .setClickAction(clickData -> {
                            clickData.user.sendMessage(Component.text("Sort By").color(NamedTextColor.GOLD));
                        })
        );

        content.add(
                Item.slot(6, 2),
                Item.builder()
                        .setType(ItemTypes.ENDER_EYE)
                        .setName(Component.text("Sort").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.UNDERLINED, true))
                        .setLore(List.of(
                                Component.text("Default").color(NamedTextColor.DARK_AQUA),
                                Component.text("Play Count").color(NamedTextColor.GRAY),
                                Component.text("Alphabetical").color(NamedTextColor.GRAY),
                                Component.text("Release Date").color(NamedTextColor.GRAY),
                                Component.text("Minecraft Version").color(NamedTextColor.GRAY)
                        ))
                        .setClickAction(clickData -> {
                            clickData.user.sendMessage(Component.text("Sort By").color(NamedTextColor.GOLD));
                        })
        );

        // Ascending and Descending toggle items
        Item ascending = Item.builder()
                .setType(ItemTypes.AMETHYST_SHARD)
                .setName(Component.text("Sort Order").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.UNDERLINED, true))
                .setLore(List.of(
                        Component.text("Ascending").color(NamedTextColor.DARK_AQUA),
                        Component.text("Descending").color(NamedTextColor.GRAY)
                ));

        Item descending = Item.builder()
                .setType(ItemTypes.LAPIS_LAZULI)
                .setName(Component.text("Sort Order").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.UNDERLINED, true))
                .setLore(List.of(
                        Component.text("Ascending").color(NamedTextColor.GRAY),
                        Component.text("Descending").color(NamedTextColor.DARK_AQUA)
                ));

        ascending.setClickAction(clickData -> {
            MenuService.getMenu(user).getContent().add(Item.slot(6, 3), descending);
            Packet.sound().play(Sounds.ENTITY_EXPERIENCE_ORB_PICKUP, clickData.user);
            update();
        });
        descending.setClickAction(clickData -> {
            MenuService.getMenu(user).getContent().add(Item.slot(6, 3), ascending);
            Packet.sound().play(Sounds.ENTITY_EXPERIENCE_ORB_PICKUP, clickData.user);
            update();
        });

        content.add(Item.slot(6, 3), ascending);

        content.add(
                Item.slot(1, 1),
                Item.builder()
                        .setType(ItemTypes.BARRIER)
                        .setName(Component.text("◀ Back").color(NamedTextColor.BLUE))
                        .setClickAction(clickData -> new SelectionMenu(user))
        );

        content.add(
                Item.slot(6, 4),
                Item.builder()
                        .setType(ItemTypes.PLAYER_HEAD)
                        .setName(Component.text("Next").color(NamedTextColor.GREEN))
                        .setLore(List.of(Component.text("Next Page").color(NamedTextColor.DARK_GRAY)))
                        .setHeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTU2YTM2MTg0NTllNDNiMjg3YjIyYjdlMjM1ZWM2OTk1OTQ1NDZjNmZjZDZkYzg0YmZjYTRjZjMwYWI5MzExIn19fQ==")
                        .setClickAction(clickData -> {
                            clickData.user.sendMessage(Component.text("Loading next page").color(NamedTextColor.GOLD));
                        })
        );

        content.add(
                Item.slot(6, 1),
                Item.builder()
                        .setType(ItemTypes.KNOWLEDGE_BOOK)
                        .setName(Component.text("Web GUI").color(NamedTextColor.GOLD))
                        .setLore(List.of(
                                Component.text("Click to open the web gui.").color(NamedTextColor.GRAY),
                                Component.text("! Coming soon !").color(NamedTextColor.RED)
                        ))
                        .setClickAction(clickData -> new SelectionMenu(user))
        );

        content.add(
                Item.slot(6, 9),
                Item.builder()
                        .setType(ItemTypes.BOOK)
                        .setName(Component.text("Category").color(NamedTextColor.GREEN))
                        .setLore(categories())
                        .setClickAction(clickData -> {
                            clickData.user.sendMessage(Component.text("Category").color(NamedTextColor.GOLD));
                        })
        );


        return content;
    }

    public abstract List<Component> categories();


}
