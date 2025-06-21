package com.protoxon.promenu.service;

import com.github.retrooper.packetevents.protocol.player.User;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MenuService {

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private static final ConcurrentHashMap<User, Menu> viewers = new ConcurrentHashMap<>();

    public static ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    public static Menu getMenu(User user) {
        return viewers.get(user);
    }

    public static void removeMenu(User user) {
        viewers.remove(user);
    }

    public static Menu addMenu(User user, Menu menu) {
        return viewers.put(user, menu);
    }

    public static int getWindowId(User user) {
        return getMenu(user).windowId;
    }

}
