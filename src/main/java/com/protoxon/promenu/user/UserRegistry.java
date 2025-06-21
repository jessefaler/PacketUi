package com.protoxon.promenu.user;

import com.github.retrooper.packetevents.protocol.player.User;

import java.util.HashMap;

public class UserRegistry {

    private static final HashMap<User, UserData> usersData = new HashMap<>();

    public static UserData getUserData(User user) {
        UserData data = usersData.get(user);
        if(data == null) {
            usersData.put(user, new UserData(user));
            return usersData.get(user);
        }
        return data;
    }

}
