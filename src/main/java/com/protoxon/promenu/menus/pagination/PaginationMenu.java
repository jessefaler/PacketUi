package com.protoxon.promenu.menus.pagination;

import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.sound.Sounds;
import com.protoxon.promenu.Item;
import com.protoxon.promenu.map.MapType;
import com.protoxon.promenu.menus.SortType;
import com.protoxon.promenu.menus.FavoritesMenu;
import com.protoxon.promenu.menus.search.SearchMenu;
import com.protoxon.promenu.menus.SelectionMenu;
import com.protoxon.promenu.packets.Packet;
import com.protoxon.promenu.service.Content;
import com.protoxon.promenu.service.Menu;
import com.protoxon.promenu.service.MenuService;
import com.protoxon.promenu.types.InventoryType;
import com.protoxon.promenu.user.UserRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.List;
import java.util.function.Consumer;

public abstract class PaginationMenu extends Menu {

    public int page;
    public long updateSpeed = 8L;
    private SortType sortType = SortType.FEATURED;

    public PaginationMenu(Component title, InventoryType inventoryType, User user) {
        super(title, inventoryType, user);
    }

    public void onOpen() {
        page = 1;
        setContent(toolbarContent());
        update();
    }

    public abstract MapType getMapType();

    public abstract void onNext();

    public abstract void onPrevious();

    public abstract String getMenuName();

    public Content toolbarContent() {

        Content content = new Content(inventoryType);

        content.fillRow(1, Item.builder().setType(ItemTypes.BLACK_STAINED_GLASS_PANE).setHideTooltip(true));
        content.fillRow(6, Item.builder().setType(ItemTypes.BLACK_STAINED_GLASS_PANE).setHideTooltip(true));
        content.fillColumn(1, Item.builder().setType(ItemTypes.BLACK_STAINED_GLASS_PANE).setHideTooltip(true));
        content.fillColumn(9, Item.builder().setType(ItemTypes.BLACK_STAINED_GLASS_PANE).setHideTooltip(true));

        content.add(
                Item.slot(1, 9),
                Item.builder()
                        .setType(ItemTypes.NAME_TAG)
                        .setName(Component.text("Search").color(NamedTextColor.GOLD).decoration(TextDecoration.UNDERLINED, true))
                        .setLore(List.of(Component.text("Click to search for maps.").color(NamedTextColor.GRAY)))
                        .setClickAction(clickData -> {
                            Packet.sound().play(Sounds.UI_TOAST_IN, clickData.user);
                            new SearchMenu(user, this, getMapType()).open();
                        })
        );

        content.add(
                Item.slot(1, 1),
                Item.builder()
                        .setType(ItemTypes.BARRIER)
                        .setName(Component.text("◀ Back").color(NamedTextColor.BLUE))
                        .setClickAction(clickData -> {
                            Packet.sound().play(Sounds.UI_TOAST_OUT, user);
                            new SelectionMenu(user).open();
                        })
        );

        // Nav Left (Previous Page)
        content.add(
                Item.slot(6, 4),
                Item.builder()
                        .setType(ItemTypes.PLAYER_HEAD)
                        .setName(Component.text("Previous Page").color(NamedTextColor.GREEN))
                        .setHeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RjOWU0ZGNmYTQyMjFhMWZhZGMxYjViMmIxMWQ4YmVlYjU3ODc5YWYxYzQyMzYyMTQyYmFlMWVkZDUifX19")
                        .setClickAction(clickData -> {
                            if(page <= 1) {
                                Packet.sound().play(Sounds.BLOCK_NOTE_BLOCK_GUITAR, 0, user);
                                return;
                            }
                            page--;
                            onPrevious();
                            Packet.sound().play(Sounds.UI_TOAST_OUT, user);
                            updatePageNumber(content);
                        })
        );

        // Page Number
        content.add(
                Item.slot(6, 5),
                Item.builder()
                        .setType(ItemTypes.PAPER)
                        .setName(Component.text("Page: ").color(NamedTextColor.DARK_AQUA).append(Component.text(page + "/" + maxPage()).color(NamedTextColor.GOLD)))
        );

        // Nav Right (Next Page)
        content.add(
                Item.slot(6, 6),
                Item.builder()
                        .setType(ItemTypes.PLAYER_HEAD)
                        .setName(Component.text("Next Page").color(NamedTextColor.GREEN))
                        .setHeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTU2YTM2MTg0NTllNDNiMjg3YjIyYjdlMjM1ZWM2OTk1OTQ1NDZjNmZjZDZkYzg0YmZjYTRjZjMwYWI5MzExIn19fQ==")
                        .setClickAction(clickData -> {
                            if(page >= maxPage()) {
                                Packet.sound().play(Sounds.BLOCK_NOTE_BLOCK_GUITAR, 0, user);
                                return;
                            }
                            page++;
                            onNext();
                            updatePageNumber(content);
                            Packet.sound().play(Sounds.UI_TOAST_IN, user);
                        })
        );

        SortType initialState = SortType.FEATURED;
        Item toggle = sortToggle(initialState, content, newState -> {
            user.sendMessage(newState.name());
        });
        content.add(Item.slot(3, 9), toggle);

        content.add(
                Item.slot(3, 1),
                Item.builder()
                        .setType(ItemTypes.WRITABLE_BOOK)
                        .setName(Component.text("Favorites").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.UNDERLINED, true))
                        .setClickAction(clickData -> {
                            Packet.sound().play(Sounds.UI_TOAST_IN, clickData.user);
                            new FavoritesMenu(user, Component.text("Favorited " + getMenuName()).color(TextColor.color(0x1063a1)).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.UNDERLINED, true), this, UserRegistry.getUserData(user).getFavoriteMaps(getMapType()), getMapType()).open();
                        })
        );

        content.add(
                Item.slot(4, 1),
                Item.builder()
                        .setType(ItemTypes.CLOCK)
                        .setName(Component.text("History").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.UNDERLINED, true))
                        .setClickAction(clickData -> {
                            clickData.user.sendMessage(Component.text("History").color(NamedTextColor.GOLD));
                        })
        );

        content.add(
                Item.slot(4, 9),
                Item.builder()
                        .setType(ItemTypes.KNOWLEDGE_BOOK)
                        .setName(Component.text("Web GUI").color(NamedTextColor.GOLD))
                        .setLore(List.of(
                                Component.text("Click to open the web gui.").color(NamedTextColor.GRAY),
                                Component.text("! Coming soon !").color(NamedTextColor.RED)
                        ))
                        .setClickAction(clickData -> {
                            Packet.sound().play(Sounds.BLOCK_NOTE_BLOCK_GUITAR, 0, user);
                        })
        );

        return content;
    }

    public abstract int maxPage();

    public abstract List<Component> categories();

    public void updatePageNumber(Content content) {
        Item item = content.getItem(Item.slot(6, 5)).setAmount(page).setName(Component.text("Page: ").color(NamedTextColor.DARK_AQUA).append(Component.text(page + "/" + maxPage()).color(NamedTextColor.GOLD)));
        content.add(Item.slot(6, 5), item);
        update();
    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    private Item sortToggle(SortType state, Content content, Consumer<SortType> stateUpdater) {
        Item item = Item.builder().setType(ItemTypes.ENDER_EYE);
        item.setName(Component.text("Sort").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.UNDERLINED, true));
        switch (state) {
            case FEATURED ->
                    item.setLore(List.of(
                    Component.text("Featured").color(NamedTextColor.DARK_AQUA),
                    Component.text("Play Count").color(NamedTextColor.GRAY),
                    Component.text("Active Games").color(NamedTextColor.GRAY)
            ));
            case PLAY_COUNT ->
                    item.setLore(List.of(
                            Component.text("Featured").color(NamedTextColor.GRAY),
                            Component.text("Play Count").color(NamedTextColor.DARK_AQUA),
                            Component.text("Active Games").color(NamedTextColor.GRAY)
                    ));
            case ACTIVE_GAMES ->
                    item.setLore(List.of(
                            Component.text("Featured").color(NamedTextColor.GRAY),
                            Component.text("Play Count").color(NamedTextColor.GRAY),
                            Component.text("Active Games").color(NamedTextColor.DARK_AQUA)
                    ));
        }

        item.setClickAction(clickData -> {
            SortType next = state.next();
            setSortType(next);
            stateUpdater.accept(next);

            // Update player menu
            content.add(Item.slot(3, 9), sortToggle(next, content, stateUpdater));

            Packet.sound().play(Sounds.ENTITY_EXPERIENCE_ORB_PICKUP, clickData.user);
            update();
            clearSlots();
            onNext();
        });

        return item;
    }

    public void clearSlots() {
        int row = 2;
        int col = 2;
        while(row <= 5) {
            while (col <= 8) {
                getContent().add(Item.slot(row, col), null);
                col++;
            }
            col = 2;
            row++;
        }
    }
}
