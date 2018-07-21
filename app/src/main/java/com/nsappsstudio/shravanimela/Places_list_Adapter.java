package com.nsappsstudio.shravanimela;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class Places_list_Adapter extends RecyclerView.Adapter<Places_list_Adapter.ViewHolderClass>  {

    public List<PlacesItem> placesItems;
    public Context context;

    public Places_list_Adapter(List<PlacesItem> placesItems, Context context) {
        this.placesItems = placesItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_simple,parent,false);
        return new ViewHolderClass(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderClass holder, int position) {
        final PlacesItem placesItem= placesItems.get(position);
        holder.title.setText(placesItem.getTitle());
        holder.subTitle.setText(placesItem.getSubtitle());
        holder.mapIcon.setImageResource(R.drawable.ic_map_24dp);
        switch (placesItem.getItemType()){
            case "ATM":
                holder.imageType.setImageResource(R.drawable.atm);
                break;
            case "Bathroom":
                holder.imageType.setImageResource(R.drawable.shower);
                break;
            case "Control Room ":
                holder.imageType.setImageResource(R.drawable.call_center);
                break;
            case "Drinking water":
                holder.imageType.setImageResource(R.drawable.drinking_water);
                break;
            case "Jharna":
                holder.imageType.setImageResource(R.drawable.waterfall);
                break;
            case "Health Centre":
                holder.imageType.setImageResource(R.drawable.ic_healing_black_24dp);
                break;
            case "Information Centre":
                holder.imageType.setImageDrawable(context.getResources().getDrawable(R.drawable.rangoli_bitmap));
                break;
            case "Parking":
                holder.imageType.setImageResource(R.drawable.parking);
                break;
            case "Petrol Pump":
                holder.imageType.setImageResource(R.drawable.ic_local_gas_station_black_24dp);
                break;
            case "Sanskritik Centre":
                holder.imageType.setImageDrawable(context.getResources().getDrawable(R.drawable.rangoli_bitmap));
                break;
            case "Police Station":
                holder.imageType.setImageResource(R.drawable.police);
                break;
            case "Shivir":
                holder.imageType.setImageResource(R.drawable.tent);
                break;
            case "Stay Place":
                holder.imageType.setImageResource(R.drawable.rest_room);
                break;
            case "Temple":
                holder.imageType.setImageDrawable(context.getResources().getDrawable(R.drawable.rangoli_bitmap));
                break;
            case "Toilet":
                holder.imageType.setImageResource(R.drawable.toilet);
                break;
            case "Workshop":
                holder.imageType.setImageResource(R.drawable.workshop);
                break;
            default:
                holder.imageType.setImageDrawable(context.getResources().getDrawable(R.drawable.rangoli_bitmap));
                break;

        }
        holder.contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PLaces)context).onPlaceClicked(placesItem.getLat(),placesItem.getLang(),placesItem.getTitle());
                ((PLaces)context).detailCard(placesItem.getLat(),placesItem.getLang(),placesItem.getTitle(),placesItem.getSubtitle(),placesItem.getItemType(),placesItem.getContact(),placesItem.getAddress());

            }
        });
        holder.goToMapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PLaces)context).startNavigation(placesItem.getLat(),placesItem.getLang());
            }
        });
    }

    @Override
    public int getItemCount() {
        return placesItems.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView subTitle;
        public LinearLayout goToMapLayout;
        public ImageView imageType;
        public LinearLayout contentLayout;
        public ImageView mapIcon;

        public ViewHolderClass(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.ls_title);
            subTitle=itemView.findViewById(R.id.ls_subtitle);
            goToMapLayout=itemView.findViewById(R.id.gmap_layout);
            imageType=itemView.findViewById(R.id.ls_image_type);
            contentLayout=itemView.findViewById(R.id.ls_content_layout);
            mapIcon=itemView.findViewById(R.id.ls_view_gmap);

        }
    }
}
