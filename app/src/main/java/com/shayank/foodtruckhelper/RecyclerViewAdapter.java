package com.shayank.foodtruckhelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mItemNames;
    private ArrayList<String> mItemPrices;
    private Context mContext;
    private MainActivity mMainActivity;

    public RecyclerViewAdapter(ArrayList<String> mItemNames, ArrayList<String> mItemPrices, Context mContext, MainActivity mMainActivity) {
        this.mItemNames = mItemNames;
        this.mItemPrices = mItemPrices;
        this.mContext = mContext;
        this.mMainActivity = mMainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_menuitem, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called.");

        viewHolder.itemName.setText(mItemNames.get(i));
        viewHolder.itemPrice.setText(mItemPrices.get(i));
        viewHolder.itemPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                mMainActivity.calculate();
            }
        });
        viewHolder.itemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemName;
        TextView itemPrice;
        NumberPicker itemPicker;
        Button itemDelete;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.menu_item_name);
            itemPrice = itemView.findViewById(R.id.menu_item_price);
            itemPicker = itemView.findViewById(R.id.menu_item_picker);
            itemDelete = itemView.findViewById(R.id.menu_item_delete);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
