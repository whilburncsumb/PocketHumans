package com.example.pockethumans.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pockethumans.Human;
import com.example.pockethumans.Move;

import java.util.List;

@Dao
public interface MovesDAO {

    @Insert
    void insert(Move... moves);

    @Update
    void update(Move... moves);

    @Delete
    void delete(Move... moves);

    @Query("SELECT * FROM " + AppDatabase.MOVE_TABLE + " WHERE moveId = :moveId")
    Move getMove(int moveId);

    @Query("SELECT * FROM " + AppDatabase.MOVE_TABLE + " WHERE name = :name")
    Move getMove(String name);

    @Query("SELECT * FROM " + AppDatabase.MOVE_TABLE + " ORDER BY moveId")
    List<Move> listMoves();
    
}
