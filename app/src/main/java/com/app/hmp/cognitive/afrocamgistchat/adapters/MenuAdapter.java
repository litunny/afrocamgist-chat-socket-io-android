package com.app.hmp.cognitive.afrocamgistchat.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.hmp.cognitive.afrocamgistchat.R;
import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.DataObjectHolder> {

    private ArrayList<String> menuList;
    private OnMenuItemClickListener listener;
    private int itemPosition;

    public MenuAdapter(ArrayList<String> menuList, int position, OnMenuItemClickListener listener) {
        this.menuList = menuList;
        this.itemPosition = position;
        this.listener = listener;
    }

    static class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView label;


        DataObjectHolder(View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label);
        }
    }

    @NonNull
    @Override
    public DataObjectHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.menu_spinner, viewGroup, false);

        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataObjectHolder holder, int position) {

        holder.label.setText(menuList.get(position));
        holder.label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMenuItemClicked(menuList.get(holder.getAdapterPosition()), itemPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (menuList == null)
            return 0;
        else
            return menuList.size();
    }

    public interface OnMenuItemClickListener {
        void onMenuItemClicked(String item, int position);
    }
}
