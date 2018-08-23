package com.example.keshav.grocerylist.Activities.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.keshav.grocerylist.R;


public class DetailsActivity extends AppCompatActivity {

    private TextView itemNameDet;
    private TextView itemQuantityDet;
    private TextView dateAddedDet;
    private int groceryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        itemNameDet = findViewById(R.id.itemNameDet);
        itemQuantityDet = findViewById(R.id.itemQuantityDet);
        dateAddedDet = findViewById(R.id.dateAddedDet);

        Bundle bundle = getIntent().getExtras();

        if(bundle!=null)
        {
            itemNameDet.setText(bundle.getString("Name"));
            //Log.i("test", bundle.getString("Name"));
            itemQuantityDet.setText(bundle.getString("Quantity"));
            dateAddedDet.setText(bundle.getString("DateAdded"));
            groceryId = bundle.getInt("Id");
        }
    }


}
