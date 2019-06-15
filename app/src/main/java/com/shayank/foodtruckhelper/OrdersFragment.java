package com.shayank.foodtruckhelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {
    private static final String TAG = "OrdersFragment";

    RecyclerView ordersRecyclerView;
    OrdersRecyclerViewAdapter ordersRecyclerViewAdapter;

    ArrayList<String> mOrderDetails = new ArrayList<>();
    ArrayList<Boolean> mOrdersFulfilled = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ordersRecyclerView = view.findViewById(R.id.orders_list);
        ordersRecyclerViewAdapter = new OrdersRecyclerViewAdapter(mOrderDetails, mOrdersFulfilled, getActivity());
        ordersRecyclerView.setAdapter(ordersRecyclerViewAdapter);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void addOrder(String orderDetails, boolean fulfilled) {
        this.mOrderDetails.add(orderDetails);
        this.mOrdersFulfilled.add(fulfilled);
    }
}
