package com.shayank.foodtruckhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shawnlin.numberpicker.NumberPicker;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    MenuFragment menuFragment;
    OrdersFragment ordersFragment;

    MenuRecyclerViewAdapter menuRecyclerViewAdapter;
    OrdersRecyclerViewAdapter ordersRecyclerViewAdapter;

    BottomNavigationView bottomNav;

    private ArrayList<String> mItemNames = new ArrayList<>();
    private ArrayList<String> mItemPrices = new ArrayList<>();
    private ArrayList<Integer> mItemCounts = new ArrayList<>();

    private ArrayList<Order> mOrderDetails = new ArrayList<>();
    private ArrayList<Boolean> mOrdersFulfilled = new ArrayList<>();

    private HashMap<String, BigDecimal> totalItemCount = new HashMap<>();

    private BigDecimal totalNoTax;
    private BigDecimal taxRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuRecyclerViewAdapter = new MenuRecyclerViewAdapter(mItemNames, mItemPrices, mItemCounts, this);
        menuFragment = new MenuFragment(menuRecyclerViewAdapter, this);
        ordersRecyclerViewAdapter = new OrdersRecyclerViewAdapter(mOrderDetails, mOrdersFulfilled, this);
        ordersFragment = new OrdersFragment(ordersRecyclerViewAdapter, this);


        totalNoTax = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        taxRate = new BigDecimal(1.13).setScale(2, BigDecimal.ROUND_HALF_EVEN);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                menuFragment).commit();

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.nav_food_menu:
                        selectedFragment = menuFragment;
                        break;
                    case R.id.nav_orders_list:
                        selectedFragment = ordersFragment;
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();

                return true;
            }
        });
    }

    protected void addNewMenuItem(String name, String price) {
        mItemNames.add(name);
        mItemPrices.add(price);
        menuRecyclerViewAdapter.notifyItemInserted(mItemNames.size() - 1);
    }

    public boolean addOrder(String customerName) {
        Order order = new Order(customerName);
        for (int i = 0; i < mItemNames.size(); i++) {
            if (mItemCounts.get(i) != 0){
                order.add(mItemNames.get(i), mItemPrices.get(i), mItemCounts.get(i));
            }
        }
        if (!order.isEmpty()) {
            mOrderDetails.add(order);
            mOrdersFulfilled.add(false);

            for (Map.Entry<String, Order.ItemPair> entry: order.getOrderDetails().entrySet()) {
                if(totalItemCount.containsKey(entry.getKey())) {
                    totalItemCount.put(entry.getKey(), totalItemCount.get(entry.getKey()).add(entry.getValue().getQuantity()));
                } else {
                    totalItemCount.put(entry.getKey(), entry.getValue().getQuantity());
                }
            }
            return true;
        } else return false;
    }

    public void calculate() {
        Log.d(TAG, "calculate: " + mItemNames.size());
        totalNoTax = totalNoTax.multiply(BigDecimal.ZERO);
        for(int i = 0; i < mItemNames.size(); i++) {
            BigDecimal price = new BigDecimal(mItemPrices.get(i)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            BigDecimal quantity = new BigDecimal(mItemCounts.get(i)).setScale(0, BigDecimal.ROUND_HALF_EVEN);
            Log.d(TAG, "calculate: " + price);
            Log.d(TAG, "calculate: " + quantity);
            totalNoTax = totalNoTax.add(price.multiply(quantity));
        }
        if (menuFragment.addTaxChecked()) {
            menuFragment.setTotalText("Total: $" + totalNoTax.multiply(taxRate).setScale(2, RoundingMode.HALF_EVEN));
        } else {
            menuFragment.setTotalText("Total: $" + totalNoTax);
        }
    }

    public void updateOrderFulfillment(int orderPosition, boolean fulfilled) {
        mOrdersFulfilled.set(orderPosition, fulfilled);
        Order order = mOrderDetails.get(orderPosition);

        if (fulfilled) {
            for (Map.Entry<String, Order.ItemPair> entry: order.getOrderDetails().entrySet()) {
                totalItemCount.put(entry.getKey(), totalItemCount.get(entry.getKey()).subtract(entry.getValue().getQuantity()));
                if (totalItemCount.get(entry.getKey()).equals(BigDecimal.ZERO)) {
                    totalItemCount.remove(entry.getKey());
                }
            }
        } else {
            for (Map.Entry<String, Order.ItemPair> entry: order.getOrderDetails().entrySet()) {
                if(totalItemCount.containsKey(entry.getKey())) {
                    totalItemCount.put(entry.getKey(), totalItemCount.get(entry.getKey()).add(entry.getValue().getQuantity()));
                } else {
                    totalItemCount.put(entry.getKey(), entry.getValue().getQuantity());
                }
            }
        }

        ordersFragment.updateTotalOrders();
    }

    public void clearFields() {
        for(int i = 0; i < mItemCounts.size(); i++) {
            mItemCounts.set(i, 0);
        }
        menuRecyclerViewAdapter.clearCounts();
        totalNoTax = totalNoTax.multiply(BigDecimal.ZERO);
    }

    public BigDecimal getTotalNoTax() {
        return totalNoTax;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public HashMap<String, BigDecimal> getTotalItemCount() {
        return totalItemCount;
    }
}
