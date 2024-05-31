package com.nsappsstudio.shravanimela.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nsappsstudio.shravanimela.Animation.Animations;
import com.nsappsstudio.shravanimela.Gallery;
import com.nsappsstudio.shravanimela.MainActivity;
import com.nsappsstudio.shravanimela.Model.FacilityItem;
import com.nsappsstudio.shravanimela.Model.PoDModel;
import com.nsappsstudio.shravanimela.R;
import com.nsappsstudio.shravanimela.SubmitPhoto;
import com.nsappsstudio.shravanimela.Utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.annotations.EverythingIsNonNull;

public class PotDAdapter extends RecyclerView.Adapter<PotDAdapter.ViewHolder> {

    private List<PoDModel> items=new ArrayList<>();
    private Context ctx;

    public PotDAdapter(Context ctx) {
        this.ctx = ctx;
    }
    @EverythingIsNonNull
    @Override
    public ViewHolder onCreateViewHolder(@EverythingIsNonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pic_of_the_day, parent, false);
        return new ViewHolder(v);    }

    @Override
    public void onBindViewHolder(@EverythingIsNonNull final ViewHolder holder, int position) {


        final PoDModel item=items.get(position);
        holder.name.setText(item.getWinner());
        holder.location.setText(item.getWinnerLocation());
        holder.date.setText(item.getDate());

        if (position==0){
            holder.spacer.setVisibility(View.VISIBLE);
            holder.constraintLayout.setVisibility(View.VISIBLE);
            holder.cardView.setVisibility(View.GONE);
            holder.date.setBackgroundTintList(ContextCompat.getColorStateList(ctx,R.color.bright_pink));
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animations.squeeze(holder.card,ctx);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (ctx instanceof MainActivity){
                                Intent intent=new Intent(ctx, SubmitPhoto.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                ctx.startActivity(intent);
                            }
                        }
                    },100);
                }
            });


        }else {
            holder.spacer.setVisibility(View.GONE);
            holder.constraintLayout.setVisibility(View.GONE);
            holder.cardView.setVisibility(View.VISIBLE);
            holder.date.setBackgroundTintList(ContextCompat.getColorStateList(ctx,R.color.mm_purple));

            holder.name.setText(item.getWinner());
            holder.location.setText(item.getWinnerLocation());
            holder.date.setText(item.getDate());

            Animations.scaleWithAlpha(holder.name,0f,1f,0.5f,0.5f,200);
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animations.squeeze(holder.card,ctx);
                    Toast.makeText(ctx,"Winner of "+item.getDate()+" is "+item.getWinner(),Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (ctx instanceof MainActivity){
                                ((MainActivity)ctx).loadFullScreenImage(item.getUrl());
                            }
                        }
                    },100);
                }
            });

            if (item.getUrl() != null && item.getUrl().contains("https:")) {
                Picasso.get().load(item.getUrl()).resize(Utils.dpToPx(160,ctx),Utils.dpToPx(90,ctx)).networkPolicy(NetworkPolicy.OFFLINE).into(holder.image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(item.getUrl()).resize(Utils.dpToPx(160,ctx),Utils.dpToPx(90,ctx)).into(holder.image);

                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView location;
        TextView date;
        ImageView image;
        View spacer;
        ConstraintLayout card;
        ConstraintLayout constraintLayout;
        CardView cardView;
        public ViewHolder(@EverythingIsNonNull View itemView) {
            super(itemView);
            name =itemView.findViewById(R.id.first_line);
            location =itemView.findViewById(R.id.first_line2);
            date =itemView.findViewById(R.id.distance);
            image =itemView.findViewById(R.id.image);
            card=itemView.findViewById(R.id.card);
            spacer=itemView.findViewById(R.id.spacer);
            cardView=itemView.findViewById(R.id.cardView3);
            constraintLayout=itemView.findViewById(R.id.free_entry);

        }
    }
    public void insertItem(PoDModel item) {
        this.items.add(item);
        notifyItemInserted(getItemCount());
    }
}

