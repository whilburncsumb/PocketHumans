package com.example.pockethumans.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.pockethumans.User;
import com.example.pockethumans.Move;
import com.example.pockethumans.Human;

@Database(entities = {User.class, Move.class, Human.class},version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "Users.db";
    public static final String USER_TABLE = "users_table";
    public static final String HUMAN_TABLE = "humans_table";
    public static final String MOVE_TABLE = "moves_table";
    private static volatile AppDatabase instance;
    private static final Object LOCK = new Object();

    public abstract UserLoginDAO UserLoginDAO();
    public abstract MovesDAO MovesDAO();
    public abstract HumanDAO HumanDAO();

    public static AppDatabase getInstance(Context context){
        if(instance == null){
            synchronized (LOCK){
                if(instance==null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }
}
