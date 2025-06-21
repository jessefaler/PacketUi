package com.protoxon.promenu.menus;

public enum SortType {
    FEATURED,
    PLAY_COUNT,
    ACTIVE_GAMES;

    // Cycle to the next state
    public SortType next() {
        return values()[(this.ordinal() + 1) % values().length];
    }
}
