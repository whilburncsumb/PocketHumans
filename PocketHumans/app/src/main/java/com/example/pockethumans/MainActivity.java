package com.example.pockethumans;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.pockethumans.DB.AppDatabase;
import com.example.pockethumans.DB.HumanDAO;
import com.example.pockethumans.DB.MovesDAO;
import com.example.pockethumans.DB.UserLoginDAO;
import com.example.pockethumans.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText mLoginUsername;
    EditText mLoginPassword;
    Button mLoginButton;
    TextView mLoginFeedback;
    Button mLoginRegister;

    ActivityMainBinding mMainBinding;
    UserLoginDAO mUsersDAO;
    MovesDAO mMovesDAO;
    HumanDAO mHumanDAO;
    User testuser1 = new User("testuser1", "testuser1", false);
    User admin2 = new User("admin2", "admin2", true);
    User a = new User("a", "a", true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mMainBinding.getRoot());

        mLoginUsername = mMainBinding.loginUsernameEntry;
        mLoginPassword = mMainBinding.loginPasswordEntry;
        mLoginButton = mMainBinding.loginButton;
        mLoginFeedback = mMainBinding.loginFeedback;
        mLoginRegister = mMainBinding.registerButton;

        //create database if it doesn't exist
        mUsersDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .UserLoginDAO();
        mMovesDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .MovesDAO();
        mHumanDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .HumanDAO();

        //build the database, including the predefined users, moves, and humans if they don't exist
        List<Move> premadeMoves = new ArrayList<>();
        premadeMoves.add(new Move("Tackle", "User tackles the opponent, damaging them.", 25, "attack"));//1
        premadeMoves.add(new Move("Headbutt", "User headbutts the opponent with abandon, damaging them.", 30, "attack"));//2
        premadeMoves.add(new Move("Mega Punch", "User winds up for a big punch and lets loose on the opponent, damaging them.", 30, "attack"));//3
        premadeMoves.add(new Move("Speedy Kick", "User swiftly roundhouse kicks opponent, damaging them.", 30, "attack"));//4
        premadeMoves.add(new Move("Divide by zero", "User divides by zero, causing a shockwave of negative energy that damages opponent.", 35, "attack"));//5
        premadeMoves.add(new Move("Grade Papers", "User returns a terrible grade for the opponents work, damaging them brutally.", 40, "attack"));//6
        premadeMoves.add(new Move("Quiet Riot", "User shushes opponent before slamming them with a heavy encyclopedia, damaging them.", 35, "attack"));//7
        premadeMoves.add(new Move("Stapler Slam", "User wields a stapler as one would a nun-chuck, damaging the opponent.", 35, "attack"));//8

        premadeMoves.add(new Move("Mental Health Break", "User takes a little break to restore their health.", 30, "heal"));//9
        premadeMoves.add(new Move("Apply Bandage", "User applies some bandages to their boo-boos, restoring their health.", 30, "heal"));//10
        premadeMoves.add(new Move("Eat Baby Carrots", "User eats some baby carrots, restoring a little health.", 20, "heal"));//11

        premadeMoves.add(new Move("Tickle", "User tickles the opponent a little, lowering their attack. Hehe!", 0.75, "statusAttack"));//12
        premadeMoves.add(new Move("Strategy Pattern", "User employs the strategy design pattern, increasing their attack.", 1.5, "statusAttack"));//13
        premadeMoves.add(new Move("Leg Sweep", "User sweeps the leg, tripping opponent and lowering their defense.", .75, "statusDefense"));//14
        premadeMoves.add(new Move("Study", "User studies their opponents moves, increasing their defense.", 1.5, "statusDefense"));//15
        premadeMoves.add(new Move("Aerobics", "User does some quick aerobics, invigorating themselves and increasing their speed.", 2, "statusSpeed"));//16
        premadeMoves.add(new Move("Roller Blaze", "User accelerates on their roller skates, increasing their speed.", 2, "statusSpeed"));//17
        premadeMoves.add(new Move("Java Homework", "User assigns opponent a very difficult java homework assignment, halving their speed.", .5, "statusSpeed"));//18
        premadeMoves.add(new Move("Desk Jump", "User jumps on top of a desk in preparation for next attack. Raises their attack stat.", 1.5, "statusAttack"));//19

        List<Human> premadeHumans = new ArrayList<>();
        premadeHumans.add(new Human("William","A mustached student at CSUMB. It's said he loves his boyfriend, his cat, and the color yellow. He's known for his incredible wit and humility.",
                30,30,30,3,11,12,15));
        premadeHumans.add(new Human("Ben","A handsome software developer at Apple. William's dashing boyfriend. Gregarious and funny, he loves conversation. And also pickles, apparently.",
                25,40,25,5,10,12,14));
        premadeHumans.add(new Human("Sally","A retired roller derby star who's looking to make a comeback. She uses roller skates to zip around, attacking rapidly. Has a Barbie collection she keeps secret.",
                30,19,41,4,10,14,17));
        premadeHumans.add(new Human("Gretchen","A mild mannered librarian who enrolled in a self defense course. Despite her reluctance to fight, she's not shy about slamming her opponents with very heavy books.",
                30,40,20,7,9,15,19));
        premadeHumans.add(new Human("Bob","A middle aged office worker who became a Pocket Human after accidentally spilling coffee on a coworker's suit. Fights with a variety of devastating office supplies.",
                40,30,20,8,1,10,19));

        premadeHumans.add(new Human("Dr. C","A wise and illustrious professor of Computer Science at CSUMB. Fights using various design patterns and unimpeachable coding skills. Has stats beyond a typical human's.",
                40,40,40,6,11,18,13));

        //insert moves that are missing
        for (Move i : premadeMoves){
//            System.out.println(i.getName());
            if (mMovesDAO.getMove(i.getName()) == null) {
                mMovesDAO.insert(i);
            }
        }
        //insert humans that are missing
        for (Human i : premadeHumans){
//            System.out.println(i.getName());
            if (mHumanDAO.getHuman(i.getName()) == null) {
                mHumanDAO.insert(i);
            }
        }
        //Add predefined users if they don't exist
        if (mUsersDAO.checkLogin(testuser1.getUsername(), testuser1.getPassword()) == null) {
            mUsersDAO.insert(testuser1);
            mUsersDAO.insert(admin2);
            mUsersDAO.insert(a);
        }

        mLoginButton.setOnClickListener(v -> {
            //if login found
            User user = (mUsersDAO.checkLogin(mLoginUsername.getText().toString(), mLoginPassword.getText().toString()));
            if (user != null) {
//                    mLoginFeedback.setText("Welcome, user!");
                Intent intent = LandingActivity.getIntent(getApplicationContext(), user.getUserId());
                startActivity(intent);
            } else {//if login not found
                mLoginFeedback.setText(R.string.login_fail_feedback);//user not found feedback
            }
        });

        mLoginRegister.setOnClickListener(v -> {
            Intent intent = RegisterActivity.getIntent(getApplicationContext());
            startActivity(intent);
        });
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}