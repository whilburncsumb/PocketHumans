package com.example.pockethumans;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

        mRegisterButton.setOnClickListener(v -> {
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
            if (mUsersDAO.listUsernames().contains(username)) {
                mRegisterFeedback.setText(R.string.username_taken);
                return;
            }
            //Check if passwords match
            if (!password.equals(confirm)) {
                mRegisterFeedback.setText(R.string.password_mismatch);
            } else {
                mRegisterFeedback.setText("");
                User user = new User(username, password, false);
                mUsersDAO.insert(user);
                Intent intent = LandingActivity.getIntent(getApplicationContext(), mUsersDAO.checkLogin(user.getUsername(),user.getPassword()).getUserId());
                startActivity(intent);
            }
        });

        mRegisterLogin.setOnClickListener(v -> {
            Intent intent = MainActivity.getIntent(getApplicationContext());
            startActivity(intent);
        });
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        return intent;
    }
}