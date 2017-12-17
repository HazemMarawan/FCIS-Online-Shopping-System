package com.example.hazemmarawan.fcisshopping;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductsOfCat extends AppCompatActivity {
    private FCISShoppingDB Obj;
    private Cursor AllProductFromDb;
    private ArrayAdapter<String> ListAllProAdapter;
    private ListView ProductsList;
    private String UserNameLast;
    private String NameOFCat;
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=new MenuInflater(this);
        inflater.inflate(R.menu.application_menu,menu);
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
                Obj.proUpdate(ItemName, Price, NewQuan);
                Obj.addToShoppingList(UserNameLast, ItemName, Price);

                AllProductFromDb = Obj.ProductsByCategory(NameOFCat);
                ListAllProAdapter.clear();
                if (AllProductFromDb != null) {
                    while (!AllProductFromDb.isAfterLast()) {
                        ListAllProAdapter.add(AllProductFromDb.getString(0) + "/" + AllProductFromDb.getString(1) + "/" + AllProductFromDb.getString(2));
                        AllProductFromDb.moveToNext();
                    }
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"This Item Not Available Now",Toast.LENGTH_LONG).show();
            }



        }

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.LogoutID)
        {
            Intent GotoLogout=new Intent(ProductsOfCat.this,Login.class);
            startActivity(GotoLogout);
        }
        else if(item.getItemId()==R.id.SearchID)
        {
            Intent GotoSearch=new Intent(ProductsOfCat.this,Search.class);
            GotoSearch.putExtra("user_name",UserNameLast);
            startActivity(GotoSearch);
        }
        else if(item.getItemId()==R.id.SListID)
        {
            Intent GotoShoppingList=new Intent(ProductsOfCat.this,ShoppingList.class);
            GotoShoppingList.putExtra("user_name",UserNameLast);
            startActivity(GotoShoppingList);
        }
        else if(item.getItemId()==R.id.CategoriesID)
        {
            Intent GotoShoppingList=new Intent(ProductsOfCat.this,Categories.class);
            GotoShoppingList.putExtra("user_name",UserNameLast);
            startActivity(GotoShoppingList);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_of_cat);
        UserNameLast=getIntent().getStringExtra("user_name");
        NameOFCat=getIntent().getStringExtra("NameOFCat");
        ProductsList=(ListView)findViewById(R.id.AllProID);
        ListAllProAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        ProductsList.setAdapter(ListAllProAdapter);

        registerForContextMenu(ProductsList);

        Obj=new FCISShoppingDB(this);
        AllProductFromDb=Obj.ProductsByCategory(NameOFCat);
        if(AllProductFromDb!=null) {
            while (!AllProductFromDb.isAfterLast()) {
                ListAllProAdapter.add(AllProductFromDb.getString(0)+"/"+AllProductFromDb.getString(1)+"/"+AllProductFromDb.getString(2));
                AllProductFromDb.moveToNext();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"No Labs",Toast.LENGTH_LONG).show();
        }
    }
}
