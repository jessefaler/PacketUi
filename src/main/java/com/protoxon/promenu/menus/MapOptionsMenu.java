package com.protoxon.promenu.menus;

import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.sound.Sounds;
import com.protoxon.promenu.Item;
import com.protoxon.promenu.map.Map;
import com.protoxon.promenu.packets.Packet;
import com.protoxon.promenu.service.Content;
import com.protoxon.promenu.service.Menu;
import com.protoxon.promenu.types.InventoryType;
import com.protoxon.promenu.user.UserData;
import com.protoxon.promenu.user.UserRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class MapOptionsMenu extends Menu {

    private Map map;
    private static final InventoryType INVENTORY_TYPE = InventoryType.GENERIC9X3;
    private Menu returnMenu;

    public MapOptionsMenu(User user, Map map, Menu returnMenu) {
        super(Component.text(map.getName()).decoration(TextDecoration.UNDERLINED, true).decoration(TextDecoration.BOLD, true).color(NamedTextColor.DARK_PURPLE), INVENTORY_TYPE, user);
        this.map = map;
        this.returnMenu = returnMenu;
    }

    @Override
    public void onOpen() {
        setContent(pageContent());
        update();
    }

    @Override
    public void onClose() {
        Packet.sound().play(Sounds.UI_TOAST_OUT, user);
        if(returnMenu instanceof FavoritesMenu favoritesMenu) {
            favoritesMenu.refresh();
        } else {
            returnMenu.reopen();
        }
    }

    public Content pageContent() {

        Content content = new Content(INVENTORY_TYPE);

        content.fillRow(1, Item.builder().setType(ItemTypes.BLACK_STAINED_GLASS_PANE).setHideTooltip(true));
        content.fillRow(2, Item.builder().setType(ItemTypes.BLACK_STAINED_GLASS_PANE).setHideTooltip(true));
        content.fillRow(3, Item.builder().setType(ItemTypes.BLACK_STAINED_GLASS_PANE).setHideTooltip(true));

        content.add(
                Item.slot(2, 3),
                Item.builder()
                        .setType(ItemTypes.LIME_CONCRETE)
                        .setName(Component.text("Quick Play").color(NamedTextColor.GOLD))
                        .setClickAction(clickData -> {
                            clickData.user.sendMessage(Component.text("Joining game").color(NamedTextColor.GREEN));
                        })
        );

        content.add(
                Item.slot(2, 5),
                Item.builder()
                        .setType(ItemTypes.NETHER_STAR)
                        .setName(Component.text("Active Games").color(NamedTextColor.GOLD))
                        .setClickAction(clickData -> {
                            clickData.user.sendMessage(Component.text("Opening active games menu").color(NamedTextColor.GREEN));
                        })
        );

        UserData userData = UserRegistry.getUserData(user);

        // Ascending and Descending toggle items
        Item favorite = Item.builder()
                .setType(ItemTypes.WRITABLE_BOOK)
                .setName(Component.text("Add To Favorites").color(NamedTextColor.AQUA));

        Item unfavorite = Item.builder()
                .setType(ItemTypes.WRITABLE_BOOK)
                .setEnchantmentGlint(true)
                .setName(Component.text("Remove From Favorites").color(NamedTextColor.RED));

        favorite.setClickAction(clickData -> {
            userData.addFavoriteMap(map);
            content.add(Item.slot(2, 7), unfavorite);
            Packet.sound().play(Sounds.ENTITY_EXPERIENCE_ORB_PICKUP, clickData.user);
            update();
        });
        unfavorite.setClickAction(clickData -> {
            userData.removeFavoriteMap(map);
            content.add(Item.slot(2, 7), favorite);
            Packet.sound().play(Sounds.ENTITY_EXPERIENCE_ORB_PICKUP, 0.9f, clickData.user);
            update();
        });

        if(userData.isMapFavorite(map)) {
            content.add(Item.slot(2, 7), unfavorite);
        } else {
            content.add(Item.slot(2, 7), favorite);
        }

        content.add(
                Item.slot(1, 1),
                Item.builder()
                        .setType(ItemTypes.BARRIER)
                        .setName(Component.text("◀ Back").color(NamedTextColor.BLUE))
                        .setClickAction(clickData -> {
                            Packet.sound().play(Sounds.UI_TOAST_OUT, user);
                            if(returnMenu instanceof FavoritesMenu favoritesMenu) {
                                favoritesMenu.refresh();
                            } else {
                                 returnMenu.reopen();
                            }
                        })
        );

        return content;
    }

}
