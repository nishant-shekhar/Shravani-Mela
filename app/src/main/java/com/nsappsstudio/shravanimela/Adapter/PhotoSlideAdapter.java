package com.nsappsstudio.shravanimela.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;


import com.nsappsstudio.shravanimela.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.annotations.EverythingIsNonNull;

public class PhotoSlideAdapter extends RecyclerView.Adapter<PhotoSlideAdapter.ViewHolder> {

    private List<String> items=new ArrayList<>();
    private Context ctx;

    public PhotoSlideAdapter(List<String> items, Context ctx) {
        this.items = items;
        this.ctx = ctx;
    }

    @EverythingIsNonNull
    @Override
    public ViewHolder onCreateViewHolder(@EverythingIsNonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.slides,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@EverythingIsNonNull ViewHolder holder, int position) {
        String item=items.get(position);
        if (item != null && item.contains("https:")) {
            Picasso.get().load(item).networkPolicy(NetworkPolicy.OFFLINE).into(holder.imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(item).into(holder.imageView);

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       private ImageView imageView;
        public ViewHolder(@EverythingIsNonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.slide_image);


        }
    }
}
