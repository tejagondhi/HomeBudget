package com.example.homebudget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homebudget.listeners.LoginNetworkInterface;
import com.example.homebudget.models.User;
import com.example.homebudget.util.Constants;
import com.example.homebudget.util.HomeNetwork;
import com.example.homebudget.util.PreferencesUtil;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements LoginNetworkInterface {

    private Spinner drpdown;
    private User selectedUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acttivity);
        Button loginbtn = findViewById(R.id.login_btn);
        drpdown = findViewById(R.id.login_users);
        loginbtn.setOnClickListener(v -> {
            new PreferencesUtil(LoginActivity.this).setPreference(Constants.LOGIN_USER,selectedUser.getPersonId());
            new PreferencesUtil(LoginActivity.this).setPreference(Constants.USER_NAME,selectedUser.getUserName());
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        });
        if (new PreferencesUtil(LoginActivity.this).getPreference(Constants.LOGIN_USER)!=null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }else{
            HomeNetwork.getInstance().getUsers(this);
        }

    }

    @Override
    public void onReceived(ArrayList<User> users) {
        String[] userArray = new String[users.size()];
        int count=0;
        for (User user: users) {
            userArray[count++]= user.getUserName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, userArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drpdown.setAdapter(adapter);
        drpdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUser=users.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();

    }
}