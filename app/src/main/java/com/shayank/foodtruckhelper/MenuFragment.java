package com.shayank.foodtruckhelper;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shawnlin.numberpicker.NumberPicker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class MenuFragment extends Fragment {
    private static final String TAG = "MenuFragment";

    RecyclerView menuRecyclerView;
    private ArrayList<String> mItemNames = new ArrayList<>();
    private ArrayList<String> mItemPrices = new ArrayList<>();
    private ArrayList<NumberPicker> mItemPickers = new ArrayList<>();
    MenuRecyclerViewAdapter menuRecyclerViewAdapter;

    private BigDecimal totalNoTax;
    private BigDecimal taxRate;

    EditText newItemName;
    EditText newItemPrice;
    Button newItemAdd;

    TextView totalText;
    Button clearButton;
    ToggleButton addTaxButton;

    EditText newOrderEditText;
    Button newOrderAddButton;

    OrdersFragment ordersFragment;

    public MenuFragment(OrdersFragment ordersFragment) {
        this.ordersFragment = ordersFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        menuRecyclerView = view.findViewById(R.id.menu);
        menuRecyclerViewAdapter = new MenuRecyclerViewAdapter(mItemNames, mItemPrices, mItemPickers, getActivity(), this);
        menuRecyclerView.setAdapter(menuRecyclerViewAdapter);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        totalNoTax = new BigDecimal(0);
        totalNoTax = totalNoTax.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        taxRate = new BigDecimal(1.13);
        taxRate = taxRate.setScale(2, BigDecimal.ROUND_HALF_EVEN);

        newItemName = view.findViewById(R.id.new_item_name);
        newItemPrice = view.findViewById(R.id.new_item_price);
        newItemAdd = view.findViewById(R.id.new_item_add_button);

        newOrderEditText = view.findViewById(R.id.order_name_edit_text);
        newOrderAddButton = view.findViewById(R.id.order_add_button);

        newItemAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewItem();
            }
        });

        totalText = view.findViewById(R.id.total_text);
        addTaxButton = view.findViewById(R.id.add_tax_button);
        clearButton = view.findViewById(R.id.clear_button);

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

        newOrderAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder orderDetails = new StringBuilder("");
                for (int i = 0; i < mItemNames.size(); i++) {
                    if (mItemPickers.get(i).getValue() != 0){
                        orderDetails.append(mItemPickers.get(i).getValue() + " " + mItemNames.get(i) + ", ");
                    }
                }
                if (orderDetails.toString() != "") {
                    orderDetails.append("for " + newOrderEditText.getText());
                    ordersFragment.addOrder(orderDetails.toString(), false);
                    Toast.makeText(getContext(), "Order added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Order is empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void createNewItem() {
        mItemNames.add(newItemName.getText().toString());
        mItemPrices.add(newItemPrice.getText().toString());
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
            BigDecimal quantity = new BigDecimal(mItemPickers.get(i).getValue());
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
