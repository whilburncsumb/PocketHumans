package com.example.pockethumans;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.example.pockethumans.DB.AppDatabase;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = AppDatabase.HUMAN_TABLE)
public class Human {
    @PrimaryKey(autoGenerate = true)
    private int humanId;
    private String name; //name of the human
    private String description; //Description of the human, for flavor
    @Ignore
    private double hp = 100; //All humans start battle with 100 HP.
    private int attack; //attack stat. this is added to the moves' attack stat,
    // gets the opponent's defense subtracted, and removed from enemy hp
    private int defense; //used to block damage as described above
    private int speed; //determines which human moves first
    //temp modifiers for status moves, these do not get saved into the DB.
    // When a human uses a status move this is modified
    @Ignore
    private double attackModifier = 1;
    @Ignore
    private double defenseModifier = 1;
    @Ignore
    private double speedModifier = 1;
    private int move1id;
    private int move2id;
    private int move3id;
    private int move4id;


    public Human(String name, String description, int attack, int defense, int speed, int move1id, int move2id, int move3id, int move4id) {
        this.name = name;
        this.description = description;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.move1id = move1id;
        this.move2id = move2id;
        this.move3id = move3id;
        this.move4id = move4id;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getHumanId() {
        return humanId;
    }

    public void setHumanId(int humanId) {
        this.humanId = humanId;
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

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getSpeed() {
        return speed;
    }

    public double getModifiedAttack(){ return (double) attack*attackModifier; }

    public double getModifiedDefense(){ return (double) defense*defenseModifier; }

    public double getModifiedSpeed(){ return (double) speed*speedModifier; }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getAttackModifier() {
        return attackModifier;
    }

    public void setAttackModifier(double attackModifier) {
        this.attackModifier = attackModifier;
    }

    public double getDefenseModifier() {
        return defenseModifier;
    }

    public void setDefenseModifier(double defenseModifier) {
        this.defenseModifier = defenseModifier;
    }

    public double getSpeedModifier() {
        return speedModifier;
    }

    public void setSpeedModifier(double speedModifier) {
        this.speedModifier = speedModifier;
    }

    public int getMove1id() {
        return move1id;
    }

    public void setMove1id(int move1id) {
        this.move1id = move1id;
    }

    public int getMove2id() {
        return move2id;
    }

    public void setMove2id(int move2id) {
        this.move2id = move2id;
    }

    public int getMove3id() {
        return move3id;
    }

    public void setMove3id(int move3id) {
        this.move3id = move3id;
    }

    public int getMove4id() {
        return move4id;
    }

    public void setMove4id(int move4id) {
        this.move4id = move4id;
    }
}
