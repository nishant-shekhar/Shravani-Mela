package com.nsappsstudio.shravanimela.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nsappsstudio.shravanimela.Model.CrowdItemList;
import com.nsappsstudio.shravanimela.R;

import java.util.List;

import okhttp3.internal.annotations.EverythingIsNonNull;


public class CrowdAdapter extends RecyclerView.Adapter<CrowdAdapter.ViewHolderClass> {

    public List<CrowdItemList> crowdItemLists;
    public Context context;

    public CrowdAdapter(List<CrowdItemList> crowdItemLists, Context context) {
        this.crowdItemLists = crowdItemLists;
        this.context = context;
    }

    @EverythingIsNonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@EverythingIsNonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.crowd_status,parent,false);
        return new ViewHolderClass(v);
    }

    @Override
    public void onBindViewHolder(@EverythingIsNonNull ViewHolderClass holder, int position) {
        //if (position==0){
         //   holder.spacer.setVisibility(View.VISIBLE);
        //}else {
        //}
        holder.spacer.setVisibility(View.GONE);

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
        public View spacer;

        public ViewHolderClass(View itemView) {
            super(itemView);
            lastUpdate=itemView.findViewById(R.id.last_update);
            placeName=itemView.findViewById(R.id.crowd_location2);
            crowdStatus=itemView.findViewById(R.id.crowd_status);
            crowdImage=itemView.findViewById(R.id.crowd_image);
            spacer=itemView.findViewById(R.id.spacer);
        }
    }
}
