package com.shayank.foodtruckhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    RecyclerView recyclerView;
    private ArrayList<String> mItemNames = new ArrayList<>();
    private ArrayList<String> mItemPrices = new ArrayList<>();
    RecyclerViewAdapter recyclerViewAdapter;

    EditText newItemName;
    EditText newItemPrice;
    Button newItemAdd;

    TextView totalText;
    Button clearButton;
    ToggleButton addTaxButton;

    private float totalNoTax = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.menu);
        recyclerViewAdapter = new RecyclerViewAdapter(mItemNames, mItemPrices,this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        newItemName = findViewById(R.id.new_item_name);
        newItemPrice = findViewById(R.id.new_item_price);
        newItemAdd = findViewById(R.id.new_item_add);

        newItemAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewItem();
            }
        });

        totalText = findViewById(R.id.total_text);
        addTaxButton = findViewById(R.id.add_tax_button);
        clearButton = findViewById(R.id.clear_button);

        addTaxButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    totalText.setText("Total: $" + totalNoTax * 1.13);
                } else {
                    totalText.setText("Total: $" + totalNoTax);
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(NumberPicker picker: recyclerViewAdapter.getItemPickers()) {
                    picker.setValue(0);
                }
                totalNoTax = 0;
                totalText.setText("Total: $0.00");
            }
        });
    }

    protected void createNewItem() {
        mItemNames.add(newItemName.getText().toString());
        mItemPrices.add(newItemPrice.getText().toString());
        NumberPicker newItemPicker = new NumberPicker(this);
        newItemPicker.setMinValue(0);
        newItemPicker.setMaxValue(100);
        newItemPicker.setValue(0);
        newItemPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                calculate();
            }
        });
        recyclerViewAdapter.notifyItemInserted(mItemNames.size() - 1);
    }

    public void calculate() {
        Log.d(TAG, "calculate: " + mItemNames.size());
        totalNoTax = 0;
        for(int i = 0; i < mItemNames.size(); i++) {
            float price = Float.parseFloat(mItemPrices.get(i));
            int quantity = recyclerViewAdapter.getItemPickers().get(i).getValue();
            Log.d(TAG, "calculate: " + price);
            Log.d(TAG, "calculate: " + quantity);
            totalNoTax += (price * quantity);
        }
        if (addTaxButton.isChecked()) {
            totalText.setText("Total: $" + totalNoTax * 1.13);
        } else {
            totalText.setText("Total: $" + totalNoTax);
        }
    }
}
