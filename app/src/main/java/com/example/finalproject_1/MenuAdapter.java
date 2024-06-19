package com.example.finalproject_1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<MenuItem> menuItems;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(MenuItem item);
    }

    public MenuAdapter(List<MenuItem> menuItems, OnItemClickListener onItemClickListener) {
        this.menuItems = menuItems;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem menuItem = menuItems.get(position);
        holder.menuItemName.setText(menuItem.getName());
        holder.menuItemPrice.setText(String.valueOf(menuItem.getPrice()) + "원");
        holder.menuItemImage.setImageResource(menuItem.getImageResId());

        holder.menuItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(menuItem);
            }
        });
        holder.menuItemPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(menuItem);
            }
        });
        holder.menuItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(menuItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public void updateMenuItems(List<MenuItem> newMenuItems) {
        this.menuItems = newMenuItems;
        notifyDataSetChanged();
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {

        ImageView menuItemImage;
        TextView menuItemName;
        TextView menuItemPrice;

        MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            menuItemImage = itemView.findViewById(R.id.menuItemImage);
            menuItemName = itemView.findViewById(R.id.menuItemName);
            menuItemPrice = itemView.findViewById(R.id.menuItemPrice);
        }
    }
}
