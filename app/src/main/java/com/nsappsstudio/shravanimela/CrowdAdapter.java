package com.nsappsstudio.shravanimela;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class CrowdAdapter extends RecyclerView.Adapter<CrowdAdapter.ViewHolderClass> {

    public List<CrowdItemList> crowdItemLists;
    public Context context;

    public CrowdAdapter(List<CrowdItemList> crowdItemLists, Context context) {
        this.crowdItemLists = crowdItemLists;
        this.context = context;
    }

    @NonNull
    @Override
    public CrowdAdapter.ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.crowd_status,parent,false);
        return new ViewHolderClass(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CrowdAdapter.ViewHolderClass holder, int position) {
        final CrowdItemList crowdItemList= crowdItemLists.get(position);
        holder.lastUpdate.setText(crowdItemList.getLastUpdate());
        holder.placeName.setText(crowdItemList.getPlace());
        switch (crowdItemList.getCrowdLevel()){
            case "1":
                holder.crowdImage.setImageResource(R.drawable.crowd_1);
                holder.crowdStatus.setText(R.string.light_crowd);
                holder.crowdStatus.setBackgroundColor(context.getResources().getColor(R.color.crowd1));
                break;
            case "2":
                holder.crowdImage.setImageResource(R.drawable.crowd_2);
                holder.crowdStatus.setText(R.string.medium_crowd);
                holder.crowdStatus.setBackgroundColor(context.getResources().getColor(R.color.crowd2));
                break;
            case "3":
                holder.crowdImage.setImageResource(R.drawable.crowd_3);
                holder.crowdStatus.setText(R.string.medium_crowd);
                holder.crowdStatus.setBackgroundColor(context.getResources().getColor(R.color.crowd3));
                break;
            case "4":
                holder.crowdImage.setImageResource(R.drawable.crowd_4);
                holder.crowdStatus.setText(R.string.heavy_crowd);
                holder.crowdStatus.setBackgroundColor(context.getResources().getColor(R.color.crowd4));
                break;
            case "5":
                holder.crowdImage.setImageResource(R.drawable.crowd_6);
                holder.crowdStatus.setText(R.string.heavy_crowd);
                holder.crowdStatus.setBackgroundColor(context.getResources().getColor(R.color.crowd5));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return crowdItemLists.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder {
        public TextView lastUpdate;
        public TextView placeName;
        public TextView crowdStatus;
        public ImageView crowdImage;

        public ViewHolderClass(View itemView) {
            super(itemView);
            lastUpdate=itemView.findViewById(R.id.last_update);
            placeName=itemView.findViewById(R.id.crowd_location);
            crowdStatus=itemView.findViewById(R.id.crowd_status);
            crowdImage=itemView.findViewById(R.id.crowd_image);
        }
    }
}
