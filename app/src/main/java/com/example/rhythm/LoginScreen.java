package com.example.rhythm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginScreen extends AppCompatActivity {

    TextInputEditText etUsername, etPassword;
    TextInputLayout tilUsername, tilPassword;
    Button btSignIn;
    CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tilUsername = findViewById(R.id.username);
        tilPassword = findViewById(R.id.password);
        btSignIn = findViewById(R.id.btSignIn);
        checkBox = findViewById(R.id.checkBox);

        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        boolean check = sharedPreferences.getBoolean("flag", false);
        Intent intent;
        if (check){
            intent = new Intent(LoginScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        }



        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tilUsername.setError(null);
                tilPassword.setError(null);
                String username = Objects.requireNonNull(etUsername.getText()).toString().trim();
                String password = Objects.requireNonNull(etPassword.getText()).toString().trim();

                DataService dataService = new DataService(LoginScreen.this);
                dataService.userValidate(username, password, new DataService.UserValidateResponseListener() {
                    @Override
                    public void onResponse(boolean valid) {
                        Intent intent;
                        if (valid) {
                            SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            if(checkBox.isChecked()){
                                editor.putBoolean("flag", true);
                                editor.apply();
                            }
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.apply();
                            intent = new Intent(LoginScreen.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            tilPassword.setErrorEnabled(true);
                            tilPassword.setError("Check password");
                            tilUsername.setErrorEnabled(true);
                            tilUsername.setError("Check username");
                        }

                    }

                    @Override
                    public void onError(String message) {

                    }
                });
            }
        });

    }
}