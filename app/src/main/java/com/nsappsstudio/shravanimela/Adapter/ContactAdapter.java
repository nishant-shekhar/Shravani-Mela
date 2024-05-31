package com.nsappsstudio.shravanimela.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.nsappsstudio.shravanimela.Animation.Animations;
import com.nsappsstudio.shravanimela.DoctorShift;
import com.nsappsstudio.shravanimela.Gallery;
import com.nsappsstudio.shravanimela.Model.ContactModel;
import com.nsappsstudio.shravanimela.Model.PoDModel;
import com.nsappsstudio.shravanimela.R;
import com.nsappsstudio.shravanimela.Utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.annotations.EverythingIsNonNull;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private List<ContactModel> items=new ArrayList<>();
    private Context ctx;
    private int type;

    public ContactAdapter(Context ctx, int type) {
        this.ctx = ctx;
        this.type = type;
    }

    @EverythingIsNonNull
    @Override
    public ViewHolder onCreateViewHolder(@EverythingIsNonNull ViewGroup parent, int viewType) {
        View v;
        if (type==2) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item2, parent, false);

        }else if (type==3){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item2, parent, false);

        }
        else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false);

        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@EverythingIsNonNull final ViewHolder holder, int position) {
        ContactModel item=items.get(position);

        SharedPreferences sharedPref = ctx.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String language= sharedPref.getString("lang",null);
        if (language!=null && language.equals("hi")){
            holder.name.setText(item.getNameHindi());
            holder.designation.setText(item.getDesignationHindi());
        }else {
            holder.name.setText(item.getNameEng());
            holder.designation.setText(item.getDesignationEng());
        }

        if (item.getNameEng()==null && item.getNameHindi()==null){
            holder.name.setText(String.valueOf(position+1));
        }
        holder.contact.setText(item.getContact());
        if (ctx instanceof DoctorShift) {
            holder.card.setVisibility(View.GONE);
            Animations.translateWithAlpha(holder.card, position * 100, 300, 2, 0.5f, 1f);
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animations.squeeze(holder.card,ctx);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        call(item.getContact());
                        if (ctx instanceof DoctorShift){
                            Toast.makeText(ctx,"Calling "+item.getNameEng(),Toast.LENGTH_LONG).show();
                        }

                    }
                },300);

            }
        });




    }
    private void call(String mobileNum) {
        if(mobileNum!=null){
            if (mobileNum.length()>1){
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mobileNum));
                if (intent.resolveActivity(ctx.getPackageManager()) != null) {
                    ctx.startActivity(intent);
                }
            }else {
                Toast.makeText(ctx,"Number isn't Available",Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(ctx,"Number isn't Available",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView designation;
        TextView contact;
        LinearLayout card;
        public ViewHolder(@EverythingIsNonNull View itemView) {
            super(itemView);
            name =itemView.findViewById(R.id.textView13);
            designation =itemView.findViewById(R.id.textView14);
            contact =itemView.findViewById(R.id.textView18);
            card=itemView.findViewById(R.id.card);

        }
    }
    public void insertItem(ContactModel item) {
        this.items.add(item);
        notifyItemInserted(getItemCount());
    }
}

