package com.nsappsstudio.shravanimela;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DetailCardAdaptor extends RecyclerView.Adapter<DetailCardAdaptor.ViewHolderClass> {
    public Context context;
    public List<DetailCardItem> detailCardItems;

    public DetailCardAdaptor(Context context, List<DetailCardItem> detailCardItems) {
        this.context = context;
        this.detailCardItems = detailCardItems;
    }

    @NonNull
    @Override
    public DetailCardAdaptor.ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_card,parent,false);
        return new ViewHolderClass(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailCardAdaptor.ViewHolderClass holder, int position) {
        DetailCardItem detailCardItem= detailCardItems.get(position);
        if (detailCardItem.isTitle()){
            holder.detailCard.setVisibility(View.GONE);
            holder.mainTitle.setText(detailCardItem.getMainTitle());
            holder.titleCard.setVisibility(View.VISIBLE);
        }else {
            holder.detailCard.setVisibility(View.VISIBLE);
            holder.titleCard.setVisibility(View.GONE);

            holder.srNumber.setText(detailCardItem.getSrNumber());
            holder.optionalTitle.setText(detailCardItem.getOptionalTitle());
            holder.innerTitle.setText(detailCardItem.getInnerTitle());
            holder.subtitle.setText(detailCardItem.getSubTitle());
            holder.description.setText(detailCardItem.getDescription());
            holder.shift.setText(detailCardItem.getShift());
            holder.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return detailCardItems.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder {
        public CardView detailCard;
        public CardView titleCard;
        public TextView mainTitle;
        public TextView srNumber;
        public TextView optionalTitle;
        public TextView innerTitle;
        public TextView subtitle;
        public TextView description;
        public TextView shift;
        public TextView call;



        public ViewHolderClass(View itemView) {
            super(itemView);
            detailCard=itemView.findViewById(R.id.dc_detail_card);
            titleCard=itemView.findViewById(R.id.dc_title_card);
            srNumber=itemView.findViewById(R.id.dc_sr_no);
            optionalTitle=itemView.findViewById(R.id.dc_title);
            innerTitle=itemView.findViewById(R.id.dc_name);
            subtitle=itemView.findViewById(R.id.dc_designation);
            description=itemView.findViewById(R.id.dc_mobile);
            shift=itemView.findViewById(R.id.dc_shift);
            call=itemView.findViewById(R.id.dc_call);
            mainTitle=itemView.findViewById(R.id.dc_mainTitle);

        }
    }
}
