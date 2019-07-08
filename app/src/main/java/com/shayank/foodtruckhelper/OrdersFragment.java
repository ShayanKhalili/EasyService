package com.shayank.foodtruckhelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class OrdersFragment extends Fragment {
    private static final String TAG = "OrdersFragment";

    RecyclerView ordersRecyclerView;
    OrdersRecyclerViewAdapter ordersRecyclerViewAdapter;

    TextView totalOrdersText;

    MainActivity mainActivity;

    public OrdersFragment(OrdersRecyclerViewAdapter ordersRecyclerViewAdapter, MainActivity mainActivity) {
        this.ordersRecyclerViewAdapter = ordersRecyclerViewAdapter;
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ordersRecyclerView = view.findViewById(R.id.orders_list);
        ordersRecyclerView.setAdapter(ordersRecyclerViewAdapter);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        totalOrdersText = view.findViewById(R.id.total_orders_text);
        updateTotalOrders();
    }

    public void updateTotalOrders() {
        StringBuilder totalOrders = new StringBuilder();
        int i = 0;
        for (Map.Entry<String, BigDecimal> entry: mainActivity.getTotalItemCount().entrySet()) {
            totalOrders.append(entry.getValue() + " " + entry.getKey());
            if (i < mainActivity.getTotalItemCount().size() - 1) {
                totalOrders.append(", ");
            }
            i++;
        }
        totalOrdersText.setText(totalOrders.toString());
    }
}
