package com.example.keshav.grocerylist.Activities.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.keshav.grocerylist.Activities.Activities.DetailsActivity;
import com.example.keshav.grocerylist.Activities.Data.DatabaseHandler;
import com.example.keshav.grocerylist.Activities.Model.Grocery;
import com.example.keshav.grocerylist.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    private Context context;
    private List<Grocery> groceryItems;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private LayoutInflater layoutInflater;


    public RecyclerViewAdapter(Context context, List<Grocery> groceryItems) {
        this.context = context;
        this.groceryItems = groceryItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Grocery grocery = groceryItems.get(position);

        holder.groceryItemName.setText(grocery.getName());
        holder.quantity.setText(grocery.getQuantity());
        holder.dateAdded.setText(grocery.getDateItemAdded());
    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView groceryItemName;
        public TextView quantity;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        public int id;

        public ViewHolder(View view, Context ctx) {
            super(view);
            context = ctx;

            groceryItemName = view.findViewById(R.id.name);
            quantity = view.findViewById(R.id.qty);
            dateAdded = view.findViewById(R.id.dateAdded);

            editButton = view.findViewById(R.id.editButton);
            deleteButton = view.findViewById(R.id.deleteButton);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go to next screen DetailsActivity
                    int position = getAdapterPosition();
                    Grocery grocery = groceryItems.get(position);
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("Name ", grocery.getName());
                    intent.putExtra("Quantity ", grocery.getQuantity());
                    intent.putExtra("DateAdded ", grocery.getDateItemAdded());
                    intent.putExtra("Id ", grocery.getId());
                    context.startActivity(intent);
                }
            });

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int position = getAdapterPosition();
            Grocery grocery = groceryItems.get(position);
            switch(v.getId())
            {
                case R.id.editButton:
                    editItem(grocery);
                    break;

                case R.id.deleteButton:
                    deleteItem(grocery.getId());
                    break;
            }
        }

        public void editItem(final Grocery grocery)
        {
            alertDialogBuilder = new AlertDialog.Builder(context);
            layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.popup, null);

            final EditText groceryItem = view.findViewById(R.id.groceryItem);
            final EditText groceryQty = view.findViewById(R.id.groceryQty);
            TextView tile = view.findViewById(R.id.tile);

            tile.setText("Edit Grocery Item");
            Button saveButton = view.findViewById(R.id.saveButton);

            alertDialogBuilder.setView(view);
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Update
                    DatabaseHandler db = new DatabaseHandler(context);
                    grocery.setName(groceryItem.getText().toString());
                    grocery.setQuantity(groceryQty.getText().toString());

                    if(!grocery.getName().toString().isEmpty() && !grocery.getQuantity().toString().isEmpty()) {
                        db.updateGroceryItem(grocery);
                        notifyItemChanged(getAdapterPosition(), grocery);
                    }
                    else
                    {
                        Snackbar.make(v, "Add Grocery Item and Quantity", Snackbar.LENGTH_LONG).show();
                    }
                    alertDialog.dismiss();
                }
            });
        }

        public void deleteItem(final int id)
        {
            //Create alertbuilder dialog object
            alertDialogBuilder = new AlertDialog.Builder(context);
            layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.confirmation_dialog, null);

            Button yesButtonCD = view.findViewById(R.id.yesButtonCD);
            Button noButtonCD = view.findViewById(R.id.noButtonCD);

            alertDialogBuilder.setView(view);
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            noButtonCD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            yesButtonCD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler databaseHandler = new DatabaseHandler(context);
                    databaseHandler.deleteGroceryItem(id);
                    groceryItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    alertDialog.dismiss();
                }
            });
        }
    }
}

