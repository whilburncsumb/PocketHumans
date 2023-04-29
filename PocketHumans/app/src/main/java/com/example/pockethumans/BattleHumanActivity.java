package com.example.pockethumans;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.pockethumans.DB.AppDatabase;
import com.example.pockethumans.DB.HumanDAO;
import com.example.pockethumans.DB.MovesDAO;
import com.example.pockethumans.DB.UserLoginDAO;
import com.example.pockethumans.databinding.ActivityBattleBinding;

public class BattleHumanActivity extends AppCompatActivity {
    ActivityBattleBinding mBattleHumanBinding;
    boolean validSelection = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        UserLoginDAO mUsersDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .UserLoginDAO();
        MovesDAO mMovesDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .MovesDAO();
        HumanDAO mHumanDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .HumanDAO();
        User currentUser = mUsersDAO.checkUserByID(getIntent().getIntExtra("USERID",0));
        mBattleHumanBinding = ActivityBattleBinding.inflate(getLayoutInflater());
        setContentView(mBattleHumanBinding.getRoot());

        Spinner mSelectYourHumanSpinner = mBattleHumanBinding.selectYourHumanSpinner;
        Spinner mSelectOpponentSpinner = mBattleHumanBinding.selectOpponentSpinner;
        Button mBeginBattleButton= mBattleHumanBinding.beginBattleButton;
        TextView mUnlockFeedback = mBattleHumanBinding.selectionFeedback1;
        ViewFlipper flipper = mBattleHumanBinding.viewFlipper;
        ObjectAnimator animator = ObjectAnimator.ofFloat(mUnlockFeedback,"rotation",-5,5);
        animator.setDuration(250);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(2);

        //populate the list of humans
        ArrayAdapter<Human> adapterH = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, mHumanDAO.listHumans());
        adapterH.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSelectYourHumanSpinner.setAdapter(adapterH);
        mSelectOpponentSpinner.setAdapter(adapterH);

        mSelectYourHumanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Human selection = (Human) parent.getItemAtPosition(position);
                if(selection.getHumanId()==6){
                    //if Dr.C, check that user has unlocked him
                    validSelection = false;
                    mUnlockFeedback.setVisibility(View.VISIBLE);
                    animator.start();
                } else {
                    validSelection = true;
                    mUnlockFeedback.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mBeginBattleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flipper.showNext();
                //begin the battle
                //transition other elements to be invisible
            }
        });
    }
    public static Intent getIntent(Context context, int userId){
        Intent intent = new Intent(context, BattleHumanActivity.class);
        intent.putExtra("USERID", userId);
        return intent;
    }
}