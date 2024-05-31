package com.nsappsstudio.shravanimela.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nsappsstudio.shravanimela.Animation.Animations;
import com.nsappsstudio.shravanimela.FindPlaces;
import com.nsappsstudio.shravanimela.Model.PlacesModel;
import com.nsappsstudio.shravanimela.R;
import com.nsappsstudio.shravanimela.Utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.internal.annotations.EverythingIsNonNull;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolderClass>  {

    public List<PlacesModel> placesItems;
    public Context context;
    private int selectedPosition=0;

    public PlacesAdapter(List<PlacesModel> placesItems, Context context) {
        this.placesItems = placesItems;
        this.context = context;
    }

    @EverythingIsNonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@EverythingIsNonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.flat_list,parent,false);
        return new ViewHolderClass(v);
    }

    @Override
    public void onBindViewHolder(@EverythingIsNonNull ViewHolderClass holder, int position) {
        final PlacesModel placesItem= placesItems.get(position);
        //Toast.makeText(context,placesItem.getName(),Toast.LENGTH_LONG).show();

        if (placesItem.getName()!=null && placesItem.getName().trim().length()!=0) {
            holder.title.setText(placesItem.getName());
        }else {
            String t=(position+1)+". "+placesItem.getType();
            holder.title.setText(t);
        }
        holder.subTitle.setText(placesItem.getAddress());
        holder.distance.setText(placesItem.getDistance());
        if (placesItem.getPicUrl() != null && placesItem.getPicUrl().contains("https:")) {
            Picasso.get().load(placesItem.getPicUrl()).resize(Utils.dpToPx(60,context),Utils.dpToPx(60,context)).networkPolicy(NetworkPolicy.OFFLINE).into(holder.imageType, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(placesItem.getPicUrl()).resize(Utils.dpToPx(60,context),Utils.dpToPx(60,context)).into(holder.imageType);

                }
            });
            holder.imageType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof FindPlaces ){
                        ((FindPlaces)context).loadFullScreenImage(placesItem.getPicUrl());
                    }
                }
            });
        }else {

            switch (placesItem.getType()) {

                case "Bathroom":
                    holder.imageType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.shower));
                    break;
                case "Control Room ":
                    holder.imageType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.call_center));
                    break;
                case "Drinking water":
                    holder.imageType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.drinking_water));
                    break;

                case "Health Centre":
                    holder.imageType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_healing_black_24dp));
                    break;

                case "Information Centre":
                    holder.imageType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rangoli_bitmap));
                    break;
                case "Police Station":
                    holder.imageType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.police));
                    break;
                case "Shivir":
                    holder.imageType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tent));
                    break;
                case "Stay Place":
                    holder.imageType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rest_room));
                    break;
                case "Toilet":
                    holder.imageType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.toilet));
                    break;
                default:
                    holder.imageType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rangoli_bitmap));
                    break;

            }
        }
        holder.card.setBackground(ContextCompat.getDrawable(context,R.drawable.rounded_square_gradient3));

        if (placesItem.isSelected()){
            holder.card.setBackground(ContextCompat.getDrawable(context,R.drawable.rounded_square_gradient2));
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof FindPlaces) {
                    placesItem.setSelected(true);
                    holder.card.setBackground(ContextCompat.getDrawable(context,R.drawable.rounded_square_gradient2));

                    if (position != selectedPosition) {
                        changeSelection(position);
                    }
                    ((FindPlaces)context).HighLight(placesItem.getLat(),placesItem.getLang());
                }
            }
        });
        holder.navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof FindPlaces) {
                    ((FindPlaces) context).startNavigation(placesItem.getLat(), placesItem.getLang());
                }
            }
        });
        holder.call.setVisibility(View.INVISIBLE);
        if (placesItem.getContact()!=null){
            holder.call.setVisibility(View.VISIBLE);
            holder.call.setOnClickListener(view -> {
                Animations.squeeze(holder.call,context);
                call(placesItem.getContact());
            });
        }
    }
    private void changeSelection(int position) {
        //change the selected position to normal
        PlacesModel dateItem = placesItems.get(selectedPosition);
        dateItem.setSelected(false);
        notifyItemChanged(selectedPosition);
        //Toast.makeText(ctx,position+": "+selectedPosition,Toast.LENGTH_LONG).show();
        selectedPosition = position;
    }
    private void call(String mobileNum) {
        if(mobileNum!=null){
            if (mobileNum.length()>4){
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mobileNum));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }else {
                Toast.makeText(context,"Number isn't Available",Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(context,"Number isn't Available",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public int getItemCount() {
        return placesItems.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView subTitle;
        public TextView distance;
        public LinearLayout navigate;
        public LinearLayout call;
        public ImageView imageType;
        public ConstraintLayout card;

        public ViewHolderClass(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.name);
            subTitle=itemView.findViewById(R.id.textView12);
            navigate=itemView.findViewById(R.id.navigate);
            call=itemView.findViewById(R.id.call);
            imageType=itemView.findViewById(R.id.pic);
            card=itemView.findViewById(R.id.card);
            distance=itemView.findViewById(R.id.distance);

        }
    }
}
