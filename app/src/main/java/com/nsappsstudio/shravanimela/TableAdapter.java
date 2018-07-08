package com.nsappsstudio.shravanimela;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolderClass> {

    public List<TableList> tableLists;
    public Context context;

    public TableAdapter(List<TableList> tableLists, Context context) {
        this.tableLists = tableLists;
        this.context = context;
    }

    @NonNull
    @Override
    public TableAdapter.ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.table_cell,parent,false);
        return new ViewHolderClass(v);    }

    @Override
    public void onBindViewHolder(@NonNull TableAdapter.ViewHolderClass holder, int position) {
        TableList tableList= tableLists.get(position);
        if (tableList.getTableTitle()==null){
            holder.titleCard.setVisibility(View.GONE);
        }else {
            holder.titleCard.setVisibility(View.VISIBLE);
            holder.titleView.setText(tableList.getTableTitle());
        }
        holder.cell1.setText(tableList.getCell1());
        holder.cell2.setText(tableList.getCell2());
        holder.cell3.setText(tableList.getCell3());
        holder.cell4.setText(tableList.getCell4());

    }

    @Override
    public int getItemCount() {
        return tableLists.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder {

        public TextView titleView;
        public TextView cell1;
        public TextView cell2;
        public TextView cell3;
        public TextView cell4;
        public CardView titleCard;
        public ConstraintLayout tableRow;


        public ViewHolderClass(View itemView) {
            super(itemView);
            titleCard=itemView.findViewById(R.id.table_card);
            tableRow=itemView.findViewById(R.id.table_row);
            titleView=itemView.findViewById(R.id.table_title);
            cell1=itemView.findViewById(R.id.cell1);
            cell2=itemView.findViewById(R.id.cell2);
            cell3=itemView.findViewById(R.id.cell3);
            cell4=itemView.findViewById(R.id.cell4);

        }
    }
}
