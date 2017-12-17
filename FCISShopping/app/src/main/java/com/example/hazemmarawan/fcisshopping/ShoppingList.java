package com.example.hazemmarawan.fcisshopping;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ShoppingList extends AppCompatActivity {
private ListView ShoppingListDis;
private FCISShoppingDB Obj;
private ArrayAdapter<String> ShoppingAdapter;
private Button GetShopinButton;
private Cursor GetShoppingListItemsCurrsor;
private Cursor TmpCur;
private String UserNameFromCat;
private float SumOfPrices;
private TextView LastResultOfSummion;
private ImageButton AddOrderButton;
private int GetUserId;
private boolean CheckList;
    private LocationManager locationManager;
    private LocationListener listener;
    private String LocationString;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=new MenuInflater(this);
        inflater.inflate(R.menu.application_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.LogoutID)
        {
            Intent GotoLogout=new Intent(ShoppingList.this,Login.class);
            startActivity(GotoLogout);
        }
        else if(item.getItemId()==R.id.SearchID)
        {
            Intent GotoSearch=new Intent(ShoppingList.this,Search.class);
            GotoSearch.putExtra("user_name",UserNameFromCat);
            startActivity(GotoSearch);
        }
        else if(item.getItemId()==R.id.CategoriesID)
        {
            Intent GotoShoppingList=new Intent(ShoppingList.this,Categories.class);
            GotoShoppingList.putExtra("user_name",UserNameFromCat);
            startActivity(GotoShoppingList);
        }

        return true;

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater=new MenuInflater(this);
        inflater.inflate(R.menu.secmenufordel,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String SelectedItemFromSl=((TextView)info.targetView).getText().toString();
        String [] DetailsOfSLItems=SelectedItemFromSl.split("/");
        String DeletedItem=DetailsOfSLItems[0];
        if(item.getItemId()==R.id.DeleteFromSL)
        {

            SumOfPrices-=Float.parseFloat(DetailsOfSLItems[1]);
            LastResultOfSummion.setText(String.valueOf("Total: "+SumOfPrices));

            Cursor CursorSearch=Obj.searchProducts(DeletedItem);
            int NewQuanTity=CursorSearch.getInt(2);
            NewQuanTity+=Integer.parseInt(DetailsOfSLItems[2]);
            Obj.proUpdate(DeletedItem,CursorSearch.getFloat(1),NewQuanTity);
            Obj.removeFromShoppingList(UserNameFromCat,DeletedItem);



            ShoppingAdapter.remove(DeletedItem);
            //TmpCur=Obj.searchProducts(DeletedItem);



            //Dispaly
            ShoppingAdapter.clear();
            GetShoppingListItemsCurrsor=Obj.getAllItems(UserNameFromCat);

            if(GetShoppingListItemsCurrsor!=null)
            {

                while (!GetShoppingListItemsCurrsor.isAfterLast())
                {
                    ShoppingAdapter.add(GetShoppingListItemsCurrsor.getString(0)+"/"+GetShoppingListItemsCurrsor.getString(1)+"/"+GetShoppingListItemsCurrsor.getString(2));
                    GetShoppingListItemsCurrsor.moveToNext();
                }
            }
            else
            {
                CheckList=false;
                Toast.makeText(getApplicationContext(),"No Items",Toast.LENGTH_LONG).show();
            }
        }
        else if(R.id.IncSL==item.getItemId()) {
            Cursor CursorSearch = Obj.searchProducts(DeletedItem);
            int NewQuanTity = CursorSearch.getInt(2);
            if (NewQuanTity != 0)
            {
                Obj.addToShoppingList(UserNameFromCat, DeletedItem, Float.parseFloat(CursorSearch.getString(1)));

            Float TmpPri = CursorSearch.getFloat(1);
            SumOfPrices += TmpPri;
            LastResultOfSummion.setText(String.valueOf("Total: " + SumOfPrices));

            NewQuanTity  -= 1;
            Obj.proUpdate(DeletedItem, CursorSearch.getFloat(1), NewQuanTity);
            ShoppingAdapter.clear();

            GetShoppingListItemsCurrsor = Obj.getAllItems(UserNameFromCat);

            if (GetShoppingListItemsCurrsor != null) {

                while (!GetShoppingListItemsCurrsor.isAfterLast()) {
                    ShoppingAdapter.add(GetShoppingListItemsCurrsor.getString(0) + "/" + GetShoppingListItemsCurrsor.getString(1) + "/" + GetShoppingListItemsCurrsor.getString(2));
                    GetShoppingListItemsCurrsor.moveToNext();
                }
            } else {
                CheckList = false;
                Toast.makeText(getApplicationContext(), "No Items", Toast.LENGTH_LONG).show();
            }
        }
        else
            {
                Toast.makeText(getApplicationContext(),"The Quantity Ended From Store",Toast.LENGTH_LONG).show();
            }

        }
        else if(R.id.DecSL==item.getItemId())
        {
            int SLQuan=Integer.parseInt(DetailsOfSLItems[2]);
            SLQuan-=1;
            if(SLQuan==0)
            {
                ShoppingAdapter.remove(DeletedItem);
                Obj.removeFromShoppingList(UserNameFromCat,DeletedItem);


                Cursor CursorSearch=Obj.searchProducts(DeletedItem);
                Float TmpPri=CursorSearch.getFloat(1);
                SumOfPrices-=TmpPri;
                LastResultOfSummion.setText(String.valueOf("Total: "+SumOfPrices));

                int NewQuanTity=CursorSearch.getInt(2);
                NewQuanTity+=1;
                Obj.proUpdate(DeletedItem,CursorSearch.getFloat(1),NewQuanTity);

                ShoppingAdapter.clear();

                GetShoppingListItemsCurrsor = Obj.getAllItems(UserNameFromCat);

                if (GetShoppingListItemsCurrsor != null) {

                    while (!GetShoppingListItemsCurrsor.isAfterLast()) {
                        ShoppingAdapter.add(GetShoppingListItemsCurrsor.getString(0) + "/" + GetShoppingListItemsCurrsor.getString(1) + "/" + GetShoppingListItemsCurrsor.getString(2));
                        GetShoppingListItemsCurrsor.moveToNext();
                    }
                } else {
                    CheckList = false;
                    Toast.makeText(getApplicationContext(), "No Items", Toast.LENGTH_LONG).show();
                }
            }
            else {


                Cursor CursorSearch = Obj.searchProducts(DeletedItem);

                Float TmpPri = CursorSearch.getFloat(1);
                Float Price=Float.parseFloat(DetailsOfSLItems[1]);
                Price-=TmpPri;
                Obj.EditQu(UserNameFromCat, DetailsOfSLItems[0], Price, SLQuan);
                SumOfPrices -= TmpPri;
                LastResultOfSummion.setText(String.valueOf("Total: " + SumOfPrices));

                int NewQuanTity = CursorSearch.getInt(2);
                NewQuanTity += 1;
                Obj.proUpdate(DeletedItem, CursorSearch.getFloat(1), NewQuanTity);


                ShoppingAdapter.clear();

                GetShoppingListItemsCurrsor = Obj.getAllItems(UserNameFromCat);

                if (GetShoppingListItemsCurrsor != null) {

                    while (!GetShoppingListItemsCurrsor.isAfterLast()) {
                        ShoppingAdapter.add(GetShoppingListItemsCurrsor.getString(0) + "/" + GetShoppingListItemsCurrsor.getString(1) + "/" + GetShoppingListItemsCurrsor.getString(2));
                        GetShoppingListItemsCurrsor.moveToNext();
                    }
                } else {
                    CheckList = false;
                    Toast.makeText(getApplicationContext(), "No Items", Toast.LENGTH_LONG).show();
                }
            }
        }


        return super.onContextItemSelected(item);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure_button();
                break;
            default:
                break;
        }

    }
    void configure_button(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        AddOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission
                locationManager.requestLocationUpdates("gps", 5000, 0, listener);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        CheckList=true;
        UserNameFromCat=getIntent().getStringExtra("user_name");
        ShoppingListDis=(ListView)findViewById(R.id.ShoppingListID);
        Obj=new FCISShoppingDB(this);
        ShoppingAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        ShoppingListDis.setAdapter(ShoppingAdapter);
        registerForContextMenu(ShoppingListDis);
        GetShopinButton=(Button)findViewById(R.id.DisShop);
        LastResultOfSummion=(TextView)findViewById(R.id.TotalOrderID);
        AddOrderButton=(ImageButton)findViewById(R.id.AddOrderToDbID);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LocationString="Latitude: "+location.getLatitude()+"/Longitude: "+location.getLongitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        AddOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetUserId=Obj.getUserDetails(UserNameFromCat);
                String DateOfToday = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                GetShoppingListItemsCurrsor=Obj.getAllItems(UserNameFromCat);


                if(GetShoppingListItemsCurrsor!=null)
                {

                    Long OID=Obj.addOrder(UserNameFromCat,DateOfToday,LocationString);
                    GetShoppingListItemsCurrsor.moveToFirst();
                    while(!GetShoppingListItemsCurrsor.isAfterLast())
                    {
                        Obj.addinOrderDetail(UserNameFromCat,OID,GetShoppingListItemsCurrsor.getString(0));
                        GetShoppingListItemsCurrsor.moveToNext();
                    }
                    Obj.RemoveAllItemsinSLToSpecificUser(UserNameFromCat);
                    ShoppingAdapter.clear();
                    SumOfPrices=0;
                    LastResultOfSummion.setText(String.valueOf((SumOfPrices)));
                    Toast.makeText(getApplicationContext(),"Order Added Successfully & It's Details",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"No Item To Buy",Toast.LENGTH_LONG).show();
                }







            }
        });
        SumOfPrices=0;
        GetShopinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetShoppingListItemsCurrsor=Obj.getAllItems(UserNameFromCat);
                SumOfPrices=0;
                if(GetShoppingListItemsCurrsor!=null)
                {

                    ShoppingAdapter.clear();
                    while (!GetShoppingListItemsCurrsor.isAfterLast())
                    {
                        ShoppingAdapter.add(GetShoppingListItemsCurrsor.getString(0)+"/"+GetShoppingListItemsCurrsor.getString(1)+"/"+GetShoppingListItemsCurrsor.getString(2));

                        SumOfPrices+=GetShoppingListItemsCurrsor.getFloat(1);
                        GetShoppingListItemsCurrsor.moveToNext();
                    }
                    LastResultOfSummion.setText("Total :"+String.valueOf(SumOfPrices));
                }
                else
                {
                    CheckList=false;
                    Toast.makeText(getApplicationContext(),"No Items",Toast.LENGTH_LONG).show();
                }

            }
        });



    }
}
