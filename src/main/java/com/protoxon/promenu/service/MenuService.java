package com.protoxon.promenu.service;

import com.github.retrooper.packetevents.protocol.player.User;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class MenuService {

    private static final ConcurrentHashMap<User, Menu> viewers = new ConcurrentHashMap<>();

    public static Menu getMenu(User user) {
        return viewers.get(user);
    }

    public static Menu addMenu(User user, Menu menu) {
        return viewers.put(user, menu);
    }

    public static int getWindowId(User user) {
        return getMenu(user).windowId;
    }

}
