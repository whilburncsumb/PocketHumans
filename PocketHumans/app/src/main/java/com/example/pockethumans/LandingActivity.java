package com.example.pockethumans;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pockethumans.databinding.ActivityLandingBinding;

public class LandingActivity extends AppCompatActivity {

    private static String currentUser;
    private static boolean isAdmin;
    private static int userId;

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

        mLandingBinding = ActivityLandingBinding.inflate(getLayoutInflater());
        setContentView(mLandingBinding.getRoot());

        currentUser = getIntent().getStringExtra("USERNAME");
        isAdmin = getIntent().getBooleanExtra("ISADMIN",false);
        mLandingTextview = mLandingBinding.landingTextview;
        mHumanBattleButton = mLandingBinding.battleHumanButton;
        mEditHumanButton =mLandingBinding.editHumanButton;
        mMakeHumanButton = mLandingBinding.makeHumanButton;
        mLogoutButton = mLandingBinding.landingLogoutButton;

        mLandingTextview.setText(getString(R.string.pocket_humans_landing_text) + "\nWelcome " + currentUser);
        if(isAdmin){
            //Set admin only buttons to visible
            mEditHumanButton.setVisibility(View.VISIBLE);
        }

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = MainActivity.getIntent(getApplicationContext());
                    startActivity(intent);
            }
        });
    }

    public static Intent getIntent(Context context, User user){
        Intent intent = new Intent(context, LandingActivity.class);
        intent.putExtra("USERID", user.getUserId());
        intent.putExtra("USERNAME", user.getUsername());
        intent.putExtra("ISADMIN", user.isAdmin());
        return intent;
    }
}
