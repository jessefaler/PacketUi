package com.protoxon.promenu.menus.search;

import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.sound.Sounds;
import com.protoxon.promenu.Item;
import com.protoxon.promenu.map.Map;
import com.protoxon.promenu.map.MapType;
import com.protoxon.promenu.packets.Packet;
import com.protoxon.promenu.service.Content;
import com.protoxon.promenu.service.Menu;
import com.protoxon.promenu.types.InventoryType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;

import java.util.List;

public class SearchMenu extends Menu {

    private Menu returnMenu;
    private String input;
    private static final Component TITLE = Component.text("Search");
    private static final InventoryType INVENTORY_TYPE = InventoryType.ANVIL;
    private MapType mapType;

    public SearchMenu(User user, Menu returnMenu, MapType mapType) {
        super(TITLE, INVENTORY_TYPE, user);
        this.returnMenu = returnMenu;
        this.mapType = mapType;
    }

    @Override
    public void onOpen() {
        setContent(pageContent());
        update();
    }

    @Override
    public void onClose() {
        Packet.sound().play(Sounds.UI_TOAST_OUT, user);
        returnMenu.reopen();
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Content pageContent() {

        Content content = new Content(INVENTORY_TYPE);

        content.add(
                0,
                Item.builder()
                        .setType(ItemTypes.NAME_TAG)
                        .setName(Component.text("").color(NamedTextColor.GOLD))
                        .setClickAction(clickData -> {
                        })
        );

        content.add(
                2,
                Item.builder()
                        .setClickAction(clickData -> {
                            Packet.sound().play(Sounds.ENTITY_ITEM_FRAME_ROTATE_ITEM, clickData.user);
                            Packet.sound().play(Sounds.UI_TOAST_IN, clickData.user);

                            try {
                                List<Map> results = MapSearchIndex.search(input, mapType);
                                if(results.isEmpty()) {
                                    new SearchResults(clickData.user, Component.text()
                                            .append(Component.text("No Matches Found").color(NamedTextColor.DARK_RED).decorate(TextDecoration.UNDERLINED)).build(), returnMenu, results, input, mapType).open();
                                    return;
                                }
                                new SearchResults(clickData.user, Component.text()
                                        .append(Component.text("Search:").decorate(TextDecoration.UNDERLINED).color(NamedTextColor.BLUE))
                                        .append(Component.text(" " + input)).build(), returnMenu, results, input, mapType).open();
                            } catch (Exception e) {
                                e.printStackTrace();
                                clickData.user.sendMessage(Component.text("Search failed.").color(NamedTextColor.RED));
                            }

                        })
        );

        return content;
    }
}
