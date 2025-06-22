package com.protoxon.promenu.map;

import com.protoxon.promenu.menus.SortType;
import com.protoxon.promenu.menus.search.tags.SearchTag;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MapRegistry {

    private static ArrayList<Map> minigameMaps = new ArrayList<>();
    private static ArrayList<Map> adventureMaps = new ArrayList<>();

    public static Map getMap(MapType mapType, String name) {
        ArrayList<Map> maps = getMaps(mapType);
        for (Map map : maps) {
            if (map.getName().equalsIgnoreCase(name)) {
                return map;
            }
        }
        return null;
    }

    public static List<Map> getAllMaps() {
        List<Map> allMaps = new ArrayList<>();
        allMaps.addAll(minigameMaps);
        allMaps.addAll(adventureMaps);
        return allMaps;
    }

    public static void addMap(MapType mapType, Map map) {
        getMaps(mapType).add(map);
    }

    public static void addMaps(MapType mapType, List<Map> mapsToAdd) {
        ArrayList<Map> maps = getMaps(mapType);
        maps.addAll(mapsToAdd);
    }

    public static List<Map> getMapsInRange(MapType mapType, int start, int end) {
        ArrayList<Map> maps = getMaps(mapType);
        // Ensure start and end are within bounds
        int size = maps.size();
        if (start < 0) start = 0;
        if (end > size) end = size;
        if (start > end) return new ArrayList<>();

        return new ArrayList<>(maps.subList(start, end));
    }

    public static List<Map> getMapsInRange(MapType mapType, int start, int end, SortType sortType) {
        ArrayList<Map> maps = getMaps(mapType); // Get the base maps list

        // Sort based on sortType
        switch (sortType) {
            case PLAY_COUNT -> maps.sort(Comparator.comparingInt(Map::getPlaycount).reversed());
            case ACTIVE_GAMES -> maps.sort(Comparator.comparingInt(Map::getActiveGames).reversed());
            case FEATURED -> {
                // No sorting, keep base order
            }
        }

        // Ensure start and end are within bounds
        int size = maps.size();
        if (start < 0) start = 0;
        if (end > size) end = size;
        if (start > end) return new ArrayList<>();

        return new ArrayList<>(maps.subList(start, end));
    }

    public static ArrayList<Map> getMaps(MapType mapType) {
        ArrayList<Map> maps = null;
        if(mapType == MapType.MINIGAMES) {
            maps = minigameMaps;
        } else if (mapType == MapType.ADVENTURE) {
            maps = adventureMaps;
        } else {
            throw new IllegalArgumentException("Invalid map type: " + mapType);
        }
        return maps;
    }

    public static ArrayList<Map> getMaps(MapType mapType, SortType sortType) {
        List<Map> baseList;

        if (mapType == MapType.MINIGAMES) {
            baseList = minigameMaps;
        } else if (mapType == MapType.ADVENTURE) {
            baseList = adventureMaps;
        } else {
            throw new IllegalArgumentException("Invalid map type: " + mapType);
        }

        // Create a copy so original list isn't mutated
        ArrayList<Map> sortedList = new ArrayList<>(baseList);

        switch (sortType) {
            case PLAY_COUNT -> sortedList.sort(Comparator.comparingInt(Map::getPlaycount).reversed());
            case ACTIVE_GAMES -> sortedList.sort(Comparator.comparingInt(Map::getActiveGames).reversed());
            case FEATURED -> {
                // No sorting, return base order
            }
        }

        return sortedList;
    }

    public static Map getMap(String name) {
        for (Map map : minigameMaps) {
            if (map.getName().equalsIgnoreCase(name)) {
                return map;
            }
        }
        for (Map map : adventureMaps) {
            if (map.getName().equalsIgnoreCase(name)) {
                return map;
            }
        }
        return null;
    }
}
