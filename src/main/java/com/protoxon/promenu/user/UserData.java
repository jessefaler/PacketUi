package com.protoxon.promenu.user;

import com.github.retrooper.packetevents.protocol.player.User;
import com.protoxon.promenu.database.Database;
import com.protoxon.promenu.map.Map;
import com.protoxon.promenu.map.MapType;

import java.util.ArrayList;
import java.util.List;

public class UserData {

    private final User user;
    private final ArrayList<Map> favorites = new ArrayList<>();
    private final ArrayList<Map> history = new ArrayList<>();
    boolean modifiedFavorites;

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

    public List<Map> getMapHistory(MapType mapType) {
        return history.stream()
                .filter(map -> map.getMapType() == mapType)
                .toList();
    }

    public boolean isMapFavorite(Map map) {
        return favorites.contains(map);
    }

    public void removeFavoriteMap(Map map) {
        favorites.remove(map);
        modifiedFavorites = true;
    }

    public void addFavoriteMap(Map map) {
        favorites.add(map);
        modifiedFavorites = true;
    }

    public void addMapToHistory(Map map) {
        history.add(map);
        // Save Map History To User Database
        List<String> mapNameHistory = history.stream().map(Map::getName).toList();
        Database.players.saveMapHistory(user.getUUID().toString(), mapNameHistory);
    }

    public void loadFavoriteMapFromDatabase(Map map) {
        favorites.add(map);
    }

    public void loadMapHistoryFromDatabase(Map map) {
        history.add(map);
    }

    public void saveFavoritesToDatabase() {
        if(modifiedFavorites) {
            List<String> favoriteMapNames = favorites.stream().map(Map::getName).toList();
            Database.players.saveFavoriteMaps(user.getUUID().toString(), favoriteMapNames);
            modifiedFavorites = false;
        }
    }
}
