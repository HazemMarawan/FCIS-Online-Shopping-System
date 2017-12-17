package com.example.hazemmarawan.fcisshopping;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Search extends AppCompatActivity {
private ImageButton VoiceSearching;
private Button TextSearching;
private EditText KeyWordForSearching;
private RadioButton VoiceSelection;
private RadioButton TextSelection;
private FCISShoppingDB DBOBj;
private TextView TestTextNow;
private String UserNameLast;
private ArrayAdapter<String> ListAdapterSeach;
private ListView OutList;
private Cursor AllProductFromDb;
protected static final int RESULT_SPEECH = 1;
private String ItemWanted;



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
            Intent GotoLogout=new Intent(Search.this,Login.class);

            startActivity(GotoLogout);
        }
        else if(item.getItemId()==R.id.CategoriesID)
        {
            Intent GotoSearch=new Intent(Search.this,Categories.class);
            GotoSearch.putExtra("user_name",UserNameLast);
            startActivity(GotoSearch);
        }
        else if(item.getItemId()==R.id.SListID)
        {
            Intent GotoShoppingList=new Intent(Search.this,ShoppingList.class);
            GotoShoppingList.putExtra("user_name",UserNameLast);
            startActivity(GotoShoppingList);
        }

        return true;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater=new MenuInflater(this);
        inflater.inflate(R.menu.contextforpayment,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(R.id.addToShoppingList==item.getItemId())
        {
            AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            String SelectedLab=((TextView)(info.targetView)).getText().toString();
            String[] LabComponent=SelectedLab.split("/");
            String ItemName=LabComponent[0];
            float Price=Float.parseFloat(LabComponent[1]);
            int NewQuan=Integer.parseInt(LabComponent[2]);


            if(item.getItemId()==R.id.addToShoppingList)
            {
                if(NewQuan!=0) {
                    NewQuan -= 1;
                    DBOBj.proUpdate(ItemName, Price, NewQuan);
                    DBOBj.addToShoppingList(UserNameLast, ItemName, Price);

                    AllProductFromDb = DBOBj.searchProducts(ItemWanted);
                    ListAdapterSeach.clear();
                    if (AllProductFromDb != null) {
                        while (!AllProductFromDb.isAfterLast()) {
                            ListAdapterSeach.add(AllProductFromDb.getString(0) + "/" + AllProductFromDb.getString(1) + "/" + AllProductFromDb.getString(2));
                            AllProductFromDb.moveToNext();
                        }
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Not Available Now",Toast.LENGTH_LONG).show();
                }



            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        DBOBj=new FCISShoppingDB(this);
        VoiceSearching=(ImageButton)findViewById(R.id.SEARCHVOICEID);
        TextSearching=(Button)findViewById(R.id.SEARCHTEXTID);
        VoiceSelection=(RadioButton)findViewById(R.id.VoiceIDRadio);
        TextSelection=(RadioButton)findViewById(R.id.TextSearchIDRadio);
        KeyWordForSearching=(EditText)findViewById(R.id.KeyWordID);
        TestTextNow=(TextView)findViewById(R.id.TestSearchingV);
        ListAdapterSeach=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        OutList=(ListView)findViewById(R.id.SeaList);
        OutList.setAdapter(ListAdapterSeach);
        UserNameLast=getIntent().getStringExtra("user_name");
        registerForContextMenu(OutList);
        VoiceSearching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                try {
                    startActivityForResult(intent, RESULT_SPEECH);

                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Opps! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }



            }
        });

        TextSearching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!KeyWordForSearching.getText().toString().equals(""))
                {
                    ItemWanted=KeyWordForSearching.getText().toString();
                    Cursor MatchedProducts=DBOBj.searchProducts(ItemWanted);

                    if(MatchedProducts!=null)
                    {
                        ListAdapterSeach.clear();
                        while (!MatchedProducts.isAfterLast())
                        {
                            ListAdapterSeach.add(MatchedProducts.getString(0)+"/"+MatchedProducts.getFloat(1)+"/"+MatchedProducts.getInt(2));
                            MatchedProducts.moveToNext();
                        }
                    }
                    else
                    {
                        ListAdapterSeach.clear();
                        ListAdapterSeach.add("No Matched Items");
                    }

                }
                else
                {
                    KeyWordForSearching.setError("Enter KeyWord");
                }
            }
        });

        VoiceSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VoiceSearching.setVisibility(View.VISIBLE);
                TextSearching.setVisibility(View.INVISIBLE);
                KeyWordForSearching.setVisibility(View.INVISIBLE);
            }
        });

        TextSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VoiceSearching.setVisibility(View.INVISIBLE);
                TextSearching.setVisibility(View.VISIBLE);
                KeyWordForSearching.setVisibility(View.VISIBLE);
            }
        });




    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case RESULT_SPEECH:
            {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    ItemWanted=text.get(0);

                    Cursor MatchedProducts = DBOBj.searchProducts(ItemWanted);
                    if (MatchedProducts != null)
                    {
                        ListAdapterSeach.clear();
                        while (!MatchedProducts.isAfterLast())
                        {
                            ListAdapterSeach.add(MatchedProducts.getString(0)+"/"+MatchedProducts.getFloat(1)+"/"+MatchedProducts.getInt(2));
                            MatchedProducts.moveToNext();
                        }
                    }

                    TestTextNow.setText(text.get(0));


                }
            }
        }
    }
}
