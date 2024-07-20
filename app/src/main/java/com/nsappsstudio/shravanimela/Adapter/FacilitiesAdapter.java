package com.nsappsstudio.shravanimela.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nsappsstudio.shravanimela.Animation.Animations;
import com.nsappsstudio.shravanimela.FindPlaces;
import com.nsappsstudio.shravanimela.Gallery;
import com.nsappsstudio.shravanimela.MainActivity;
import com.nsappsstudio.shravanimela.R;
import com.nsappsstudio.shravanimela.Model.FacilityItem;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.annotations.EverythingIsNonNull;

public class FacilitiesAdapter extends RecyclerView.Adapter<FacilitiesAdapter.ViewHolder> {

    private List<FacilityItem> items=new ArrayList<>();
    private Context ctx;

    public FacilitiesAdapter(Context ctx) {
        this.ctx = ctx;
    }
    @EverythingIsNonNull
    @Override
    public ViewHolder onCreateViewHolder(@EverythingIsNonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.services_item, parent, false);
        return new ViewHolder(v);    }

    @Override
    public void onBindViewHolder(@EverythingIsNonNull final ViewHolder holder, int position) {
        final FacilityItem item=items.get(position);
        holder.name.setText(item.getName());

        Animations.scaleWithAlpha(holder.card,0.1f,1f,0.5f,0.5f,300);
        Animations.scaleWithAlpha(holder.name,0f,1f,0.5f,0.5f,200);

        switch (item.getName()){
            case "Ambulance":
                holder.imageView.setImageDrawable(ContextCompat.getDrawable(ctx,R.drawable.ambulance));
                holder.name.setText(ctx.getResources().getString(R.string.ambulance));
                break;
            case "Control Room":
                holder.imageView.setImageDrawable(ContextCompat.getDrawable(ctx,R.drawable.call_center));
                holder.name.setText(ctx.getResources().getString(R.string.control_room));

                break;
            case "Toilets":
                holder.imageView.setImageDrawable(ContextCompat.getDrawable(ctx,R.drawable.toilet));
                holder.name.setText(ctx.getResources().getString(R.string.toilet));

                break;
            case "Bathroom":
                holder.imageView.setImageDrawable(ContextCompat.getDrawable(ctx,R.drawable.shower));
                holder.name.setText(ctx.getResources().getString(R.string.bathroom));

                break;
            case "Drinking Water":
                holder.imageView.setImageDrawable(ContextCompat.getDrawable(ctx,R.drawable.drinking_water));
                holder.name.setText(ctx.getResources().getString(R.string.drinking_water));

                break;
            case "Police Station":
                holder.imageView.setImageDrawable(ContextCompat.getDrawable(ctx,R.drawable.police));
                holder.name.setText(ctx.getResources().getString(R.string.police_station));

                break;
            case "Gallery":
                holder.imageView.setImageDrawable(ContextCompat.getDrawable(ctx, com.github.dhaval2404.imagepicker.R.drawable.ic_photo_black_48dp));
                holder.name.setText(ctx.getResources().getString(R.string.gallery));

                break;
            case "Rest Room":
                holder.imageView.setImageDrawable(ContextCompat.getDrawable(ctx,R.drawable.rest_room));
                holder.name.setText(ctx.getResources().getString(R.string.rest_room));

                break;
            case "Centralize Contact":
                holder.imageView.setImageDrawable(ContextCompat.getDrawable(ctx,R.drawable.ic_call));
                holder.name.setText(ctx.getResources().getString(R.string.centralizeContact));

                break;
            case "Mela Route":
                holder.imageView.setImageDrawable(ContextCompat.getDrawable(ctx,R.drawable.ic_route));
                holder.name.setText(ctx.getResources().getString(R.string.mela_route));

                break;
            case "Parking":
                holder.imageView.setImageDrawable(ContextCompat.getDrawable(ctx,R.drawable.parking));
                holder.name.setText(ctx.getResources().getString(R.string.parking));

                break;
            case "Health Centre":
                holder.imageView.setImageDrawable(ContextCompat.getDrawable(ctx,R.drawable.ic_healing_black_24dp));
                holder.name.setText(ctx.getResources().getString(R.string.health_centre));

                break;
            case "Shivir":
                holder.imageView.setImageDrawable(ContextCompat.getDrawable(ctx,R.drawable.tent));
                holder.name.setText(ctx.getResources().getString(R.string.shivir));

                break;
            case "Dharamshala":
                holder.imageView.setImageDrawable(ContextCompat.getDrawable(ctx,R.drawable.tent));
                holder.name.setText(ctx.getResources().getString(R.string.dharamshala));

                break;
            case "DataType2":
                holder.imageView.setImageDrawable(ContextCompat.getDrawable(ctx,R.drawable.baseline_atm_24));
                holder.name.setText(ctx.getResources().getString(R.string.atm));

                break;
            case "Fire Brigade":
                holder.imageView.setImageDrawable(ContextCompat.getDrawable(ctx,R.drawable.baseline_fire_truck_24));
                holder.name.setText(ctx.getResources().getString(R.string.fire_brigade));

                break;
        }
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (item.getName()){
                    case "Gallery":
                        openGallery();
                        break;
                    case "Ambulance":
                        ((MainActivity)ctx).loadAmbulanceList();
                        break;
                    case "Control Room":
                        openPlaces("ControlRoom",item.getName());
                        break;
                    case "Toilets":
                        openPlaces("Toilets","Toilets");
                        break;
                    case "Bathroom":
                        openPlaces("Bathroom",item.getName());
                        break;
                    case "Drinking Water":
                        openPlaces("Drinking Waters",item.getName());
                        break;
                    case "Police Station":
                        openPlaces("Police Station",item.getName());
                        break;
                    case "Rest Room":
                        openPlaces("Schools for Stay",item.getName());
                        break;
                    case "Centralize Contact":
                        ((MainActivity)ctx).loadContactList("Centralize Contact");
                        break;
                    case "Mela Route":
                        ((MainActivity)ctx).routeImage();
                        break;
                    case "Parking":
                        openPlaces("Parking",item.getName());


                        break;
                    case "Health Centre":
                        openPlaces("Health Centre",item.getName());

                        break;
                    case "Shivir":
                        openPlaces("Shivir",item.getName());
                        break;
                    case "Dharamshala":
                        openPlaces("Dharamshala",item.getName());

                        break;
                    case "DataType2":
                        openPlaces("DataType2","ATM");

                        break;
                    case "Fire Brigade":
                        openPlaces("Fire Brigade",item.getName());

                        break;
                }
            }
        });


    }
    private void openContactList(String name ){
        //Intent intent=new Intent(ctx, SubmitPhoto.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //ctx.startActivity(intent);
        Toast.makeText(ctx,"Will Activate this once data is populated",Toast.LENGTH_LONG).show();
    }
    private void openGallery(){
        Intent intent=new Intent(ctx, Gallery.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ctx.startActivity(intent);
        //Toast.makeText(ctx,"Gallery",Toast.LENGTH_LONG).show();

    }
    private void openPlaces(String name ,String display){
        Intent intent=new Intent(ctx, FindPlaces.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("type",name);
        intent.putExtra("display",display);

        ctx.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView imageView;
        ConstraintLayout card;
        public ViewHolder(@EverythingIsNonNull View itemView) {
            super(itemView);
            name =itemView.findViewById(R.id.textView4);
            card=itemView.findViewById(R.id.card);
            imageView=itemView.findViewById(R.id.imageView4);

        }
    }
    public void insertItem(FacilityItem item) {
        this.items.add(item);
        notifyItemInserted(getItemCount());
    }
}

