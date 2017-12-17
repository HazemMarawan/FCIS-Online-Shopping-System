package com.example.hazemmarawan.fcisshopping;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
private FCISShoppingDB DBOBj;
private EditText UserNameFromUser;
private EditText PassFormUser;
private TextView FORGET;
private TextView CreateAccount;
private Button LoginBut;
private SharedPreferences data ;
public static   SharedPreferences.Editor editor;

private CheckBox CheckRem;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       // String LOGINDATA="SHAREDDATA";


        DBOBj=new FCISShoppingDB(this);
        UserNameFromUser=(EditText)findViewById(R.id.EMAILADDID);
        PassFormUser=(EditText)findViewById(R.id.PassID);
        FORGET=(TextView)findViewById(R.id.FORGETID);
        CreateAccount=(TextView)findViewById(R.id.CreateID);
        CheckRem=(CheckBox)findViewById(R.id.REMEMBERID);
        LoginBut=(Button)findViewById(R.id.LOGINID);
       // SharedPrefereneces
        data = getSharedPreferences("user_data", MODE_PRIVATE);
        editor = data.edit();
        if(data.contains("saveLogin")) {
            UserNameFromUser.setText(data.getString("username", ""));
            PassFormUser.setText(data.getString("password", ""));
            CheckRem.setChecked(true);
        }


        LoginBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DBOBj.logIn(UserNameFromUser.getText().toString(),PassFormUser.getText().toString())==true)
                {

                    if(CheckRem.isChecked())
                    {

                        editor.putBoolean("saveLogin", true);
                        editor.putString("username", UserNameFromUser.getText().toString());
                        editor.putString("password", PassFormUser.getText().toString());
                        editor.commit();

                    }
                    else
                    {
                        editor.clear();
                        editor.commit();


                    }
                    Intent GoToCat=new Intent(Login.this,Categories.class);
                    GoToCat.putExtra("user_name",UserNameFromUser.getText().toString());
                    startActivity(GoToCat);
                }
                else
                {
                    UserNameFromUser.setError("Error");
                    PassFormUser.setError("Error");
                }

            }
        });
        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CreateIn=new Intent(Login.this,SignUP.class);
                startActivity(CreateIn);
            }
        });

        FORGET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ForgetIn=new Intent(Login.this,ForgetPassword.class);
                startActivity(ForgetIn);
            }
        });
    }
}
