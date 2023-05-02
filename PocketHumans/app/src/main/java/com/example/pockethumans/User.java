package com.example.pockethumans;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.example.pockethumans.DB.AppDatabase;
@Entity(tableName = AppDatabase.USER_TABLE)
public class User {
    @PrimaryKey(autoGenerate = true)
    private int userId;
    private String username;
    private String password;
    private boolean isAdmin;
    private boolean unlockedDrC;

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.unlockedDrC = isAdmin;//admins can automatically use Dr.C in battle
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }

    public boolean isUnlockedDrC() {
        return unlockedDrC;
    }

    public void setUnlockedDrC(boolean unlockedDrC) {
        this.unlockedDrC = unlockedDrC;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
