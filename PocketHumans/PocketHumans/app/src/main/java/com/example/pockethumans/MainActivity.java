package com.example.pockethumans;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pockethumans.databinding.ActivityMainBinding;
import com.example.pockethumans.DB.UserLoginDAO;
import com.example.pockethumans.DB.AppDatabase;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText mLoginUsername;
    EditText mLoginPassword;
    Button mLoginButton;
    TextView mLoginFeedback;
    Button mLoginRegister;

    ActivityMainBinding mMainBinding;
    UserLoginDAO mUsersDAO;
    User testuser1 = new User("testuser1","testuser1",false);
    User admin2 = new User("admin2","admin2",true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mMainBinding.getRoot());

        mLoginUsername=mMainBinding.loginUsernameEntry;
        mLoginPassword=mMainBinding.loginPasswordEntry;
        mLoginButton=mMainBinding.loginButton;
        mLoginFeedback=mMainBinding.loginFeedback;
        mLoginRegister=mMainBinding.registerButton;

        //create database if it doesn't exist
        mUsersDAO = Room.databaseBuilder(this, AppDatabase.class,AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .UserLoginDAO();

        //Add predefined users if they dont exist
        if(mUsersDAO.checkLogin(testuser1.getUsername(),testuser1.getPassword())==null){
            mUsersDAO.insert(testuser1);
            mUsersDAO.insert(admin2);
        }

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if login found
                User user = (mUsersDAO.checkLogin(mLoginUsername.getText().toString(),mLoginPassword.getText().toString()));
                if(user!=null){
//                    mLoginFeedback.setText("Welcome, user!");
                    Intent intent = LandingActivity.getIntent(getApplicationContext(),user);
                    startActivity(intent);
                } else {//if login not found
                    mLoginFeedback.setText(R.string.login_fail_feedback);//user not found feedback
                }
            }
        });
    }

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}