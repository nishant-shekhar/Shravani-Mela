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

import com.google.common.cache.AbstractCache;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.nsappsstudio.shravanimela.Animation.Animations;
import com.nsappsstudio.shravanimela.MainActivity;
import com.nsappsstudio.shravanimela.Model.EventModel;
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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<EventModel> items=new ArrayList<>();
    private Context ctx;

    public EventAdapter(Context ctx) {
        this.ctx = ctx;
    }
    @EverythingIsNonNull
    @Override
    public ViewHolder onCreateViewHolder(@EverythingIsNonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_details, parent, false);
        return new ViewHolder(v);    }

    @Override
    public void onBindViewHolder(@EverythingIsNonNull final ViewHolder holder, int position) {


        final EventModel item=items.get(position);
        holder.name.setText(item.getEventName());
        holder.date.setText(item.getEventDate());

        if (position==0){
            holder.spacer.setVisibility(View.VISIBLE);



        }else {
            holder.spacer.setVisibility(View.GONE);
        }


            Animations.scaleWithAlpha(holder.name,0f,1f,0.5f,0.5f,200);

                item.setDpUrl("https://firebasestorage.googleapis.com/v0/b/shravanimela18.appspot.com/o/ShravaniMela24%2FUntitled%20design.png?alt=media&token=aa39bc46-264a-420c-8541-6f82371aa202");
            if (item.getDpUrl() != null && item.getDpUrl().contains("https:")) {
                Picasso.get().load(item.getDpUrl()).resize(Utils.dpToPx(160,ctx),Utils.dpToPx(90,ctx)).networkPolicy(NetworkPolicy.OFFLINE).into(holder.image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(item.getDpUrl()).resize(Utils.dpToPx(160,ctx),Utils.dpToPx(90,ctx)).into(holder.image);

                    }
                });
            }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animations.squeeze(holder.card,ctx);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (ctx instanceof MainActivity){
                            ((MainActivity)ctx).loadEventDialog(item);
                        }
                    }
                },100);
            }
        });
        holder.simpleRatingBar.setEnabled(false);
        holder.simpleRatingBar.setRating(item.getStars());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView date;
        ImageView image;
        View spacer;
        ConstraintLayout card;
        SimpleRatingBar simpleRatingBar;
        public ViewHolder(@EverythingIsNonNull View itemView) {
            super(itemView);
            name =itemView.findViewById(R.id.first_line);
            date =itemView.findViewById(R.id.first_line2);
            image =itemView.findViewById(R.id.image);
            card=itemView.findViewById(R.id.card);
            spacer=itemView.findViewById(R.id.spacer);
            simpleRatingBar=itemView.findViewById(R.id.simpleRatingBar);

        }
    }
    public void insertItem(EventModel item) {
        this.items.add(item);
        notifyItemInserted(getItemCount());
    }
}

