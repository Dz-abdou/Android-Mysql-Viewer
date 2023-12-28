package com.example.mysql.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysql.R;

import java.util.ArrayList;
import java.util.List;

public class TablesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<String> tables;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }
    public TablesRecyclerViewAdapter() {
        this.tables = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tables_recyclerview_item,parent,false);
        return new TablesRecyclerViewAdapterViewHolder(rootView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        String database = this.tables.get(position);
        TablesRecyclerViewAdapterViewHolder viewHolder= (TablesRecyclerViewAdapterViewHolder) holder;

        viewHolder.tableNameTv.setText(database);
    }

    @Override
    public int getItemCount() {
        return tables.size();
    }

    public void upDateDatabasesList(final List<String> tables) {
        this.tables.clear();
        this.tables = tables;
        notifyDataSetChanged();
    }

    static class TablesRecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView tableNameTv;

        public TablesRecyclerViewAdapterViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            tableNameTv = itemView.findViewById(R.id.tableNameTV);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null) {
                        int position = getBindingAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
