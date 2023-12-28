package com.example.mysql.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysql.R;

import com.example.mysql.models.Cell;

import java.util.ArrayList;

public class RecordsRecyclerViewAdapterChild extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Cell> row;
    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }
    public RecordsRecyclerViewAdapterChild(ArrayList<Cell> row) {
        this.row = row;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.records_recycler_view_item_item,parent,false);
        return new RecordsRecyclerViewChildViewHolder(rootView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        String cellData = this.row.get(position).getData().toString();
        RecordsRecyclerViewChildViewHolder viewHolder= (RecordsRecyclerViewChildViewHolder) holder;

        viewHolder.cellDataTV.setText(cellData);
    }

    @Override
    public int getItemCount() {
        return row.size();
    }

    public void upDateDatabasesList(final ArrayList<Cell> row) {
        this.row.clear();
        this.row = row;
        notifyDataSetChanged();
    }

    static class RecordsRecyclerViewChildViewHolder extends RecyclerView.ViewHolder {

        TextView cellDataTV;

        public RecordsRecyclerViewChildViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            cellDataTV = itemView.findViewById(R.id.cellDataTV);

            itemView.setOnClickListener(view -> {
                if(listener != null) {
                    int position = getBindingAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });

        }
    }
}
