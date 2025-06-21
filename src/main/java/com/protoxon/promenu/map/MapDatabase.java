package com.protoxon.promenu.map;

import com.protoxon.promenu.menus.search.tags.SearchTag;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapDatabase {

    Connection cconnection;
    public final String MAP_DATABASE = "MapDatabase.db";
    public final String PATH = "./plugins/promenu";

    public MapDatabase() {
        // JDBC PROTOCOL - DATABASE: SQLITE - PATH
        connect("jdbc:sqlite:" + PATH + "/" + MAP_DATABASE);
    }

    public void connect(String url) {
        try {
            ensureFolderExists();
            createDatabaseFile();
            Class.forName("org.sqlite.JDBC"); // Required to fix an issue with the jdbc driver not being present
            // See (https://stackoverflow.com/questions/16725377/unable-to-connect-to-database-no-suitable-driver-found_
            cconnection = DriverManager.getConnection(url);
            createMapsTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void createDatabaseFile() {
        File databaseFile = new File(PATH + "/" + MAP_DATABASE);
        if (!databaseFile.exists()) {
            try {
                boolean created = databaseFile.createNewFile();
                if (created) {
                    System.out.println("[ProMenu] SQLite: Database file created. " + MAP_DATABASE);
                }
            } catch (IOException e) {
                System.err.println("[ProMenu] SQLite: Error creating the database file. " + MAP_DATABASE);
                System.err.println(e.getMessage());
            }
        }
    }

    private void ensureFolderExists() {
        File directory = new File(PATH);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("[ProMenu] Created plugin data folder at: " + PATH);
            } else {
                System.err.println("[ProMenu] Failed to create plugin data folder at: " + PATH);
            }
        }
    }

    public void addMapToDatabase(Map map, MapType mapType) {
        String sql = """
        INSERT INTO maps (name, authors, keywords, tag, map_type, playcount)
        VALUES (?, ?, ?, ?, ?, ?)
        ON CONFLICT(name) DO UPDATE SET
            authors = excluded.authors,
            keywords = excluded.keywords,
            tag = excluded.tag,
            map_type = excluded.map_type,
            playcount = excluded.playcount
        """;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + PATH + "/" + MAP_DATABASE);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, map.getName());
            stmt.setString(2, map.getAuthors());
            stmt.setString(3, String.join(",", map.getKeywords()));
            stmt.setString(4, map.getSearchTag().name());
            stmt.setString(5, mapType.name());
            stmt.setInt(6, map.getPlaycount());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("[ProMenu] Failed to insert or update map into database", e);
        }
    }

    public void createMapsTable() {
        String sql = """
        CREATE TABLE IF NOT EXISTS maps (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL UNIQUE,
            authors TEXT,
            keywords TEXT,
            tag TEXT,
            map_type TEXT NOT NULL,
            playcount INTEGER DEFAULT 0
        );
        """;
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + PATH + "/" + MAP_DATABASE);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("[ProMenu] Failed to create maps table", e);
        }
    }

    public List<Map> getMapsByType(MapType mapType) {
        String sql = "SELECT name, authors, keywords, tag, playcount FROM maps WHERE map_type = ?";
        List<Map> maps = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + PATH + "/" + MAP_DATABASE);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mapType.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    String authors = rs.getString("authors");
                    List<String> keywords = Arrays.stream(rs.getString("keywords").split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .toList();
                    String tagName = rs.getString("tag");
                    int playcount = rs.getInt("playcount");

                    SearchTag searchTag = SearchTag.fromNameOrNone(tagName);

                    Map map = new Map(name, authors, searchTag, keywords, playcount, mapType);
                    map.setSearchScore(0); // or set as needed
                    maps.add(map);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load maps of type " + mapType.name(), e);
        }

        return maps;
    }
}
