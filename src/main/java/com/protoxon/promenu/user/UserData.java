package com.protoxon.promenu.user;

import com.github.retrooper.packetevents.protocol.player.User;
import com.protoxon.promenu.map.Map;
import com.protoxon.promenu.map.MapType;

import java.util.ArrayList;
import java.util.List;

public class UserData {

    private final User user;
    private final ArrayList<Map> favorites = new ArrayList<>();

    public UserData(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public List<Map> getFavoriteMaps() {
        return favorites;
    }

    public List<Map> getFavoriteMaps(MapType mapType) {
        return favorites.stream()
                .filter(map -> map.getMapType() == mapType)
                .toList();
    }

    public boolean isMapFavorite(Map map) {
        return favorites.contains(map);
    }

    public void removeFavoriteMap(Map map) {
        favorites.remove(map);
    }

    public void addFavoriteMap(Map map) {
        favorites.add(map);
    }
}
