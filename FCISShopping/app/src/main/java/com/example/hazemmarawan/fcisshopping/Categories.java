package com.example.hazemmarawan.fcisshopping;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import at.markushi.ui.CircleButton;

public class Categories extends AppCompatActivity {
private String UserNameFromUser;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=new MenuInflater(this);
        inflater.inflate(R.menu.application_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.LogoutID)
        {
            Intent GotoLogout=new Intent(Categories.this,Login.class);
            startActivity(GotoLogout);
        }
        else if(item.getItemId()==R.id.SearchID)
        {
            Intent GotoSearch=new Intent(Categories.this,Search.class);
            GotoSearch.putExtra("user_name",UserNameFromUser);
            startActivity(GotoSearch);
        }
        else if(item.getItemId()==R.id.SListID)
        {
            Intent GotoShoppingList=new Intent(Categories.this,ShoppingList.class);
            GotoShoppingList.putExtra("user_name",UserNameFromUser);
            startActivity(GotoShoppingList);
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        UserNameFromUser=getIntent().getStringExtra("user_name");
        CircleButton MObiles=(CircleButton)findViewById(R.id.MOID);
        CircleButton Labs=(CircleButton)findViewById(R.id.LabID);
        CircleButton TVS=(CircleButton)findViewById(R.id.TVID);
        TVS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SwitchToTVs=new Intent(Categories.this,ProductsOfCat.class);
                SwitchToTVs.putExtra("NameOFCat","TV");
                SwitchToTVs.putExtra("user_name",UserNameFromUser);
                startActivity(SwitchToTVs);
            }
        });
        Labs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SwitchToLabs=new Intent(Categories.this,ProductsOfCat.class);
                SwitchToLabs.putExtra("NameOFCat","Labtops");
                SwitchToLabs.putExtra("user_name",UserNameFromUser);
                startActivity(SwitchToLabs);
            }
        });
        MObiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SwitchToMobile=new Intent(Categories.this,ProductsOfCat.class);
                SwitchToMobile.putExtra("NameOFCat","Mobiles");
                SwitchToMobile.putExtra("user_name",UserNameFromUser);
                startActivity(SwitchToMobile);

            }
        });

    }
}
