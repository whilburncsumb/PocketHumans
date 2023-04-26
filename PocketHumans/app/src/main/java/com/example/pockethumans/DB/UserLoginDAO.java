package com.example.pockethumans.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import com.example.pockethumans.User;
@Dao
public interface UserLoginDAO {

    @Insert
    void insert(User... users);

    @Update
    void update(User... users);

    @Delete
    void delete(User... users);

    @Query("DELETE FROM " + AppDatabase.USER_TABLE + " WHERE userId = :userId")
    void deleteById(int userId);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " ORDER BY username desc")
    List<User> getUsers();

    @Query("SELECT username FROM " + AppDatabase.USER_TABLE + " ORDER BY username desc")
    List<String> getUsernames();

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE username = :username AND password = :password")
    User checkLogin(String username, String password);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE userId = :userId")
    User checkUserByID(int userId);
}
