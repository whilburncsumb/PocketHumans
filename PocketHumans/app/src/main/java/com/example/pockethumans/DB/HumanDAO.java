package com.example.pockethumans.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pockethumans.Human;
import com.example.pockethumans.User;

import java.util.List;

@Dao
public interface HumanDAO {

    @Insert
    void insert(Human... humans);

    @Update
    void update(Human... humans);

    @Delete
    void delete(Human... humans);

    @Query("SELECT * FROM " + AppDatabase.HUMAN_TABLE + " WHERE humanId = :humanId")
    Human getHuman(int humanId);

    @Query("SELECT * FROM " + AppDatabase.HUMAN_TABLE + " WHERE name = :name")
    Human getHuman(String name);

    @Query("SELECT * FROM " + AppDatabase.HUMAN_TABLE + " ORDER BY humanId")
    List<Human> listHumans();
    
}
