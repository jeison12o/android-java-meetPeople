package com.example.yourhistory.model;

public class UserEntered {
    private static User userEntered;

    public static User getUserEntered() {
        return userEntered;
    }

    public static void setUserEntered(User userEntered) {
        UserEntered.userEntered = userEntered;
    }
}
