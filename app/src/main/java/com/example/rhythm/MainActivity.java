package com.example.rhythm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //loading the home fragment
        replaceFragment(new HomeFragment());

        //Variables
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navBar);
        toolbar = findViewById(R.id.toolBar);

        //Menu actions
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();

                if(itemId == R.id.changePasswordMenu){
                    showChangePasswordDialog();
                } else if (itemId == R.id.logOutMenu) {
                    //storing login state
                    SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("flag", false);
                    editor.apply();

                    //redirect to login screen
                    Intent intent = new Intent(MainActivity.this, LoginScreen.class);
                    startActivity(intent);
                    finishAffinity();
                    return true;
                }
                return false;
            }
        });


        //Navigation drawer toggle
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        //Navigation actions
        final MenuItem[] it= new MenuItem[1];
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (it[0] != null) {it[0].setChecked(false);}
                it[0] = item;
                int id = item.getItemId();
                item.setChecked(true);
                drawerLayout.closeDrawer(GravityCompat.START);
                if (id == R.id.navHome){
                    replaceFragment(new HomeFragment());
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (id == R.id.navMyDetails) {
                    replaceFragment(new MyDetailsFragment());
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    replaceFragment(new CheckReportFragment());
                    drawerLayout.closeDrawer(GravityCompat.START);
                }

                return true;
            }
        });
    }

    private void showChangePasswordDialog() {
        //inflate the dialog layout
        View view = LayoutInflater.from(this).inflate(R.layout.layout_change_password, null);

        //variables
        TextInputLayout tilCurrentPass = view.findViewById(R.id.tilCurrentPassword);
        TextInputLayout tilNewPass = view.findViewById(R.id.tilNewPassword);
        EditText etCurrentPass = view.findViewById(R.id.etCurrentPassword);
        EditText etNewPass = view.findViewById(R.id.etNewPassword);
        MaterialButton button = view.findViewById(R.id.btUpdatePassword);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //button actions
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentPass = etCurrentPass.getText().toString().trim() ;
                String newPass = etNewPass.getText().toString().trim();

                //checking for empty string
                if(currentPass.isEmpty() || newPass.isEmpty()){
                    tilCurrentPass.setErrorEnabled(true);
                    tilNewPass.setErrorEnabled(true);
                    tilCurrentPass.setError("Fill properly");
                    tilNewPass.setError("Fill properly");
                    return;
                }
                SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                String patientID = sharedPreferences.getString("username", "error");
                new DataService(MainActivity.this).changePassword(patientID, currentPass, newPass);
                alertDialog.dismiss();
            }
        });
    }

    //init menu option
    @Override
   public boolean onCreateOptionsMenu(Menu menu) {
       new MenuInflater(this).inflate(R.menu.toolbar_menu, menu);
       return super.onCreateOptionsMenu(menu);
   }

    //Function for fragment replacement
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}