package com.nsappsstudio.shravanimela;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
        if (tableList.isTableTitle()){
            holder.cell1.setBackgroundResource(R.drawable.cell_light_bg);
            holder.cell2.setBackgroundResource(R.drawable.cell_light_bg);
            holder.cell3.setBackgroundResource(R.drawable.cell_light_bg);
            holder.cell4.setBackgroundResource(R.drawable.cell_light_bg);
            holder.cell5.setBackgroundResource(R.drawable.cell_light_bg);
            /*holder.cell1.setTextColor(context.getResources().getColor(R.color.background));
            holder.cell2.setTextColor(context.getResources().getColor(R.color.background));
            holder.cell3.setTextColor(context.getResources().getColor(R.color.background));
            holder.cell4.setTextColor(context.getResources().getColor(R.color.background));
            holder.cell5.setTextColor(context.getResources().getColor(R.color.background));*/
            holder.cell1.setTypeface(null, Typeface.BOLD);
            holder.cell2.setTypeface(null, Typeface.BOLD);
            holder.cell3.setTypeface(null, Typeface.BOLD);
            holder.cell4.setTypeface(null, Typeface.BOLD);
            holder.cell5.setTypeface(null, Typeface.BOLD);
            holder.cell1.setTextSize(16);
            holder.cell2.setTextSize(16);
            holder.cell3.setTextSize(16);
            holder.cell4.setTextSize(16);
            holder.cell5.setTextSize(16);


        }else {
           /* if (tableList.isEven()) {

            }else {
                holder.cell1.setBackgroundResource(R.drawable.cell_light_bg);
                holder.cell2.setBackgroundResource(R.drawable.cell_light_bg);
                holder.cell3.setBackgroundResource(R.drawable.cell_light_bg);
                holder.cell4.setBackgroundResource(R.drawable.cell_light_bg);
                holder.cell5.setBackgroundResource(R.drawable.cell_light_bg);

            }*/
            holder.cell1.setBackgroundResource(R.drawable.cell_bg);
            holder.cell2.setBackgroundResource(R.drawable.cell_bg);
            holder.cell3.setBackgroundResource(R.drawable.cell_bg);
            holder.cell4.setBackgroundResource(R.drawable.cell_bg);
            holder.cell5.setBackgroundResource(R.drawable.cell_bg);
           /* holder.cell1.setTextColor(Color.BLACK);
            holder.cell2.setTextColor(Color.BLACK);
            holder.cell3.setTextColor(Color.BLACK);
            holder.cell4.setTextColor(Color.BLACK);
            holder.cell5.setTextColor(Color.BLACK);*/
            holder.cell1.setTypeface(null, Typeface.NORMAL);
            holder.cell2.setTypeface(null, Typeface.NORMAL);
            holder.cell3.setTypeface(null, Typeface.NORMAL);
            holder.cell4.setTypeface(null, Typeface.NORMAL);
            holder.cell5.setTypeface(null, Typeface.NORMAL);
            holder.cell1.setTextSize(14);
            holder.cell2.setTextSize(14);
            holder.cell3.setTextSize(14);
            holder.cell4.setTextSize(14);
            holder.cell5.setTextSize(14);

        }


        switch (tableList.getType()){
            case 3:
                holder.cell1.setVisibility(View.GONE);
                holder.cell5.setVisibility(View.GONE);
                break;
            case 4:
                holder.cell3.setVisibility(View.GONE);
                holder.cell5.setVisibility(View.GONE);
                break;
            case 5:
                holder.cell5.setVisibility(View.GONE);
                break;

        }
        holder.cell1.setText(tableList.getCell1());
        holder.cell2.setText(tableList.getCell2());
        holder.cell3.setText(tableList.getCell3());
        holder.cell4.setText(tableList.getCell4());
        holder.cell5.setText(tableList.getCell5());

        ViewGroup.LayoutParams params = holder.cell1.getLayoutParams();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        holder.cell1.setLayoutParams(params);

        ViewGroup.LayoutParams params2 = holder.cell2.getLayoutParams();
        params2.height = ViewGroup.LayoutParams.MATCH_PARENT;
        holder.cell2.setLayoutParams(params2);

        ViewGroup.LayoutParams params3 = holder.cell3.getLayoutParams();
        params3.height = ViewGroup.LayoutParams.MATCH_PARENT;
        holder.cell3.setLayoutParams(params3);

        ViewGroup.LayoutParams params4 = holder.cell4.getLayoutParams();
        params4.height = ViewGroup.LayoutParams.MATCH_PARENT;
        holder.cell4.setLayoutParams(params4);

        ViewGroup.LayoutParams params5 = holder.cell5.getLayoutParams();
        params5.height = ViewGroup.LayoutParams.MATCH_PARENT;
        holder.cell5.setLayoutParams(params5);
    }

    @Override
    public int getItemCount() {
        return tableLists.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder {

        public TextView cell1;
        public TextView cell2;
        public TextView cell3;
        public TextView cell4;
        public TextView cell5;
        public LinearLayout tableRow;


        public ViewHolderClass(View itemView) {
            super(itemView);
            tableRow=itemView.findViewById(R.id.table_row);
            cell1=itemView.findViewById(R.id.cell1);
            cell2=itemView.findViewById(R.id.cell2);
            cell3=itemView.findViewById(R.id.cell3);
            cell4=itemView.findViewById(R.id.cell4);
            cell5=itemView.findViewById(R.id.cell5);

        }
    }
}
