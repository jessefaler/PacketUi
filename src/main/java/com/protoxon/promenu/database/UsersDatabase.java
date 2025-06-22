package com.protoxon.promenu.database;

import com.github.retrooper.packetevents.protocol.player.User;
import com.protoxon.promenu.map.Map;
import com.protoxon.promenu.map.MapRegistry;
import com.protoxon.promenu.user.UserData;
import com.protoxon.promenu.user.UserRegistry;

import java.sql.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class UsersDatabase {

    private final String databasePath;

    public UsersDatabase(String databasePath) {
        Database.ensureFolderExists();
        Database.createDatabaseFile();
        this.databasePath = databasePath;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        createPlayerDataTable();
    }

    public void loadInFavoriteMaps(User user) {
        CompletableFuture.runAsync(() -> {
            List<String> favoriteMapNames = getFavoriteMapNames(user.getUUID().toString());
            if (favoriteMapNames.isEmpty()) return;

            UserData userData = UserRegistry.getUserData(user);
            for (String name : favoriteMapNames) {
                Map map = MapRegistry.getMap(name);
                if (map != null) {
                    userData.loadFavoriteMapFromDatabase(map);
                }
            }
        });
    }

    public void createPlayerDataTable() {
        String sql = """
        CREATE TABLE IF NOT EXISTS player_data (
            uuid TEXT PRIMARY KEY,
            favorite_maps TEXT,
            map_history TEXT
        );
    """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("[ProMenu] Failed to create player_data table", e);
        }
    }

    public void saveFavoriteMaps(String uuid, List<String> favoriteMaps) {
        CompletableFuture.runAsync(() -> {
            String sql = """
                INSERT INTO player_data (uuid, favorite_maps)
                VALUES (?, ?)
                ON CONFLICT(uuid) DO UPDATE SET favorite_maps = excluded.favorite_maps;
            """;

            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, uuid);
                stmt.setString(2, String.join(",", favoriteMaps));
                stmt.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException("[ProMenu] Failed to save favorite maps for player " + uuid, e);
            }
        });
    }

    public List<String> getFavoriteMapNames(String uuid) {
        String sql = "SELECT favorite_maps FROM player_data WHERE uuid = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, uuid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String mapList = rs.getString("favorite_maps");
                    if (mapList == null || mapList.isEmpty()) return List.of();
                    return Arrays.stream(mapList.split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .toList();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("[ProMenu] Failed to get favorite maps for player " + uuid, e);
        }

        return List.of();
    }

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(databasePath);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA busy_timeout = 5000;");
        }
        return conn;
    }

    public void saveMapHistory(String uuid, List<String> mapHistory) {
        CompletableFuture.runAsync(() -> {
            String sql = """
            INSERT INTO player_data (uuid, map_history)
            VALUES (?, ?)
            ON CONFLICT(uuid) DO UPDATE SET map_history = excluded.map_history;
        """;

            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, uuid);
                stmt.setString(2, String.join(",", mapHistory));
                stmt.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException("[ProMenu] Failed to save map history for player " + uuid, e);
            }
        });
    }

    public List<String> getMapHistory(String uuid) {
        String sql = "SELECT map_history FROM player_data WHERE uuid = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, uuid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String mapList = rs.getString("map_history");
                    if (mapList == null || mapList.isEmpty()) return List.of();
                    return Arrays.stream(mapList.split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .toList();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("[ProMenu] Failed to get map history for player " + uuid, e);
        }

        return List.of();
    }

    public void loadInMapHistory(User user) {
        CompletableFuture.runAsync(() -> {
            List<String> historyMapNames = getMapHistory(user.getUUID().toString());
            if (historyMapNames.isEmpty()) return;

            UserData userData = UserRegistry.getUserData(user);
            for (String name : historyMapNames) {
                Map map = MapRegistry.getMap(name);
                if (map != null) {
                    userData.loadMapHistoryFromDatabase(map);
                }
            }
        });
    }
}
