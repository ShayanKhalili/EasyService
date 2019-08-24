package com.shayank.foodtruckhelper;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<MenuRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "MenuRecyclerViewAdapter";

    private ArrayList<String> itemNames;
    private ArrayList<String> itemPrices;
    private ArrayList<Integer> itemCounts;
    private ArrayList<TextView> itemCountTexts;
    private MainActivity mainActivity;

    public MenuRecyclerViewAdapter(ArrayList<String> itemNames, ArrayList<String> itemPrices, ArrayList<Integer> itemCounts, Context context) {
        this.itemNames = itemNames;
        this.itemPrices = itemPrices;
        this.itemCounts = itemCounts;
        this.itemCountTexts = new ArrayList<>();
        this.mainActivity = (MainActivity) context;
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

        viewHolder.itemName.setText(itemNames.get(i));
        viewHolder.itemPrice.setText("$" + itemPrices.get(i));
        viewHolder.countUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = viewHolder.getAdapterPosition();
                itemCounts.set(pos, itemCounts.get(pos) + 1);
                viewHolder.itemCountText.setText(itemCounts.get(pos).toString());
                mainActivity.calculate();
            }
        });
        viewHolder.countDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = viewHolder.getAdapterPosition();
                itemCounts.set(pos, Math.max(itemCounts.get(pos) - 1, 0));
                viewHolder.itemCountText.setText(itemCounts.get(pos).toString());
                mainActivity.calculate();
            }
        });
        if (itemCounts.size() < i + 1) itemCounts.add(0);
        if (itemCountTexts.size() < i + 1) itemCountTexts.add(viewHolder.itemCountText);
        else itemCountTexts.set(i, viewHolder.itemCountText);
        viewHolder.itemDelete.setText("X");
        viewHolder.itemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                notifyItemRemoved(position);
                itemNames.remove(position);
                itemPrices.remove(position);
                itemCounts.remove(position);
                itemCountTexts.remove(position);
                mainActivity.calculate();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemName;
        TextView itemPrice;
        TextView itemCountText;
        ImageButton countUp;
        ImageButton countDown;
        Button itemDelete;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.menu_item_name);
            itemPrice = itemView.findViewById(R.id.menu_item_price);
            itemCountText = itemView.findViewById(R.id.menu_item_count);
            countUp = itemView.findViewById(R.id.menu_count_up);
            countDown = itemView.findViewById(R.id.menu_count_down);
            itemDelete = itemView.findViewById(R.id.menu_item_delete);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    public void clearCounts() {
        for (TextView textView: itemCountTexts) {
            textView.setText("0");
        }
    }
}
