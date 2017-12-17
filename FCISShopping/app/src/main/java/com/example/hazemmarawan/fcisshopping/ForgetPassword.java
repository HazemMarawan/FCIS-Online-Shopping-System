package com.example.hazemmarawan.fcisshopping;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class ForgetPassword extends AppCompatActivity {
private Button GetPassFromDb;
private FCISShoppingDB DBOBj;
private EditText EmailEntered;
private EditText QuestionEntered;
private EditText AnswerEntered;
private boolean CheckData=true;
private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        DBOBj=new FCISShoppingDB(this);
        EmailEntered=(EditText)findViewById(R.id.FMID);
        GetPassFromDb=(Button)findViewById(R.id.PASSFID);
        QuestionEntered=(EditText)findViewById(R.id.QuestionForget);
        AnswerEntered=(EditText)findViewById(R.id.AnswerForget);

        GetPassFromDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if(EmailEntered.getText().toString().equals(""))
                    {
                        CheckData=false;
                        EmailEntered.setError("Enter Email");
                    }
                    if(QuestionEntered.getText().toString().equals(""))
                    {
                        CheckData=false;
                        QuestionEntered.setError("Enter Qusetion");
                    }
                    if(AnswerEntered.getText().toString().equals(""))
                    {
                        CheckData=false;
                        AnswerEntered.setError("Enter Password");
                    }
                    if(CheckData==true)
                    {
                        password = DBOBj.getUserPassword(EmailEntered.getText().toString(),QuestionEntered.getText().toString(),AnswerEntered.getText().toString());

                        if(password.equals("NoPass"))
                        {
                            Toast.makeText(getApplicationContext(),"Wrong Data",Toast.LENGTH_LONG).show();


                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Password IS: "+password,Toast.LENGTH_LONG).show();

                        }
                    }




                    /*String To=EmailEntered.getText().toString();
                    String Subjet="FCIS ONLINE SHOPPING PASSWORD";
                    String Message="Your Passwod IS: "+password;

                    Intent SendMail=new Intent(Intent.ACTION_SEND);
                    SendMail.putExtra(SendMail.EXTRA_EMAIL,new String[]{To});
                    SendMail.putExtra(SendMail.EXTRA_SUBJECT,new String[]{Subjet});
                    SendMail.putExtra(SendMail.EXTRA_TEXT,new String[]{Message});

                    SendMail.setType("message/rfc822");
                    startActivity(Intent.createChooser(SendMail,"Select Email App"));*/






               /* try {

                    Toast.makeText(getApplicationContext(), "The Password is "+password, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }*/


            }
        });


    }
}
