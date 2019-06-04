package com.shayank.foodtruckhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private ArrayList<String> mItemNames = new ArrayList<>();
    private ArrayList<String> mItemPrices = new ArrayList<>();
    RecyclerViewAdapter recyclerViewAdapter;

    EditText newItemName;
    EditText newItemPrice;
    Button newItemAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.menu);
        recyclerViewAdapter = new RecyclerViewAdapter(mItemNames, mItemPrices, this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        newItemName = findViewById(R.id.new_item_name);
        newItemPrice = findViewById(R.id.new_item_price);
        newItemAdd = findViewById(R.id.new_item_add);

        newItemAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemNames.add(newItemName.getText().toString());
                mItemPrices.add(newItemPrice.getText().toString());
                recyclerViewAdapter.notifyItemInserted(mItemNames.size() - 1);
            }
        });
    }

    public void calculate() {

    }
}
