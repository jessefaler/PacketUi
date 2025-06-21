package com.protoxon.promenu.map;

import com.protoxon.promenu.menus.search.tags.SearchTag;

import java.util.List;
import java.util.stream.Stream;

public class Map {

    private final String name;
    private final SearchTag searchTag;
    private final List<String> keywords;
    private final String authors;
    private float searchScore;
    private int playcount;
    private int activeGames;
    private final MapType mapType;

    // Search priority
    // name -> keywords -> authors -> tag
    public Map(String name, String authors, SearchTag searchTag, List<String> keywords, int playcount, MapType mapType) {
        this.name = name;
        this.searchTag = searchTag;
        this.keywords = keywords;
        this.authors = authors;
        this.playcount = playcount;
        this.mapType = mapType;
    }

    public String getName() {
        return name;
    }

    public void setSearchScore(float score) {
        this.searchScore = score;
    }

    public float getSearchScore() {
        return searchScore;
    }

    public String getAuthors() {
        return authors;
    }

    public MapType getMapType() {
        return mapType;
    }

    public SearchTag getSearchTag() {
        return searchTag;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public List<String> getTagKeys() {
        return searchTag.getSynonyms();
    }

    public int getPlaycount() {
        return playcount;
    }

    public int getActiveGames() {
        return activeGames;
    }

}
