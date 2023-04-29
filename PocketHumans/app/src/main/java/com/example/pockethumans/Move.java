package com.example.pockethumans;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.example.pockethumans.DB.AppDatabase;

@Entity(tableName = AppDatabase.MOVE_TABLE)
public class Move {
    @PrimaryKey(autoGenerate = true)
    private int moveId;
    private String name; //name of the attack
    private String description; //Describe the attack in plain language
    private double power; //for attack moves, this lists the attack modifier of the move, a value from 1-100.
    //for healing moves, it should be the value of HP restored
    // For status moves, this should be .5 or .75 for debuffing moves against an enemy,
    // or 1.5 or 2 for buffing moves for your own human
    private String type; //possible values for type:
    // "attack" for an attack move
    // "heal" for healing move
    // "statusAttack" for a move that buffs or debuffs attack stat
    // "statusDefense" for a move that buffs or debuffs defense stat
    // "statusSpeed" for a move that buffs or debuffs speed stat

    public Move(String name, String description, double power, String type) {
        this.name = name;
        this.description = description;
        this.power = power;
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getMoveId() {
        return moveId;
    }

    public void setMoveId(int moveId) {
        this.moveId = moveId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
