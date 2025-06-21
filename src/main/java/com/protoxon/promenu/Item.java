package com.protoxon.promenu;

import com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemLore;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemProfile;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.Dummy;
import com.protoxon.promenu.types.ClickData;
import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.nbt.tag.Tag;
import io.github.retrooper.packetevents.adventure.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.Component;
import org.apache.lucene.queryparser.flexible.standard.builders.DummyQueryNodeBuilder;

import java.util.List;
import java.util.function.Consumer;

public class Item {

    private final ItemStack.Builder builder;
    private Component name;
    private ItemLore lore;
    private Boolean glint;
    private boolean hideToolTip;
    private ItemType itemType;
    private int amount = 1;
    private ClientVersion version;
    private Consumer<ClickData> clickAction;
    private ItemProfile headProfile;

    public Item() {
        this.builder = ItemStack.builder().type(ItemTypes.AIR);
    }

    public static Item builder() {
        return new Item();
    }

    public Item(ItemType itemType) {
        this.builder = ItemStack.builder().type(itemType);
    }

    public Item setType(ItemType itemType) {
        builder.type(itemType);
        return this;
    }

    public Item setName(Component name) {
        this.name = name;
        return this;
    }

    public Item setLore(List<Component> lines) {
        this.lore = new ItemLore(lines);
        return this;
    }

    public Item setEnchantmentGlint(boolean value) {
        this.glint = value;
        return this;
    }

    public Item setHideTooltip(boolean value) {
        this.hideToolTip = value;
        return this;
    }

    public Item setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public Item setClickAction(Consumer<ClickData> action) {
        this.clickAction = action;
        return this;
    }

    public void onClick(ClickData clickData) {
        if(clickAction == null) return;
        clickAction.accept(clickData);
    }

    public Item setHeadTexture(String base64Texture) {
        this.headProfile = new ItemProfile(
                null,
                null,
                List.of(new ItemProfile.Property("textures", base64Texture, null))
        );
        return this;
    }

    /**
     * Converts a 1-based row and column to a slot index.
     * This is based on a standard 9-column chest inventory.
     *
     * @param row the row number (1-based)
     * @param column the column number (1-based)
     * @return the slot index in the inventory
     * @throws IllegalArgumentException if row or column is out of bounds
     */
    public static int slot(int row, int column) {
        if (row < 1 || column < 1 || column > 9) {
            throw new IllegalArgumentException("Invalid row or column: row=" + row + ", column=" + column);
        }
        return (row - 1) * 9 + (column - 1);
    }

    public ItemStack build() {
        ItemStack item = builder.build();
        item.setAmount(amount);
        if (name != null) item.setComponent(ComponentTypes.CUSTOM_NAME, name);
        if (lore != null) item.setComponent(ComponentTypes.LORE, lore);
        if (glint != null) item.setComponent(ComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, glint);
        if (headProfile != null) item.setComponent(ComponentTypes.PROFILE, headProfile);
        //if(hideToolTip) item.setComponent(ComponentTypes.HIDE_TOOLTIP, Dummy.DUMMY);

        if (name == null) return item;
        NBTCompound display = new NBTCompound();
        String jsonName = GsonComponentSerializer.gson().serialize(name);
        display.setTag("Name", new NBTString(jsonName));

        NBTCompound rootTag = new NBTCompound();
        rootTag.setTag("display", display);

        item.setNBT(rootTag);
        return item;
    }
}
