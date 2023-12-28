package com.example.mysql.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysql.R;
import com.example.mysql.database.entities.ConnectionEntity;

import java.util.ArrayList;
import java.util.List;

public class DatabasesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<String> databases;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }
    public DatabasesRecyclerViewAdapter(ArrayList<String> databases) {
        this.databases = databases;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.databases_recyclerview_item,parent,false);
        return new DatabasesRecyclerViewViewHolder(rootView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        String database = this.databases.get(position);
        DatabasesRecyclerViewViewHolder viewHolder= (DatabasesRecyclerViewViewHolder) holder;

        viewHolder.databaseNameTV.setText(database);
    }

    @Override
    public int getItemCount() {
        return databases.size();
    }

    public void upDateDatabasesList(final List<String> databases) {
        this.databases.clear();
        this.databases = databases;
        notifyDataSetChanged();
    }

    static class DatabasesRecyclerViewViewHolder extends RecyclerView.ViewHolder {

        TextView databaseNameTV;

        public DatabasesRecyclerViewViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            databaseNameTV = itemView.findViewById(R.id.databaseNameTV);


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
