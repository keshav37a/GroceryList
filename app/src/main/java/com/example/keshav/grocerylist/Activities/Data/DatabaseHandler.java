package com.example.keshav.grocerylist.Activities.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.keshav.grocerylist.Activities.Model.Grocery;
import com.example.keshav.grocerylist.Activities.Util.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper
{

    SQLiteDatabase sqLiteDatabase;

    private Context ctx;
    public DatabaseHandler(Context context)
    {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.sqLiteDatabase = sqLiteDatabase;
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_GROCERY_TABLE = "CREATE TABLE "+ Constants.TABLE_NAME + " ("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY, "
                + Constants.KEY_GROCERY_ITEM + " TEXT, "
                + Constants.KEY_QTY_NUMBER + " INTEGER, "
                + Constants.KEY_DATE_NAME + " LONG);";

        db.execSQL(CREATE_GROCERY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }

    /**
     * CRUD OPERATIONS: CREATE, READ, UPDATE, DELETE
     */

    //ADD GROCERY
    public void addGroceryItem(Grocery grocery)
    {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.KEY_GROCERY_ITEM, grocery.getName());
        contentValues.put(Constants.KEY_QTY_NUMBER, grocery.getQuantity());
        contentValues.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());

        sqLiteDatabase.insert(Constants.TABLE_NAME, null, contentValues);

    }

    //GET A GROCERY ITEM
    public Grocery getGroceryItem(int id)
    {
        sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(Constants.TABLE_NAME, new String[]
                        {Constants.KEY_ID, Constants.KEY_GROCERY_ITEM, Constants.KEY_QTY_NUMBER, Constants.KEY_DATE_NAME}
                        , Constants.KEY_ID + "=?", new String[] {String.valueOf(id)}, null, null, null);

        if(cursor!=null)
            cursor.moveToFirst();

            Grocery grocery = new Grocery();
            grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
            grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
            grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));

            //Convert timestamp to something readable
            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
            String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());
            grocery.setDateItemAdded(formattedDate);

            return grocery;
    }
    //GET ALL GROCERIES
    public List<Grocery> getAllGroceries()
    {
        sqLiteDatabase = this.getReadableDatabase();
        List<Grocery> groceryList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(Constants.TABLE_NAME, new String []
                {Constants.KEY_ID, Constants.KEY_GROCERY_ITEM, Constants.KEY_QTY_NUMBER, Constants.KEY_DATE_NAME},
                null, null, null, null, Constants.KEY_DATE_NAME + " DESC");

        if(cursor.moveToFirst())
        {
            do {
                Grocery grocery = new Grocery();
                grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
                grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));

                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());
                grocery.setDateItemAdded(formattedDate);

                groceryList.add(grocery);
            }
            while(cursor.moveToNext());
        }
        return groceryList;
    }

    //UPDATE GROCERY ITEM
    public int updateGroceryItem(Grocery grocery)
    {
        sqLiteDatabase = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.KEY_ID, grocery.getId());
        contentValues.put(Constants.KEY_GROCERY_ITEM, grocery.getName());
        contentValues.put(Constants.KEY_QTY_NUMBER, grocery.getQuantity());
        contentValues.put(Constants.KEY_DATE_NAME, grocery.getDateItemAdded());

        return sqLiteDatabase.update(Constants.TABLE_NAME, contentValues, Constants.KEY_ID + "=?",
                new String [] {String.valueOf(grocery.getId())});
    }

    //DELETE GROCERY ITEM
    public void deleteGroceryItem(int id)
    {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(Constants.TABLE_NAME, Constants.KEY_ID + "=?", new String []
                {String.valueOf(id)});
        sqLiteDatabase.close();
    }

    //COUNT GROCERY ITEMS
    public int getGroceriesCount()
    {
        sqLiteDatabase = this.getReadableDatabase();
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        return cursor.getCount();
    }
}
