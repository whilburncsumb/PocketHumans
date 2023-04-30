package com.example.pockethumans;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.room.Room;

import com.example.pockethumans.DB.AppDatabase;
import com.example.pockethumans.DB.HumanDAO;
import com.example.pockethumans.DB.MovesDAO;
import com.example.pockethumans.DB.UserLoginDAO;
import com.example.pockethumans.databinding.ActivityBattleBinding;

import java.util.ArrayList;
import java.util.Random;

public class BattleHumanActivity extends AppCompatActivity {
    ActivityBattleBinding mBattleHumanBinding;
    boolean validSelection = true;
    Human playerHuman;
    Human cpuHuman;
    ArrayList<Move> moves = new ArrayList<>();
    Random random = new Random();

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

        ViewFlipper flipper = mBattleHumanBinding.viewFlipper;
        //bindings for selection screen view
        Spinner mSelectYourHumanSpinner = mBattleHumanBinding.selectYourHumanSpinner;
        Spinner mSelectOpponentSpinner = mBattleHumanBinding.selectOpponentSpinner;
        Button mBeginBattleButton= mBattleHumanBinding.beginBattleButton;
        TextView mUnlockFeedback = mBattleHumanBinding.selectionFeedback1;
        ObjectAnimator animator = ObjectAnimator.ofFloat(mUnlockFeedback,"rotation",-5,5);
        animator.setDuration(250);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(2);

        //bindings for battle screen view
        Button mSelectMove1 = mBattleHumanBinding.selectMove1Button;
        Button mSelectMove2 = mBattleHumanBinding.selectMove2Button;
        Button mSelectMove3 = mBattleHumanBinding.selectMove3Button;
        Button mSelectMove4 = mBattleHumanBinding.selectMove4Button;
        Button mSelectRetreat = mBattleHumanBinding.retreatButton;
//        ScrollView mBattleOutputScroll = mBattleHumanBinding.battleOutputScroll;
        TextView mBattleText = mBattleHumanBinding.battleOutput;
        mBattleText.setMovementMethod(new ScrollingMovementMethod());

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
                if(!validSelection){
                    animator.start();
                } else {
                    //begin the battle
                    //transition other elements to be invisible
                    playerHuman = (Human) mSelectYourHumanSpinner.getSelectedItem();
                    cpuHuman = (Human) mSelectOpponentSpinner.getSelectedItem();
                    flipper.showNext();
                    moves.add(mMovesDAO.getMove(playerHuman.getMove1id()));
                    moves.add(mMovesDAO.getMove(playerHuman.getMove2id()));
                    moves.add(mMovesDAO.getMove(playerHuman.getMove3id()));
                    moves.add(mMovesDAO.getMove(playerHuman.getMove4id()));
                    moves.add(mMovesDAO.getMove(cpuHuman.getMove1id()));
                    moves.add(mMovesDAO.getMove(cpuHuman.getMove2id()));
                    moves.add(mMovesDAO.getMove(cpuHuman.getMove3id()));
                    moves.add(mMovesDAO.getMove(cpuHuman.getMove4id()));

                    mSelectMove1.setText(moves.get(0).getName());
                    TooltipCompat.setTooltipText(mSelectMove1,moves.get(0).getDescription());
                    mSelectMove2.setText(moves.get(1).getName());
                    TooltipCompat.setTooltipText(mSelectMove2,moves.get(1).getDescription());
                    mSelectMove3.setText(moves.get(2).getName());
                    TooltipCompat.setTooltipText(mSelectMove3,moves.get(2).getDescription());
                    mSelectMove4.setText(moves.get(3).getName());
                    TooltipCompat.setTooltipText(mSelectMove4,moves.get(3).getDescription());
                    mBattleText.append("\n" + playerHuman.getName() + " versus " + cpuHuman.getName() + "!");
                    mBattleText.append("\nHint: Long Press a move button to see it's description!");
//                    mBattleText.scrollTo(0,mBattleText.getLayout().getLineTop(mBattleText.getLineCount()) - mBattleText.getHeight()-680);

                }
            }
        });

        mSelectRetreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                output(mBattleText,"\n" + playerHuman.getName() + " versus " + cpuHuman.getName() + "!");
                Intent intent = BattleHumanActivity.getIntent(getApplicationContext(), currentUser.getUserId());
                startActivity(intent);
            }
        });

        mSelectMove1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playerHuman.getModifiedSpeed()<cpuHuman.getModifiedSpeed()){
                    //cpu is faster
                    useMove(moves.get(random.nextInt(4)+4),mBattleText,cpuHuman,playerHuman);
                    if(playerHuman.getHp()>0){
                        useMove(moves.get(0),mBattleText,playerHuman,cpuHuman);
                    }
                } else {
                    //player is faster
                    useMove(moves.get(0),mBattleText,playerHuman,cpuHuman);
                    if(cpuHuman.getHp()>0){
                        useMove(moves.get(random.nextInt(4)+4),mBattleText,cpuHuman,playerHuman);
                    }
                }

            }
        });

    }
    public void output(TextView t, String s){
        t.append("\n"+ s);
        t.scrollTo(0,t.getLayout().getLineTop(t.getLineCount()) - t.getHeight());
        return;
    }
    public void useMove(Move m, TextView t, Human user, Human target){
        output(t,user.getName() + " used "+ m.getName() + " on " + target.getName() + "!");
    }
    public static Intent getIntent(Context context, int userId){
        Intent intent = new Intent(context, BattleHumanActivity.class);
        intent.putExtra("USERID", userId);
        return intent;
    }
}