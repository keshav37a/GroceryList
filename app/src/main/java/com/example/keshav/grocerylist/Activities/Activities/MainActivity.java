package com.example.keshav.grocerylist.Activities.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.example.keshav.grocerylist.Activities.Data.DatabaseHandler;
import com.example.keshav.grocerylist.Activities.Model.Grocery;
import com.example.keshav.grocerylist.R;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText groceryItem;
    private EditText quantity;
    private Button saveButton;
    private DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                createPopUpDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        byPassActivity();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void createPopUpDialog()
    {
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        groceryItem = view.findViewById(R.id.groceryItem);
        quantity = view.findViewById(R.id.groceryQty);
        saveButton = view.findViewById(R.id.saveButton);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save to db
                if(!groceryItem.getText().toString().isEmpty()
                        && !quantity.getText().toString().isEmpty())
                {
                    saveGroceryToDb(v);
                }

                //go to next screen

                saveGroceryToDb(v);
            }
        });
    }
    public void saveGroceryToDb(View v)
    {
        Grocery grocery = new Grocery();
        grocery.setName(groceryItem.getText().toString());
        grocery.setQuantity(quantity.getText().toString());

        //save to db
       db.addGroceryItem(grocery);
       Snackbar.make(v, "Item saved", Snackbar.LENGTH_LONG).show();
       Log.i("Item id : ", String.valueOf(db.getGroceriesCount()));

       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
                dialog.dismiss();

                //Start a new activity

               startActivity(new Intent(MainActivity.this, ListActivity.class));
           }
       }, 1000);   //1 second delay
    }
    public void byPassActivity()
    {
        //Check if database is empty; if not then we just go back to
        //listactivity and show all added items
        if(db.getGroceriesCount()>0)
        {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }
    }
}
