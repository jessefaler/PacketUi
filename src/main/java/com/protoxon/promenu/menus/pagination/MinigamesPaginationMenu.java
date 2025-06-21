package com.protoxon.promenu.menus.pagination;

import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.sound.Sounds;
import com.protoxon.promenu.Item;
import com.protoxon.promenu.map.Map;
import com.protoxon.promenu.map.MapRegistry;
import com.protoxon.promenu.map.MapType;
import com.protoxon.promenu.menus.MapOptionsMenu;
import com.protoxon.promenu.menus.SortType;
import com.protoxon.promenu.packets.Packet;
import com.protoxon.promenu.service.MenuService;
import com.protoxon.promenu.types.InventoryType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MinigamesPaginationMenu extends PaginationMenu {

    private static final Component TITLE = Component.text("Minigames").color(TextColor.color(0x1063a1)).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.UNDERLINED, true);
    private static final InventoryType INVENTORY_TYPE = InventoryType.GENERIC9X6;

    private List<ScheduledFuture<?>> activeTasks;

    public MinigamesPaginationMenu(User user) {
        super(TITLE, INVENTORY_TYPE, user);
    }

    @Override
    public void onOpen() {
        activeTasks =  new CopyOnWriteArrayList<>();
        super.onOpen();
        bodyContent();
    }

    @Override
    public MapType getMapType() {
        return MapType.MINIGAMES;
    }

    @Override
    public void onNext() {
        bodyContent();
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

    @Override
    public void onPrevious() {
        bodyContent();
    }

    @Override
    public int maxPage() {
        int totalMaps = MapRegistry.getMaps(getMapType()).size();
        int itemsPerPage = 28;
        return (int) Math.ceil((double) totalMaps / itemsPerPage);
    }

    @Override
    public List<Component> categories() {
        return List.of();
    }

    public void bodyContent() {
        cancelPendingTasks();
        clearSlots();
        user.sendMessage("page " + page);

        List<Map> maps = getMapsInRange(page, MapRegistry.getMaps(getMapType(), getSortType()));

        int row = 2;
        int col = 2;
        int index = 0;

        for (Map map : maps) {
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
                                        Component.text("Tag: ").color(NamedTextColor.DARK_AQUA).append(Component.text(finalMap.getSearchTag().name()).color(NamedTextColor.GOLD)),
                                        Component.text("Play Count: ").color(NamedTextColor.DARK_AQUA).append(Component.text(finalMap.getPlaycount())).color(NamedTextColor.GOLD),
                                        Component.text("Active Games: ").color(NamedTextColor.DARK_AQUA).append(Component.text(finalMap.getActiveGames())).color(NamedTextColor.GOLD)
                                ))
                                .setClickAction(clickData -> {
                                    Packet.sound().play(Sounds.UI_TOAST_IN, user);
                                    Packet.sound().play(Sounds.ENTITY_ITEM_FRAME_ROTATE_ITEM, clickData.user);
                                    new MapOptionsMenu(user, map, this).open();
                                })
                );
                update();
            }, index * updateSpeed, TimeUnit.MILLISECONDS);

// Track for cancellation later
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

    @Override
    public String getMenuName() {
        return "Minigames";
    }

    public void cancelPendingTasks() {
        for (ScheduledFuture<?> task : activeTasks) {
            task.cancel(false); // false = don’t interrupt if running
        }
        activeTasks.clear();
    }
}
