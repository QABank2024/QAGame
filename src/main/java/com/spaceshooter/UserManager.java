package com.spaceshooter;// com.spaceshooter.UserManager.java
import java.io.*;
import java.util.*;

public class UserManager {
    private static final String DATA_FILE = "users.dat";
    private Map<String, User> users;
    private User currentUser;

    public UserManager() {
        users = new HashMap<>();
        loadUsers();
    }

    public void createUser(String username) {
        if (!users.containsKey(username)) {
            users.put(username, new User(username));
            saveUsers();
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String username) {
        if (!users.containsKey(username)) {
            createUser(username);
        }
        currentUser = users.get(username);
    }

    public List<User> getLeaderboard() {
        List<User> leaderboard = new ArrayList<>(users.values());
        leaderboard.sort((u1, u2) -> {
            // Sort by prestige level first, then high score
            if (u1.getPrestigeLevel() != u2.getPrestigeLevel()) {
                return Integer.compare(u2.getPrestigeLevel(), u1.getPrestigeLevel());
            }
            return Integer.compare(u2.getHighScore(), u1.getHighScore());
        });
        return leaderboard;
    }

    @SuppressWarnings("unchecked")
    private void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            users = (Map<String, User>) ois.readObject();
        } catch (FileNotFoundException e) {
            users = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            users = new HashMap<>();
        }
    }

    public void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
