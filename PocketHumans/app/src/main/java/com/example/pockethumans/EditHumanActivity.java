package com.example.pockethumans;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

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

import com.example.pockethumans.DB.AppDatabase;
import com.example.pockethumans.DB.HumanDAO;
import com.example.pockethumans.DB.MovesDAO;
import com.example.pockethumans.DB.UserLoginDAO;
import com.example.pockethumans.databinding.ActivityEdithumanBinding;

public class EditHumanActivity extends AppCompatActivity {
    ActivityEdithumanBinding mEditHumanBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edithuman);

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
        boolean editMode = getIntent().getBooleanExtra("EDITMODE",false);
        //editMode determines if we are editing an existing human (true) or creating a new one (false)
        mEditHumanBinding = ActivityEdithumanBinding.inflate(getLayoutInflater());
        setContentView(mEditHumanBinding.getRoot());
        Spinner mSelectHumanSpinner = mEditHumanBinding.selectHumanSpinner;
        Button mSubmitHumanButton= mEditHumanBinding.editHumanSubmit;
        EditText mEditHumanName= mEditHumanBinding.editHumanName;
        EditText mEditHumanDescription= mEditHumanBinding.editHumanDescription;
        EditText mEditAttack= mEditHumanBinding.editHumanAttack;
        EditText mEditDefense= mEditHumanBinding.editHumanDefense;
        EditText mEditSpeed= mEditHumanBinding.editHumanSpeed;
        Spinner mSelectMove1= mEditHumanBinding.move1Spinner;
        Spinner mSelectMove2= mEditHumanBinding.move2Spinner;
        Spinner mSelectMove3= mEditHumanBinding.move3Spinner;
        Spinner mSelectMove4= mEditHumanBinding.move4Spinner;
        TextView mStatFeedback = mEditHumanBinding.editHumanStatFeedback;
        TextView mSelectYourHuman = mEditHumanBinding.selectTextview;
        ObjectAnimator animator = ObjectAnimator.ofFloat(mStatFeedback,"rotation",-10,10);
        animator.setDuration(250);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(2);

        //populate the list of humans
        ArrayAdapter<Human> adapterH = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, mHumanDAO.listHumans());
        adapterH.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSelectHumanSpinner.setAdapter(adapterH);

        //Populate the move lists
        ArrayAdapter<Move> adapterM = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, mMovesDAO.listMoves());
        adapterM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSelectMove1.setAdapter(adapterM);
        mSelectMove2.setAdapter(adapterM);
        mSelectMove3.setAdapter(adapterM);
        mSelectMove4.setAdapter(adapterM);
        if(currentUser.isAdmin()){
            mStatFeedback.setText(R.string.admins_can_give_a_human_more_than_90_stat_points);
            mStatFeedback.setTextSize(16);
        }

        mSelectHumanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Human selection = (Human) parent.getItemAtPosition(position);

                if(editMode){//Populate the input fields with the existing values from the DB if in edit mode
                    mEditHumanName.setText(selection.getName());
                    mEditHumanDescription.setText(selection.getDescription());
                    mEditAttack.setText(selection.getAttack()+"");
                    mEditDefense.setText(selection.getDefense()+"");
                    mEditSpeed.setText(selection.getSpeed()+"");
                    mSelectMove1.setSelection(selection.getMove1id()-1);
                    mSelectMove2.setSelection(selection.getMove2id()-1);
                    mSelectMove3.setSelection(selection.getMove3id()-1);
                    mSelectMove4.setSelection(selection.getMove4id()-1);
                } else {
                    mEditAttack.setText(30+"");
                    mEditDefense.setText(30+"");
                    mEditSpeed.setText(30+"");
                    mSelectHumanSpinner.setVisibility(View.INVISIBLE);
                    mSelectYourHuman.setText(R.string.create_your_human);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSubmitHumanButton.setOnClickListener(v -> {
            //submit new values when user clicks submit
            int attack = Integer.parseInt(mEditAttack.getText().toString());
            int defense = Integer.parseInt(mEditDefense.getText().toString());
            int speed = Integer.parseInt(mEditSpeed.getText().toString());
            int total = (attack + defense + speed);
            if(total>90 && !currentUser.isAdmin()){
                mStatFeedback.setTextColor(Color.RED);
                mStatFeedback.setTypeface(Typeface.DEFAULT_BOLD);
                animator.start();
            } else {
                //Update the human
                Human updateHuman = new Human(mEditHumanName.getText().toString(),mEditHumanDescription.getText().toString(),
                        attack,defense,speed,mSelectMove1.getSelectedItemPosition()+1,mSelectMove2.getSelectedItemPosition()+1,mSelectMove3.getSelectedItemPosition()+1,mSelectMove4.getSelectedItemPosition()+1);
                System.out.println(mSelectMove1.getSelectedItemPosition()+1);
                System.out.println(mSelectMove2.getSelectedItemPosition()+1);
                System.out.println(mSelectMove3.getSelectedItemPosition()+1);
                System.out.println(mSelectMove4.getSelectedItemPosition()+1);
                if(editMode){
                    updateHuman.setHumanId(((Human) mSelectHumanSpinner.getSelectedItem()).getHumanId());//this is a ridiculous way of getting the ID of the currently selected human. I hate it.
                    mHumanDAO.update(updateHuman);
                } else {
                    mHumanDAO.insert(updateHuman);
                }
                Intent intent = LandingActivity.getIntent(getApplicationContext(), currentUser.getUserId());
                startActivity(intent);
            }
        });
    }
    public static Intent getIntent(Context context, int userId, boolean editMode){
        Intent intent = new Intent(context, EditHumanActivity.class);
        intent.putExtra("USERID", userId);
        intent.putExtra("EDITMODE", editMode);
        //this determines if the activity is adding a new human or editing an existing one.
        // false for new human, true for editing
        return intent;
    }
}