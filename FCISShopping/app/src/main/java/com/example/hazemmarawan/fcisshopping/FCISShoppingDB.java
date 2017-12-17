package com.example.hazemmarawan.fcisshopping;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by HazemMarawan on 11/30/2017.
 */

public class FCISShoppingDB extends SQLiteOpenHelper {

    private static String DBName="FCISSHOPPING";
    private SQLiteDatabase e_commerce_db;
    public FCISShoppingDB(Context context) {
        super(context, DBName, null, 10);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE CUSTOMERS (CUST_ID INTEGER PRIMARY KEY AUTOINCREMENT ,CUST_NAME TEXT NOT NULL,USER_NAME TEXT NOT NULL,EMAIL TEXT NOT NULL,PASSWORD TEXT  NOT NULL,GENDER TEXT NOT NULL,BIRTHDATE TEXT NOT NULL,JOB TEXT NOT NULL,QUESTION TEXT NOT NULL,ANSWER TEXT NOT NULL)");

        db.execSQL("CREATE TABLE ORDERS (ORD_ID INTEGER PRIMARY KEY AUTOINCREMENT,ORD_DATE TEXT NOT NULL,CUST_ID INTEGER NOT NULL,ADDRESS TEXT NOT NULL,FOREIGN KEY(CUST_ID) REFERENCES CUSTOMERS(CUST_ID))");

        db.execSQL("CREATE TABLE CATEGORIES (CAT_ID INTEGER PRIMARY KEY AUTOINCREMENT,CAT_NAME TEXT NOT NULL)");

        db.execSQL("CREATE TABLE PRODUCTS (PROD_ID INTEGER PRIMARY KEY AUTOINCREMENT,PROD_NAME TEXT NOT NULL,PRICE FLOAT NOT NULL,QUANTITY INTEGER NOT NULL,CAT_ID INTEGER NOT NULL,FOREIGN KEY(CAT_ID) REFERENCES CATEGORIES(CAT_ID))");

        db.execSQL("CREATE TABLE SHOPPING_LIST (SHPL_LIST_ID INTEGER PRIMARY KEY AUTOINCREMENT ,USER_NAME TEXT NOT NULL,NAME TEXT NOT NULL,PRICE FLOAT NOT NULL,QUANTITY INTEGER NOT NULL) ");

        db.execSQL("CREATE TABLE ORDER_DETAILS (ORD_ID INTEGER NOT NULL,PROD_ID INTEGER NOT NULL,QUANTITY INTEGER NOT NULL,PRIMARY KEY(ORD_ID,PROD_ID),FOREIGN KEY(ORD_ID) REFERENCES ORDERS(ORD_ID),FOREIGN KEY(PROD_ID) REFERENCES PRODUCTS(PROD_ID))");
//


        ContentValues category=new ContentValues();

        category.put("CAT_NAME","Mobiles");
        db.insert("CATEGORIES", null, category);

        category=new ContentValues();
        category.put("CAT_NAME", "Labtops");
        db.insert("CATEGORIES", null, category);

        category=new ContentValues();
        category.put("CAT_NAME", "TV");
        db.insert("CATEGORIES", null, category);




        ContentValues product=new ContentValues();
        //Mobiles
        product.put("PROD_NAME","IPhone");
        product.put("PRICE",8000);
        product.put("QUANTITY",3);
        product.put("CAT_ID",1);
        db.insert("PRODUCTS", null, product);

        product=new ContentValues();
        product.put("PROD_NAME","Microsoft");
        product.put("PRICE",2000);
        product.put("QUANTITY",100);
        product.put("CAT_ID",1);
        db.insert("PRODUCTS", null, product);


        //Labtops
        product=new ContentValues();
        product.put("PROD_NAME","DELL");
        product.put("PRICE",4000);
        product.put("QUANTITY",100);
        product.put("CAT_ID",2);
        db.insert("PRODUCTS", null, product);

        product=new ContentValues();
        product.put("PROD_NAME","HP");
        product.put("PRICE",5000);
        product.put("QUANTITY",100);
        product.put("CAT_ID",2);
        db.insert("PRODUCTS", null, product);


        //TVs
        product=new ContentValues();
        product.put("PROD_NAME","LG");
        product.put("PRICE",3500);
        product.put("QUANTITY",16);
        product.put("CAT_ID",3);
        db.insert("PRODUCTS",null,product);

        product=new ContentValues();
        product.put("PROD_NAME","Toshiba");
        product.put("PRICE",4000);
        product.put("QUANTITY",16);
        product.put("CAT_ID",3);
        db.insert("PRODUCTS",null,product);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS ORDER_DETAILS ");
        db.execSQL("DROP TABLE IF EXISTS ORDERS ");
        db.execSQL("DROP TABLE IF EXISTS CUSTOMERS ");
        db.execSQL("DROP TABLE IF EXISTS PRODUCTS");
        db.execSQL("DROP TABLE IF EXISTS CATEGORIES ");
        db.execSQL("DROP TABLE IF EXISTS SHOPPING_LIST ");
        onCreate(db);
    }
    public boolean logIn(String user_name,String password){
        e_commerce_db=getReadableDatabase();
        String[] login_parameters={user_name,password};
        Cursor user=e_commerce_db.rawQuery("SELECT CUST_ID FROM CUSTOMERS WHERE USER_NAME=? AND PASSWORD=?", login_parameters);

        if(user.getCount() != 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean signUp(String customer_name,String user_name,String email,String password,String gender,String birthdate,String job,String Ques,String Ans){
        e_commerce_db=getWritableDatabase();
        ContentValues new_user=new ContentValues();
        new_user.put("CUST_NAME",customer_name);
        new_user.put("USER_NAME",user_name);
        new_user.put("EMAIL", email);
        new_user.put("PASSWORD", password);
        new_user.put("GENDER", gender);
        new_user.put("BIRTHDATE", birthdate);
        new_user.put("JOB", job);
        new_user.put("QUESTION", Ques);
        new_user.put("ANSWER", Ans);

        long user_id=e_commerce_db.insert("CUSTOMERS", null, new_user);
        if(user_id!=-1)
            return true;
        else
            return false;
    }
    public Cursor ProductsByCategory(String name) {
        e_commerce_db = getReadableDatabase();
        String[] search_parameters={name};
        Cursor products = e_commerce_db.rawQuery("SELECT PROD_NAME ,PRICE,QUANTITY FROM PRODUCTS WHERE CAT_ID= (SELECT CAT_ID FROM CATEGORIES WHERE CAT_NAME=?)",search_parameters);
        products.moveToFirst();
        if (products.getCount() > 0) {
            return products;
        }
        return null;
    }
    public boolean addToShoppingList(String user_name,String ItemName,float ItemPrice) {
        e_commerce_db=getWritableDatabase();


        if(searchifFound(ItemName)==false)
        {
            ContentValues shopping_lsit_item = new ContentValues();
            shopping_lsit_item.put("USER_NAME", user_name);
            shopping_lsit_item.put("NAME", ItemName);
            shopping_lsit_item.put("PRICE", ItemPrice);
            shopping_lsit_item.put("QUANTITY", 1);
            long id = e_commerce_db.insert("SHOPPING_LIST", null, shopping_lsit_item);
            if (id != -1) {
                return true;
            }

        }

        else {
            Cursor Quan=searchifFound2(ItemName);
            int QuanLast=Quan.getInt(0);
            QuanLast+=1;
            Float LastPrice=QuanLast*ItemPrice;
            EditQu(user_name,ItemName,LastPrice,QuanLast);



        }

        return false;
    }
    public int getUserDetails(String user_name){
        e_commerce_db=getReadableDatabase();
        String[] search_parameters={user_name};
        Cursor user=e_commerce_db.rawQuery("SELECT * FROM CUSTOMERS WHERE USER_NAME=?",search_parameters);
        user.moveToFirst();
        if(user.getCount()==1){
            return user.getInt(0);
        }
        return -1;
    }
    public long addOrder(String username, String order_date,String address )
    {
        e_commerce_db=getWritableDatabase();

        Cursor user=e_commerce_db.rawQuery("SELECT CUST_ID FROM CUSTOMERS WHERE USER_NAME=?", new String[]{username});
        if(user.getCount()==1){
            user.moveToFirst();
            int cust_id=user.getInt(0);
            ContentValues order=new ContentValues();
            order.put("ORD_DATE",order_date);
            order.put("CUST_ID",cust_id);
            order.put("ADDRESS",address);
            long order_id=e_commerce_db.insert("ORDERS",null,order);

            return order_id;

        }

        return -1;
    }
    public boolean addinOrderDetail(String username,long OrderFromPrevFun,String ItemNameFromSL)
    {
        e_commerce_db=getWritableDatabase();
        Cursor product=e_commerce_db.rawQuery("SELECT PROD_ID,QUANTITY FROM PRODUCTS WHERE PROD_NAME=?",new String[]{ItemNameFromSL});
        if(product.getCount()==1){
            product.moveToFirst();
            int ProdFromCur=product.getInt(0);
            int QuantityFromCur=product.getInt(1);

            ContentValues DetailsdOFOrder=new ContentValues();
            DetailsdOFOrder.put("ORD_ID",OrderFromPrevFun);
            DetailsdOFOrder.put("PROD_ID",ProdFromCur);
            DetailsdOFOrder.put("QUANTITY",QuantityFromCur);
            e_commerce_db.insert("ORDER_DETAILS",null,DetailsdOFOrder);


            return true;

        }
        return false;
    }
    public boolean RemoveAllItemsinSLToSpecificUser(String username)
    {
        e_commerce_db=getWritableDatabase();
        e_commerce_db.delete("SHOPPING_LIST","USER_NAME=?",new String[]{username});
        return true;

    }


    public boolean removeFromShoppingList(String user_name,String ItemName){
        e_commerce_db=getWritableDatabase();
        int rows=e_commerce_db.delete("SHOPPING_LIST", "USER_NAME=? AND NAME=?", new String[]{user_name, ItemName});
        if (rows>0){
            return true;
        }
        return false;
    }
    public Cursor getAllItems(String user_name){
        e_commerce_db=getReadableDatabase();
        Cursor items=e_commerce_db.rawQuery("SELECT NAME,PRICE,QUANTITY FROM SHOPPING_LIST WHERE USER_NAME=?", new String[]{user_name});
        if(items.getCount()>0) {
            items.moveToFirst();
            return items;
        }
        return null;
    }
    public int GetItemFromSL(String user_name,String itemName){
        e_commerce_db=getReadableDatabase();
        Cursor items=e_commerce_db.rawQuery("SELECT QUANTITY FROM SHOPPING_LIST WHERE USER_NAME=? AND NAME=?", new String[]{user_name,itemName});

        if(items.getCount()==0) {
            return items.getInt(0);
        }
        return 0;
    }

    public Cursor searchProducts(String name) {
        e_commerce_db = getReadableDatabase();
        String[] search_paramates = {"%" + name + "%"};
        Cursor products = e_commerce_db.rawQuery("SELECT PROD_NAME,PRICE,QUANTITY FROM PRODUCTS WHERE PROD_NAME LIKE ?", search_paramates);
        products.moveToFirst();
        if (products.getCount() > 0) {
            return products;
        }
        return null;
    }
    public String getUserPassword(String email,String Ques,String Ans){

        e_commerce_db=getReadableDatabase();
        String[] search_parameters={email,Ques,Ans};
        Cursor password=e_commerce_db.rawQuery("SELECT PASSWORD FROM CUSTOMERS WHERE EMAIL=? AND QUESTION=? AND ANSWER=?",search_parameters);
        password.moveToFirst();
        if(password.getCount()>0) {
            return password.getString(0);
        }
        return "NoPass";
    }
    public boolean searchifFound(String itemName) {
        e_commerce_db = getReadableDatabase();
        Cursor fo= e_commerce_db.rawQuery("SELECT NAME FROM SHOPPING_LIST WHERE NAME LIKE  ?", new String[]{itemName});
        fo.moveToFirst();
        if(fo.getCount()>0)
        {
            return true;
        }

        return false;
    }
    public Cursor searchifFound3(String itemName) {
        e_commerce_db = getReadableDatabase();
        Cursor fo= e_commerce_db.rawQuery("SELECT PRICE FROM SHOPPING_LIST WHERE NAME LIKE  ?", new String[]{itemName});
        fo.moveToFirst();
        if(fo.getCount()>0)
        {
            return fo;
        }

        return null;
    }

    public Cursor searchifFound2(String itemName) {
        e_commerce_db = getReadableDatabase();
        Cursor fo= e_commerce_db.rawQuery("SELECT QUANTITY FROM SHOPPING_LIST WHERE NAME LIKE  ?", new String[]{itemName});
        fo.moveToFirst();
        if (fo.getCount() > 0) {
            return fo;
        }
        return null;
    }

    public boolean EditQu(String user_name,String itemName,Float Price,int Qu) {
        e_commerce_db=getWritableDatabase();
        String[] update_parameters={String.valueOf(itemName)};
        ContentValues shopping_lsit_item=new ContentValues();
        shopping_lsit_item.put("USER_NAME",user_name);
        shopping_lsit_item.put("NAME",itemName);
        shopping_lsit_item.put("PRICE",Price);
        shopping_lsit_item.put("QUANTITY",Qu);
        int result= e_commerce_db.update("SHOPPING_LIST",shopping_lsit_item,"NAME=?",update_parameters);

        return result==1;


    }
    public boolean proUpdate(String name,Float Price,int Quan) {
        e_commerce_db=getWritableDatabase();
        String[] update_parameters={String.valueOf(name)};
        ContentValues pro=new ContentValues();
        pro.put("PROD_NAME",name);
        pro.put("PRICE",Price);
        pro.put("QUANTITY",Quan);
        int res=e_commerce_db.update("PRODUCTS",pro,"PROD_NAME=?",update_parameters);

        return res==1;

    }


}
