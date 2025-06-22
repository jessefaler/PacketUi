package com.protoxon.promenu.database;

import java.io.File;
import java.io.IOException;

public class Database {

    private static final String path = "./plugins/promenu/";
    private static final String fileName = "MenuDatabase.db";
    public static final MapDatabase maps = new MapDatabase("jdbc:sqlite:" + path + fileName);
    public static final UsersDatabase players = new UsersDatabase("jdbc:sqlite:" + path + fileName); // Players Database API

    protected static void createDatabaseFile() {
        File databaseFile = new File(path + fileName);
        if (!databaseFile.exists()) {
            try {
                boolean created = databaseFile.createNewFile();
                if (created) {
                    System.out.println("[ProMenu] SQLite: Database file created. " + fileName);
                }
            } catch (IOException e) {
                System.err.println("[ProMenu] SQLite: Error creating the database file. " + fileName);
                System.err.println(e.getMessage());
            }
        }
    }

    protected static void ensureFolderExists() {
        File directory = new File(path);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("[ProMenu] Created plugin data folder at: " + path);
            } else {
                System.err.println("[ProMenu] Failed to create plugin data folder at: " + path);
            }
        }
    }

}
