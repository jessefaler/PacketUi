package com.protoxon.promenu.menus;

import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.sound.Sounds;
import com.protoxon.promenu.Item;
import com.protoxon.promenu.map.Map;
import com.protoxon.promenu.map.MapType;
import com.protoxon.promenu.menus.pagination.PaginationMenu;
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

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HistoryMenu extends PaginationMenu {
    private Menu returnMenu;
    private static final InventoryType INVENTORY_TYPE = InventoryType.GENERIC9X6;
    private List<ScheduledFuture<?>> activeTasks;
    private List<Map> maps;
    private MapType mapType;

    public HistoryMenu(User user, Component title, Menu returnMenu, List<Map> maps, MapType mapType) {
        super(title, INVENTORY_TYPE, user);
        this.returnMenu = returnMenu;
        this.maps = maps;
        this.mapType = mapType;
    }

    @Override
    public void onOpen() {
        activeTasks =  new CopyOnWriteArrayList<>();
        super.onOpen();
        pageContent();
        bodyContent();
        update();
    }

    @Override
    public void onClose() {
        Packet.sound().play(Sounds.UI_TOAST_OUT, user);
        returnMenu.reopen();
    }

    @Override
    public MapType getMapType() {
        return mapType;
    }

    @Override
    public void onNext() {
        bodyContent();
    }

    @Override
    public void onPrevious() {
        bodyContent();
    }

    @Override
    public String getMenuName() {
        return "Favorites";
    }

    @Override
    public int maxPage() {
        int totalMaps = maps.size();
        int itemsPerPage = 28;
        return (int) Math.ceil((double) totalMaps / itemsPerPage);
    }

    @Override
    public List<Component> categories() {
        return List.of();
    }

    public void refresh() {
        maps = UserRegistry.getUserData(user).getFavoriteMaps(getMapType());
        reopen();
        bodyContent();
        if(maps.isEmpty()) {
            clearSlots();
            update();
        }
    }

    public void pageContent() {

        Content content = getContent();

        content.fillRow(1, Item.builder().setType(ItemTypes.BLACK_STAINED_GLASS_PANE));
        content.fillRow(6, Item.builder().setType(ItemTypes.BLACK_STAINED_GLASS_PANE));
        content.fillColumn(1, Item.builder().setType(ItemTypes.BLACK_STAINED_GLASS_PANE));
        content.fillColumn(9, Item.builder().setType(ItemTypes.BLACK_STAINED_GLASS_PANE));

        content.add(
                Item.slot(1, 1),
                Item.builder()
                        .setType(ItemTypes.BARRIER)
                        .setName(Component.text("◀ Back").color(NamedTextColor.BLUE))
                        .setClickAction(clickData -> {
                            Packet.sound().play(Sounds.UI_TOAST_OUT, user);
                            returnMenu.reopen();
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

    }

    public void bodyContent() {
        cancelPendingTasks();
        if (maps.isEmpty()) {
            return;
        }
        clearSlots();

        List<Map> maps = getMapsInRange(page, this.maps);

        int row = 2;
        int col = 2;
        int index = 0;

        for (int i = maps.size() - 1; i >= 0; i--) {
            Map map = maps.get(i);
            final int finalRow = row;
            final int finalCol = col;
            final Map finalMap = map;

            ScheduledFuture<?> future = MenuService.getScheduler().schedule(() -> {
                getContent().add(Item.slot(finalRow, finalCol),
                        Item.builder()
                                .setType(ItemTypes.GRAY_WOOL)
                                .setName(Component.text(finalMap.getName()).color(NamedTextColor.GOLD))
                                .setLore(List.of(
                                        Component.text("Authors: ").color(NamedTextColor.DARK_AQUA).append(Component.text(finalMap.getAuthors()).color(NamedTextColor.GOLD)),
                                        Component.text("Tag: ").color(NamedTextColor.DARK_AQUA).append(Component.text(finalMap.getSearchTag().name()).color(NamedTextColor.GOLD))
                                ))
                                .setClickAction(clickData -> {
                                    Packet.sound().play(Sounds.UI_TOAST_IN, user);
                                    Packet.sound().play(Sounds.ENTITY_ITEM_FRAME_ROTATE_ITEM, clickData.user);
                                    new MapOptionsMenu(user, finalMap, this).open();
                                })
                );
                update();
            }, index * updateSpeed, TimeUnit.MILLISECONDS);

            activeTasks.add(future);
            index++;

            col++;
            if (col == 9) {
                row++;
                col = 2;
            }

            if (row >= 6) break;
        }
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

    public void cancelPendingTasks() {
        for (ScheduledFuture<?> task : activeTasks) {
            task.cancel(false); // false = don’t interrupt if running
        }
        activeTasks.clear();
    }

    public List<Map> getMapsInRange(int pageNumber, List<Map> allMaps) {
        int itemsPerPage = 28;
        int startIndex = (pageNumber - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, allMaps.size());

        if (startIndex >= allMaps.size()) {
            return Collections.emptyList(); // No maps on this page
        }

        return allMaps.subList(startIndex, endIndex);
    }
}
