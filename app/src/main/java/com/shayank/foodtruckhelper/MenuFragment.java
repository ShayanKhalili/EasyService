package com.shayank.foodtruckhelper;

import android.os.Bundle;
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

public class MenuFragment extends Fragment {
    private static final String TAG = "MenuFragment";

    RecyclerView menuRecyclerView;
    MenuRecyclerViewAdapter menuRecyclerViewAdapter;

    EditText newItemName;
    EditText newItemPrice;
    Button newItemAdd;

    TextView totalText;
    Button clearButton;
    ToggleButton addTaxButton;

    EditText customerNameEditText;
    Button newOrderAddButton;

    MainActivity mainActivity;

    public MenuFragment(MenuRecyclerViewAdapter menuRecyclerViewAdapter, MainActivity mainActivity) {
        this.menuRecyclerViewAdapter = menuRecyclerViewAdapter;
        this.mainActivity = mainActivity;
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
        menuRecyclerView.setAdapter(menuRecyclerViewAdapter);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        newItemName = view.findViewById(R.id.new_item_name);
        newItemPrice = view.findViewById(R.id.new_item_price);
        newItemAdd = view.findViewById(R.id.new_item_add_button);

        customerNameEditText = view.findViewById(R.id.order_name_edit_text);
        newOrderAddButton = view.findViewById(R.id.order_add_button);

        newItemAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewMenuItem();
            }
        });

        totalText = view.findViewById(R.id.total_text);
        addTaxButton = view.findViewById(R.id.add_tax_button);
        clearButton = view.findViewById(R.id.clear_button);

        addTaxButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    totalText.setText("Total: $" + mainActivity.getTotalNoTax().multiply(mainActivity.getTaxRate()).setScale(2, RoundingMode.HALF_EVEN));
                } else {
                    totalText.setText("Total: $" + mainActivity.getTotalNoTax());
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.clearFields();
                totalText.setText("Total: $0.00");
                customerNameEditText.setText("");
            }
        });

        newOrderAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean success = mainActivity.addOrder(customerNameEditText.getText().toString());
                if (success) Toast.makeText(getContext(), "Order added successfully", Toast.LENGTH_SHORT).show();
                else Toast.makeText(getContext(), "Order is empty.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createNewMenuItem() {
        mainActivity.addNewMenuItem(newItemName.getText().toString(), newItemPrice.getText().toString().equals("") ? "0" : newItemPrice.getText().toString());
        newItemName.setText("");
        newItemPrice.setText("");
    }

    public void setTotalText(String text) {
        totalText.setText(text);
    }

    public boolean addTaxChecked() {
        return addTaxButton.isChecked();
    }
}
