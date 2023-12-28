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

public class ConnectionsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ConnectionEntity> connections;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClicked(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }
    public ConnectionsRecyclerViewAdapter() {
        connections = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.connection_item,parent,false);
        return new ConnectionsRecyclerViewViewHolder(rootView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ConnectionEntity connection = this.connections.get(position);
        ConnectionsRecyclerViewViewHolder viewHolder= (ConnectionsRecyclerViewViewHolder) holder;

        viewHolder.ipAddressTv.setText(connection.getIpAddress());
        viewHolder.databaseTv.setText(connection.getDbName());
        viewHolder.userTv.setText(connection.getUser());
    }

    @Override
    public int getItemCount() {
        return connections.size();
    }

    public void updateUserList(final List<ConnectionEntity> connections) {
        this.connections.clear();
        this.connections =  connections;
        notifyDataSetChanged();
    }

    static class ConnectionsRecyclerViewViewHolder extends RecyclerView.ViewHolder {

        TextView ipAddressTv;
        TextView databaseTv;
        TextView userTv;
        ImageView connectionItemMenuImageView;

        public ConnectionsRecyclerViewViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            ipAddressTv = itemView.findViewById(R.id.ipAddressTv);
            databaseTv = itemView.findViewById(R.id.databaseTv);
            userTv = itemView.findViewById(R.id.userTv);
            connectionItemMenuImageView = itemView.findViewById(R.id.connectionItemMenuImageView);

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

            connectionItemMenuImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null) {
                        int position = getBindingAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClicked(position, connectionItemMenuImageView);
                        }
                    }
                }
            });
        }
    }
}
