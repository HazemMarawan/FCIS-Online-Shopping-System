package com.example.hazemmarawan.fcisshopping;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class SignUP extends AppCompatActivity {
    private  DatePickerDialog.OnDateSetListener mDataSetLisnter;
    private FCISShoppingDB DataBaseObj;
    private String Gender;
    private  EditText FullName;
    private  EditText NickName;
    private  EditText Email;
    private  EditText Password;
    private  TextView DateofBirth;
    private  RadioButton MaleR;
    private  RadioButton FemaleR;
    private  EditText JobText;
    private  EditText Question;
    private  EditText Answer;
    private Button SignUp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ImageButton CalenderID=(ImageButton)findViewById(R.id.CaLID);
        DataBaseObj=new FCISShoppingDB(this);
        SignUp=(Button)findViewById(R.id.SIGNUPID);
        FullName=(EditText)findViewById(R.id.FULLNAMEID);
        NickName=(EditText)findViewById(R.id.NICKNAMEID);
        Email=(EditText)findViewById(R.id.EMAILID);
        Password=(EditText)findViewById(R.id.PASSWORDID);
        DateofBirth=(TextView)findViewById(R.id.DATEID);
        MaleR=(RadioButton)findViewById(R.id.MALEID);
        FemaleR=(RadioButton)findViewById(R.id.FEMALEID);
        JobText=(EditText)findViewById(R.id.JOBID);
        Question=(EditText)findViewById(R.id.QuestionID);
        Answer=(EditText)findViewById(R.id.AnswerOfQueID);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(MaleR.isChecked())
                {
                    Gender="Male";
                }
                else if(FemaleR.isChecked())
                {
                    Gender="Female";
                }

                boolean Insert=true;
                if(FullName.getText().toString().equals(""))
                {
                    Insert=false;
                  FullName.setError("Enter Full Name");
                }
                if(NickName.getText().toString().equals(""))
                {
                    Insert=false;
                   NickName.setError("Enter NickName");
                }
                if(Email.getText().toString().equals(""))
                {
                    Insert=false;
                    Email.setError("Enter Email");
                }
                if(Password.getText().toString().equals(""))
                {
                    Insert=false;
                    Password.setError("Enter Password");
                }
                if(DateofBirth.getText().toString().equals(""))
                {
                    Insert=false;
                    DateofBirth.setError("Enter BirthDate");

                }
                if(JobText.getText().toString().equals(""))
                {
                    Insert=false;
                  JobText.setError("Enter Job");
                }
                if(MaleR.isChecked()==false&&FemaleR.isChecked()==false)
                {
                    Insert=false;
                    MaleR.setError("Enter Gender");
                    FemaleR.setError("Enter Gender");
                }
                if(Insert==true)
                {
                    DataBaseObj.signUp(FullName.getText().toString(),NickName.getText().toString(),Email.getText().toString(),Password.getText().toString(),Gender,DateofBirth.getText().toString(),JobText.getText().toString(),Question.getText().toString(),Answer.getText().toString());
                    Intent i=new Intent(SignUP.this,Categories.class);
                    i.putExtra("user_name",NickName.getText().toString());
                    startActivity(i);
                    Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_LONG).show();


                }






            }
        });
        CalenderID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(SignUP.this,android.R.style.Theme_Holo_Dialog_MinWidth,mDataSetLisnter,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();



            }
        });
        mDataSetLisnter=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            month=month+1;
                TextView DateSetNowL=(TextView) findViewById(R.id.DATEID);
                DateSetNowL.setText(String.valueOf(year)+"/"+String.valueOf(month)+"/"+String.valueOf(day));
            }
        };




    }
}
