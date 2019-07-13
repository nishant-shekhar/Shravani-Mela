package com.nsappsstudio.shravanimela;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolderClass> {
    public List<CardListItem> cardListItems;
    public Context context;

    public CardAdapter(List<CardListItem> cardListItems, Context context) {
        this.cardListItems = cardListItems;
        this.context = context;
    }

    @NonNull
    @Override
    public CardAdapter.ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list,parent,false);
        return new ViewHolderClass(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.ViewHolderClass holder, int position) {

        final CardListItem cardListItem= cardListItems.get(position);
        holder.cTitle.setText(cardListItem.getcTitle());
        holder.cSubtitle.setText(cardListItem.getcSubtitle());
        holder.cBody.setText(cardListItem.getcBody());
        if (cardListItem.getLoadType()==1) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Notification) context).showNotificationOnClick(cardListItem.getcTitle(), cardListItem.getcSubtitle(), cardListItem.getcBody(), cardListItem.getcType());

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return cardListItems.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder {
        public TextView cTitle;
        public TextView cSubtitle;
        public TextView cBody;
        public CardView cardView;

        public ViewHolderClass(View itemView) {
            super(itemView);
            cTitle=itemView.findViewById(R.id.c_title);
            cSubtitle=itemView.findViewById(R.id.c_subtitle);
            cBody=itemView.findViewById(R.id.c_body);
            cardView=itemView.findViewById(R.id.c_cardview);
        }
    }
}
