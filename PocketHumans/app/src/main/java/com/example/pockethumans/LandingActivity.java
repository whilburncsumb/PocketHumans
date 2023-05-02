package com.example.pockethumans;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pockethumans.DB.AppDatabase;
import com.example.pockethumans.DB.UserLoginDAO;
import com.example.pockethumans.databinding.ActivityLandingBinding;

public class LandingActivity extends AppCompatActivity {

    private static User currentUser;
    TextView mLandingTextview;
    Button mHumanBattleButton;
    Button mMakeHumanButton;
    Button mEditHumanButton;
    Button mLogoutButton;
    ActivityLandingBinding mLandingBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        UserLoginDAO mUsersDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .UserLoginDAO();

        mLandingBinding = ActivityLandingBinding.inflate(getLayoutInflater());
        setContentView(mLandingBinding.getRoot());
        currentUser = mUsersDAO.checkUserByID(getIntent().getIntExtra("USERID",0));

        mLandingTextview = mLandingBinding.landingTextview;
        mHumanBattleButton = mLandingBinding.battleHumanButton;
        mEditHumanButton =mLandingBinding.editHumanButton;
        mMakeHumanButton = mLandingBinding.makeHumanButton;
        mLogoutButton = mLandingBinding.landingLogoutButton;

        mLandingTextview.setText(getString(R.string.pocket_humans_landing_text) + "\nWelcome " + currentUser.getUsername());
        if(currentUser.isAdmin()){
            //Set admin only buttons to visible
            mEditHumanButton.setVisibility(View.VISIBLE);
        }

        mHumanBattleButton.setOnClickListener(view -> {
            Intent intent = BattleHumanActivity.getIntent(getApplicationContext(),currentUser.getUserId());
            startActivity(intent);
        });
        mEditHumanButton.setOnClickListener(v -> {
            Intent intent = EditHumanActivity.getIntent(getApplicationContext(),currentUser.getUserId());
            startActivity(intent);
        });

        mLogoutButton.setOnClickListener(v -> {
                Intent intent = MainActivity.getIntent(getApplicationContext());
                startActivity(intent);
        });
    }

    public static Intent getIntent(Context context, int userId){
        Intent intent = new Intent(context, LandingActivity.class);
        intent.putExtra("USERID", userId);
        return intent;
    }
}
