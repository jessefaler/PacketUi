package com.protoxon.promenu.utils;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import com.protoxon.promenu.types.ButtonType;
import com.protoxon.promenu.types.ClickData;
import com.protoxon.promenu.types.ClickType;

public class ClickDataMapper {

    public static ClickData getClickData(WrapperPlayClientClickWindow packet, int slot, User user) {
        WrapperPlayClientClickWindow.WindowClickType type = packet.getWindowClickType();
        ItemStack carriedItem = packet.getCarriedItemStack();

        switch (type) {
            case PICKUP: {
                boolean isCarriedItemExist = !carriedItem.equals(ItemStack.EMPTY) && carriedItem.getType() != ItemTypes.AIR;

                if (packet.getButton() == 0) {
                    return new ClickData(ButtonType.LEFT, isCarriedItemExist ? ClickType.PICKUP : ClickType.PLACE, slot, carriedItem, user);
                } else {
                    return new ClickData(ButtonType.RIGHT, isCarriedItemExist ? ClickType.PLACE : ClickType.PICKUP, slot, carriedItem, user);
                }
            }

            case QUICK_MOVE: {
                return new ClickData(
                        packet.getButton() == 0 ? ButtonType.SHIFT_LEFT : ButtonType.SHIFT_RIGHT,
                        ClickType.SHIFT_CLICK,
                        slot,
                        carriedItem,
                        user
                );
            }

            case SWAP: {
                int button = packet.getButton();
                if (button >= 0 && button <= 8) {
                    return new ClickData(ButtonType.values()[9 + button], ClickType.PICKUP, slot, carriedItem, user);
                } else if (button == 40) {
                    return new ClickData(ButtonType.F, ClickType.PICKUP, slot, carriedItem, user);
                } else {
                    return new ClickData(ButtonType.LEFT, ClickType.PLACE, slot, carriedItem, user);
                }
            }

            case CLONE: {
                return new ClickData(ButtonType.MIDDLE, ClickType.PICKUP, slot, carriedItem, user);
            }

            case THROW: {
                return new ClickData(
                        packet.getButton() == 0 ? ButtonType.DROP : ButtonType.CTRL_DROP,
                        ClickType.PICKUP,
                        slot,
                        carriedItem,
                        user
                );
            }

            case QUICK_CRAFT: {
                switch (packet.getButton()) {
                    case 0: return new ClickData(ButtonType.LEFT, ClickType.DRAG_START, slot, carriedItem, user);
                    case 4: return new ClickData(ButtonType.RIGHT, ClickType.DRAG_START, slot, carriedItem, user);
                    case 8: return new ClickData(ButtonType.MIDDLE, ClickType.DRAG_START, slot, carriedItem, user);
                    case 1: return new ClickData(ButtonType.LEFT, ClickType.DRAG_ADD, slot, carriedItem, user);
                    case 5: return new ClickData(ButtonType.RIGHT, ClickType.DRAG_ADD, slot, carriedItem, user);
                    case 9: return new ClickData(ButtonType.MIDDLE, ClickType.DRAG_ADD, slot, carriedItem, user);
                    case 2: return new ClickData(ButtonType.LEFT, ClickType.DRAG_END, slot, carriedItem, user);
                    case 6: return new ClickData(ButtonType.RIGHT, ClickType.DRAG_END, slot, carriedItem, user);
                    case 10: return new ClickData(ButtonType.MIDDLE, ClickType.DRAG_END, slot, carriedItem, user);
                    default: return new ClickData(ButtonType.LEFT, ClickType.UNDEFINED, slot, carriedItem, user);
                }
            }

            case PICKUP_ALL: {
                return new ClickData(ButtonType.DOUBLE_CLICK, ClickType.PICKUP_ALL, slot, carriedItem, user);
            }

            default: {
                return new ClickData(ButtonType.LEFT, ClickType.UNDEFINED, slot, carriedItem, user);
            }
        }
    }
}
