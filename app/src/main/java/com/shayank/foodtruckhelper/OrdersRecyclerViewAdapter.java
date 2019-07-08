package com.shayank.foodtruckhelper;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.shawnlin.numberpicker.NumberPicker;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class OrdersRecyclerViewAdapter extends RecyclerView.Adapter<OrdersRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "MenuRecyclerViewAdapter";

    private ArrayList<Order> mOrderDetails;
    private ArrayList<Boolean> mOrdersFulfilled;

    private MainActivity mainActivity;

    public OrdersRecyclerViewAdapter(ArrayList<Order> orderDetails, ArrayList<Boolean> ordersFulfilled, Context context) {
        this.mOrderDetails = orderDetails;
        this.mOrdersFulfilled = ordersFulfilled;
        this.mainActivity = (MainActivity) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_orderitem, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called.");

        viewHolder.orderDetails.setText(mOrderDetails.get(i).getOrderText());
        viewHolder.setOrderFulfilled(mOrdersFulfilled.get(i));
        viewHolder.orderCheckBox.setChecked(mOrdersFulfilled.get(i));
    }

    @Override
    public int getItemCount() {
        return mOrderDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView orderDetails;
        AppCompatCheckBox orderCheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDetails = itemView.findViewById(R.id.order_text);
            orderCheckBox = itemView.findViewById(R.id.order_checkbox);
            orderCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    mainActivity.updateOrderFulfillment(getAdapterPosition(), checked);
                    setOrderFulfilled(checked);
                    mOrdersFulfilled.set(getAdapterPosition(), checked);
                }
            });
        }

        private void setOrderFulfilled(boolean fulfilled) {
            if (fulfilled) {
                orderDetails.setPaintFlags(orderDetails.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                orderDetails.setPaintFlags(orderDetails.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        }
    }
}
