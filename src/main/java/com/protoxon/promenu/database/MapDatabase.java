package com.protoxon.promenu.database;

import com.protoxon.promenu.map.Map;
import com.protoxon.promenu.map.MapType;
import com.protoxon.promenu.menus.search.tags.SearchTag;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapDatabase {

    private final String databasePath;

    public MapDatabase(String databasePath) {
        Database.ensureFolderExists();
        Database.createDatabaseFile();
        this.databasePath = databasePath;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        createMapsTable();
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

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("[ProMenu] Failed to create maps table", e);
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

        try (Connection conn = getConnection();
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

    public List<Map> getMapsByType(MapType mapType) {
        String sql = "SELECT name, authors, keywords, tag, playcount FROM maps WHERE map_type = ?";
        List<Map> maps = new ArrayList<>();

        try (Connection conn = getConnection();
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
                    map.setSearchScore(0); // Optional
                    maps.add(map);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load maps of type " + mapType.name(), e);
        }

        return maps;
    }

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(databasePath);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA busy_timeout = 5000;");
        }
        return conn;
    }
}