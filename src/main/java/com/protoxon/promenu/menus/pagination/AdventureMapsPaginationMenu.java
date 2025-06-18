package com.protoxon.promenu.menus.pagination;

import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.protoxon.promenu.Item;
import com.protoxon.promenu.menus.SelectionMenu;
import com.protoxon.promenu.service.Content;
import com.protoxon.promenu.service.MenuService;
import com.protoxon.promenu.types.InventoryType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.List;

public class AdventureMapsPaginationMenu extends PaginationMenu {
    private static final Component TITLE = Component.text("Minigames");
    private static final InventoryType INVENTORY_TYPE = InventoryType.GENERIC9X6;

    public AdventureMapsPaginationMenu(User user) {
        super(TITLE, INVENTORY_TYPE, user);
    }

    @Override
    public void onOpen() {
        super.onOpen();
    }

    @Override
    public List<Component> categories() {
        return List.of();
    }

    public Content bodyContent() {

        Content content = new Content(INVENTORY_TYPE);

        content.add(
                Item.slot(1, 7),
                Item.builder()
                        .setType(ItemTypes.BOOKSHELF)
                        .setName(Component.text("Block Hunt").color(NamedTextColor.RED))
                        .setLore(List.of(Component.text("Coming Soon!").color(NamedTextColor.DARK_RED)))
                        .setClickAction(clickData -> {
                            clickData.user.sendMessage(Component.text("Pvp coming soon!").color(NamedTextColor.GOLD));
                        })
        );

        return content;
    }
}
