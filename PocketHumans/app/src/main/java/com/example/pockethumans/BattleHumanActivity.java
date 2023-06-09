package com.example.pockethumans;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    int winnerDecided = 0; //0 if no winner has been chosen, 1 if the player wins, 2 if the cpu wins
    Human playerHuman;
    Human cpuHuman;
    ArrayList<Move> moves = new ArrayList<>();
    Random random = new Random();
    User currentUser;

    Button mSelectMove1;
    Button mSelectMove2;
    Button mSelectMove3;
    Button mSelectMove4;
    Button mSelectRetreat;
    TextView mYourHealthBarText;
    TextView mEnemyHealthBarText;

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
        currentUser = mUsersDAO.checkUserByID(getIntent().getIntExtra("USERID",0));
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
        mSelectMove1 = mBattleHumanBinding.selectMove1Button;
        mSelectMove2 = mBattleHumanBinding.selectMove2Button;
        mSelectMove3 = mBattleHumanBinding.selectMove3Button;
        mSelectMove4 = mBattleHumanBinding.selectMove4Button;
        mSelectRetreat = mBattleHumanBinding.retreatButton;
        TextView mBattleText = mBattleHumanBinding.battleOutput;
        mBattleText.setMovementMethod(new ScrollingMovementMethod());
        //Couldn't get these to work but they would have been cool
//        ProgressBar mYourHealthBar= mBattleHumanBinding.yourHumanHP;
//        ProgressBar mEnemyHealthBar= mBattleHumanBinding.enemyHumanHP;
        mYourHealthBarText= mBattleHumanBinding.yourHealthText;
        mEnemyHealthBarText= mBattleHumanBinding.enemyHealthText;

        //populate the list of humans
        ArrayAdapter<Human> adapterH = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, mHumanDAO.listHumans());
        adapterH.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSelectYourHumanSpinner.setAdapter(adapterH);
        mSelectOpponentSpinner.setAdapter(adapterH);
        mSelectOpponentSpinner.setSelection(5);

        mSelectYourHumanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Human selection = (Human) parent.getItemAtPosition(position);
                Human oppSelection = (Human) mSelectOpponentSpinner.getSelectedItem();

                if(selection.getHumanId()==oppSelection.getHumanId()){
                    //prevent user from fighting a human against the same type of human
                    validSelection = false;
                    mUnlockFeedback.setVisibility(View.VISIBLE);
                    mUnlockFeedback.setText(R.string.clone_human_feedback);
                    animator.start();
                    mBeginBattleButton.setEnabled(false);
                } else if(selection.getHumanId()==6 && !currentUser.isUnlockedDrC()){
                    //if Dr.C, check that user has unlocked him
                    validSelection = false;
                    mUnlockFeedback.setVisibility(View.VISIBLE);
                    mUnlockFeedback.setText(R.string.drc_need_to_unlock_feedback);
                    animator.start();
                    mBeginBattleButton.setEnabled(false);
                } else {
                    validSelection = true;
                    mUnlockFeedback.setVisibility(View.INVISIBLE);
                    mBeginBattleButton.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSelectOpponentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Human selection = (Human) parent.getItemAtPosition(position);
                Human oppSelection = (Human) mSelectYourHumanSpinner.getSelectedItem();

                if(selection.getHumanId()==oppSelection.getHumanId()){
                    validSelection = false;
                    mUnlockFeedback.setVisibility(View.VISIBLE);
                    mUnlockFeedback.setText(R.string.clone_human_feedback);
                    animator.start();
                    mBeginBattleButton.setEnabled(false);
                } else {
                    validSelection = true;
                    mUnlockFeedback.setVisibility(View.INVISIBLE);
                    mBeginBattleButton.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBeginBattleButton.setOnClickListener(v -> {
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
                refreshHPDisplay();
                mBattleText.append("\n" + playerHuman.getName() + " versus " + cpuHuman.getName() + "!");
                mBattleText.append(getString(R.string.long_press_hint));

            }
        });

        mSelectRetreat.setOnClickListener(view -> {
            if(winnerDecided==1 && cpuHuman.getHumanId()==6){
                mUsersDAO.update(currentUser);
            }
            Intent intent = BattleHumanActivity.getIntent(getApplicationContext(), currentUser.getUserId());
            startActivity(intent);
        });

        mSelectMove1.setOnClickListener(view -> combatRound(mBattleText,0));

        mSelectMove2.setOnClickListener(view -> combatRound(mBattleText,1));

        mSelectMove3.setOnClickListener(view -> combatRound(mBattleText,2));

        mSelectMove4.setOnClickListener(view -> combatRound(mBattleText,3));

    }
    public void output(TextView t, String s){
        //prints texts to screen and scrolls to bottom
        t.append("\n"+ s);
        t.scrollTo(0,t.getLayout().getLineTop(t.getLineCount()) - t.getHeight());
    }
    public void useMove(Move m, TextView t, Human user, Human target){
        //outputs results of a human using a move
        switch (m.getType()){
            case "attack": {
                output(t,user.getName() + " used "+ m.getName() + " on " + target.getName() + "!");
                double damage = (double) ((user.getModifiedAttack()+m.getPower())-target.getModifiedDefense());
                if(damage<1){
                    damage=1;//damage cannot be less than 1 hp
                }
                output(t,target.getName() + " took "+ damage + " damage!");
                target.setHp(target.getHp()-damage);
                if(target.getHp()<=0){
                    output(t, target.getName() + " fainted! " + user.getName() + " is the winner!");
                }
                break;
            }
            case "heal": {
                double heals = m.getPower();
                if(user.getHp()+heals > 100){ //cannot overheal
                    output(t,user.getName() + " used "+ m.getName() + "! They healed " + heals + " hit points and are now full!");
                    user.setHp(100);
                } else {
                    output(t,user.getName() + " used "+ m.getName() + "! They healed " + heals + " hit points!");
                    user.addHp(heals);
                }

                break;
            }
            case "statusAttack":{
                if(m.getPower()>1){
                    //buffing move
                    output(t,user.getName() + " used "+ m.getName() + "!");
                    output(t,user.getName() + " increased their attack stat!");
                    user.setAttackModifier(user.getAttackModifier()*m.getPower());
                } else {
                    output(t,user.getName() + " used "+ m.getName() + " on " + target.getName() + "!");
                    output(t,user.getName() + " reduced "+ target.getName() + "'s attack stat!");
                    target.setAttackModifier(target.getAttackModifier()*m.getPower());
                }
                break;
            }
            case "statusDefense":{
                if(m.getPower()>1){
                    //buffing move
                    output(t,user.getName() + " used "+ m.getName() + "!");
                    output(t,user.getName() + " increased their defense stat!");
                    user.setDefenseModifier(user.getDefenseModifier()*m.getPower());
                } else {
                    output(t,user.getName() + " used "+ m.getName() + " on " + target.getName() + "!");
                    output(t,user.getName() + " reduced "+ target.getName() + "'s defense stat!");
                    target.setDefenseModifier(target.getDefenseModifier()*m.getPower());
                }
                break;
            }
            case "statusSpeed":{
                if(m.getPower()>1){
                    //buffing move
                    output(t,user.getName() + " used "+ m.getName() + "!");
                    output(t,user.getName() + " increased their speed stat!");
                    user.setSpeedModifier(user.getSpeedModifier()*m.getPower());
                } else {
                    output(t,user.getName() + " used "+ m.getName() + " on " + target.getName() + "!");
                    output(t,user.getName() + " reduced "+ target.getName() + "'s attack stat!");
                    target.setSpeedModifier(target.getSpeedModifier()*m.getPower());
                }
                break;
            }
            default:{
                output(t,"ERROR: " + m.getName() + " HAS INVALID TYPE!");
                break;
            }
        }
    }
    public void combatRound(TextView t,int moveIndex){
        //Runs a round of combat, used when user selects a move
        output(t,"-------------");
        if(playerHuman.getModifiedSpeed()<cpuHuman.getModifiedSpeed()){
            //cpu is faster
            System.out.println(random.nextInt(4)+4);//for some reason the next line breaks if this sout isn't here???
            useMove(moves.get(random.nextInt(4)+4),t,cpuHuman,playerHuman);
            if(playerHuman.getHp()>0){
                useMove(moves.get(moveIndex),t,playerHuman,cpuHuman);
            }
        } else {
            //player is faster
            System.out.println(random.nextInt(4)+4);//for some reason the next line breaks if this sout isn't here???
            useMove(moves.get(moveIndex),t,playerHuman,cpuHuman);
            if(cpuHuman.getHp()>0){
                useMove(moves.get(random.nextInt(4)+4),t,cpuHuman,playerHuman);
            }
        }
        if(playerHuman.getHp()<=0){
            winnerDecided=2;//this means the cpu won
        }
        if(cpuHuman.getHp()<=0){
            winnerDecided=1;//this means player won
            if(cpuHuman.getHumanId()==6){
//                if the opponent is dr.C the user will unlock him
                currentUser.setUnlockedDrC(true);
//                mUsersDAO.update(currentUser);
            }
        }
        if(winnerDecided!=0){
            mSelectMove1.setEnabled(false);
            mSelectMove2.setEnabled(false);
            mSelectMove3.setEnabled(false);
            mSelectMove4.setEnabled(false);
            mSelectRetreat.setText(R.string.new_battle);
            mSelectRetreat.setBackgroundColor(Color.GREEN);
            mSelectRetreat.setTextColor(Color.BLACK);
        }
        refreshHPDisplay();
    }
    public void refreshHPDisplay(){
        mYourHealthBarText.setText(playerHuman.getName()  + getString(R.string.colon) + playerHuman.getHp() + getString(R.string.outof100));
        mEnemyHealthBarText.setText(cpuHuman.getName()  + getString(R.string.colon) + cpuHuman.getHp() + getString(R.string.outof100));
    }
    public static Intent getIntent(Context context, int userId){
        Intent intent = new Intent(context, BattleHumanActivity.class);
        intent.putExtra("USERID", userId);
        return intent;
    }
}