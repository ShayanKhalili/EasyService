package com.shayank.foodtruckhelper;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.shawnlin.numberpicker.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<MenuRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "MenuRecyclerViewAdapter";

    private ArrayList<String> mItemNames;
    private ArrayList<String> mItemPrices;
    private ArrayList<NumberPicker> mItemPickers = new ArrayList<>();
    private Context mContext;
    private MainActivity mMainActivity;

    public MenuRecyclerViewAdapter(ArrayList<String> mItemNames, ArrayList<String> mItemPrices, Context mContext) {
        this.mItemNames = mItemNames;
        this.mItemPrices = mItemPrices;
        this.mContext = mContext;
        this.mMainActivity = (MainActivity) mContext;
    }

    public ArrayList<NumberPicker> getItemPickers() {
        return this.mItemPickers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_menuitem, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called.");

        viewHolder.itemName.setText(mItemNames.get(i));
        viewHolder.itemPrice.setText("$" + mItemPrices.get(i));
        viewHolder.itemPicker.setMinValue(0);
        viewHolder.itemPicker.setMaxValue(100);
        viewHolder.itemPicker.setValue(0);
        viewHolder.itemPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                mMainActivity.calculate();
            }
        });
        mItemPickers.add(viewHolder.itemPicker);
        viewHolder.itemDelete.setText("X");
        viewHolder.itemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                notifyItemRemoved(position);
                mItemNames.remove(position);
                mItemPrices.remove(position);
                mItemPickers.remove(position);
                mMainActivity.calculate();
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
