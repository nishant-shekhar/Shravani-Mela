package com.nsappsstudio.shravanimela;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
        final DetailCardItem detailCardItem= detailCardItems.get(position);
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
            holder.mobile.setText(detailCardItem.getPhone());
            holder.shift.setText(detailCardItem.getShift());
            holder.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ((DutyCard)context).dialPhoneNumber(detailCardItem.getPhone());
                }
            });
            if (detailCardItem.getDescription()==null){
                holder.descriptionLinearLayout.setVisibility(View.GONE);
            }
            if(detailCardItem.getType()==2){
                holder.mobileLinearLayout.setVisibility(View.GONE);
            }

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
        public TextView mobile;
        public TextView shift;
        public TextView call;
        public LinearLayout descriptionLinearLayout;
        public LinearLayout mobileLinearLayout;



        public ViewHolderClass(View itemView) {
            super(itemView);
            detailCard=itemView.findViewById(R.id.dc_detail_card);
            titleCard=itemView.findViewById(R.id.dc_title_card);
            srNumber=itemView.findViewById(R.id.dc_sr_no);
            optionalTitle=itemView.findViewById(R.id.dc_title);
            innerTitle=itemView.findViewById(R.id.dc_name);
            subtitle=itemView.findViewById(R.id.dc_designation);
            mobile=itemView.findViewById(R.id.dc_mobile);
            description=itemView.findViewById(R.id.dc_description);
            shift=itemView.findViewById(R.id.dc_shift);
            call=itemView.findViewById(R.id.dc_call);
            mainTitle=itemView.findViewById(R.id.dc_mainTitle);
            descriptionLinearLayout=itemView.findViewById(R.id.dc_description_layout);
            mobileLinearLayout=itemView.findViewById(R.id.mobile_linear_layout);

        }
    }
}
