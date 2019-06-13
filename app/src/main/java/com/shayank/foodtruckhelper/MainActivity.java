package com.shayank.foodtruckhelper;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import com.shawnlin.numberpicker.NumberPicker;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    RecyclerView recyclerView;
    private ArrayList<String> mItemNames = new ArrayList<>();
    private ArrayList<String> mItemPrices = new ArrayList<>();
    MenuRecyclerViewAdapter menuRecyclerViewAdapter;

    EditText newItemName;
    EditText newItemPrice;
    Button newItemAdd;

    TextView totalText;
    Button clearButton;
    ToggleButton addTaxButton;

    private BigDecimal totalNoTax;
    private BigDecimal taxRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.menu);
        menuRecyclerViewAdapter = new MenuRecyclerViewAdapter(mItemNames, mItemPrices,this);
        recyclerView.setAdapter(menuRecyclerViewAdapter);
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

        totalNoTax = new BigDecimal(0);
        totalNoTax = totalNoTax.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        taxRate = new BigDecimal(1.13);
        taxRate = taxRate.setScale(2, BigDecimal.ROUND_HALF_EVEN);

        addTaxButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    totalText.setText("Total: $" + totalNoTax.multiply(taxRate).setScale(2, RoundingMode.HALF_EVEN));
                } else {
                    totalText.setText("Total: $" + totalNoTax);
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(NumberPicker picker: menuRecyclerViewAdapter.getItemPickers()) {
                    picker.setValue(0);
                }
                totalNoTax = totalNoTax.multiply(BigDecimal.ZERO);
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
        menuRecyclerViewAdapter.notifyItemInserted(mItemNames.size() - 1);
        newItemName.setText("");
        newItemPrice.setText("");
    }

    public void calculate() {
        Log.d(TAG, "calculate: " + mItemNames.size());
        totalNoTax = totalNoTax.multiply(BigDecimal.ZERO);
        for(int i = 0; i < mItemNames.size(); i++) {
            BigDecimal price = new BigDecimal(mItemPrices.get(i));
            price = price.setScale(2, BigDecimal.ROUND_HALF_EVEN);
            BigDecimal quantity = new BigDecimal(menuRecyclerViewAdapter.getItemPickers().get(i).getValue());
            quantity = quantity.setScale(0, BigDecimal.ROUND_HALF_EVEN);
            Log.d(TAG, "calculate: " + price);
            Log.d(TAG, "calculate: " + quantity);
            totalNoTax = totalNoTax.add(price.multiply(quantity));
        }
        if (addTaxButton.isChecked()) {
            totalText.setText("Total: $" + totalNoTax.multiply(taxRate).setScale(2, RoundingMode.HALF_EVEN));
        } else {
            totalText.setText("Total: $" + totalNoTax);
        }
    }
}
