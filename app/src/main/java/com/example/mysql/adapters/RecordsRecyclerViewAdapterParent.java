package com.example.mysql.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysql.R;
import com.example.mysql.models.Cell;

import java.util.ArrayList;

public class RecordsRecyclerViewAdapterParent extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<ArrayList<Cell>> rows;
    private OnItemClickListener mListener;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }
    public RecordsRecyclerViewAdapterParent(Context context, ArrayList<ArrayList<Cell>> rows) {
        this.rows = rows;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView;
        switch (viewType) {
            case 0: rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.records_recycler_view_item,parent,false);
                    break;
            case 2: rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.records_recycler_view_item2,parent,false);
                    break;
            default: rootView = null;
        }

        return new RecordsRecyclerViewParentViewHolder(rootView, mListener, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ArrayList<Cell> row = this.rows.get(position);
        RecordsRecyclerViewParentViewHolder viewHolder1 = (RecordsRecyclerViewParentViewHolder) holder;
        viewHolder1.childRecyclerView.setAdapter(new RecordsRecyclerViewAdapterChild(row));
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return position % 2 * 2;
    }

    public void upDateDatabasesList(final ArrayList<ArrayList<Cell>> rows) {
        this.rows.clear();
        this.rows = rows;
        notifyDataSetChanged();
    }

    static class RecordsRecyclerViewParentViewHolder extends RecyclerView.ViewHolder {

        RecyclerView childRecyclerView;

        public RecordsRecyclerViewParentViewHolder(@NonNull View itemView, OnItemClickListener listener, Context context) {
            super(itemView);
            int position = getBindingAdapterPosition();
            childRecyclerView = itemView.findViewById(R.id.childRecyclerView);
            childRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            Log.e("position", getLayoutPosition() + "");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null) {

                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

}
