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

import com.example.pockethumans.databinding.ActivityRegisterBinding;
import com.example.pockethumans.DB.UserLoginDAO;
import com.example.pockethumans.DB.AppDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText mRegisterUsername;
    EditText mRegisterPassword;
    EditText mRegisterPasswordConfirm;
    Button mRegisterButton;
    TextView mRegisterFeedback;
    Button mRegisterLogin;

    ActivityRegisterBinding mRegisterBinding;
    UserLoginDAO mUsersDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRegisterBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(mRegisterBinding.getRoot());

        mRegisterUsername = mRegisterBinding.registerUsernameEntry;
        mRegisterPassword = mRegisterBinding.registerPasswordEntry;
        mRegisterPasswordConfirm = mRegisterBinding.registerPasswordConfirm;
        mRegisterButton = mRegisterBinding.registerNewUserButton;
        mRegisterFeedback = mRegisterBinding.registerFeedback;
        mRegisterLogin = mRegisterBinding.goBackToLoginButton;

        mUsersDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .UserLoginDAO();

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mRegisterUsername.getText().toString();
                String password = mRegisterPassword.getText().toString();
                String confirm = mRegisterPasswordConfirm.getText().toString();

                if (username.length() == 0) {
                    mRegisterFeedback.setText(R.string.username_cannot_be_blank);
                    return;
                }
                if (password.length() == 0) {
                    mRegisterFeedback.setText(R.string.password_cannot_be_blank);
                    return;
                }
                //Check if username is taken
                if (mUsersDAO.getUsernames().contains(username)) {
                    mRegisterFeedback.setText(R.string.username_taken);
                    return;
                }
                //Check if passwords match
                if (!password.equals(confirm)) {
                    mRegisterFeedback.setText(R.string.password_mismatch);
                    return;
                } else {
                    mRegisterFeedback.setText("");
                    User user = new User(username, password, false);
                    mUsersDAO.insert(user);
                    Intent intent = LandingActivity.getIntent(getApplicationContext(), user);
                    startActivity(intent);
                }
            }
        });

        mRegisterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.getIntent(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        return intent;
    }
}