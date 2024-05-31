package com.nsappsstudio.shravanimela.Adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.nsappsstudio.shravanimela.Animation.Animations;
import com.nsappsstudio.shravanimela.Gallery;
import com.nsappsstudio.shravanimela.MainActivity;
import com.nsappsstudio.shravanimela.Model.PoDModel;
import com.nsappsstudio.shravanimela.R;
import com.nsappsstudio.shravanimela.Utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.annotations.EverythingIsNonNull;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private List<PoDModel> items=new ArrayList<>();
    private Context ctx;

    public GalleryAdapter(Context ctx) {
        this.ctx = ctx;
    }
    @EverythingIsNonNull
    @Override
    public ViewHolder onCreateViewHolder(@EverythingIsNonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
        return new ViewHolder(v);    }

    @Override
    public void onBindViewHolder(@EverythingIsNonNull final ViewHolder holder, int position) {
        final PoDModel item=items.get(position);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animations.squeeze(holder.card,ctx);
                if(item.getWinner()!=null&& item.getWinner().trim().length()>0) {
                    Toast.makeText(ctx, "Photo submitted by " + item.getWinner(), Toast.LENGTH_LONG).show();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (ctx instanceof Gallery){
                            ((Gallery)ctx).loadFullScreenImage(item.getUrl());
                        }
                    }
                },100);
            }
        });

        if (item.getUrl() != null && item.getUrl().contains("https:")) {
            Picasso.get().load(item.getUrl()).resize(Utils.dpToPx(100,ctx),Utils.dpToPx(100,ctx)).networkPolicy(NetworkPolicy.OFFLINE).into(holder.image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(item.getUrl()).resize(Utils.dpToPx(100,ctx),Utils.dpToPx(100,ctx)).into(holder.image);

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ConstraintLayout card;
        public ViewHolder(@EverythingIsNonNull View itemView) {
            super(itemView);
            image =itemView.findViewById(R.id.imageView6);
            card=itemView.findViewById(R.id.card);

        }
    }
    public void insertItem(PoDModel item) {
        this.items.add(item);
        notifyItemInserted(getItemCount());
    }
}

