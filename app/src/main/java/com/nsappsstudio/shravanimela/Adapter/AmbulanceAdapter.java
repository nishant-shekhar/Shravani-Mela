package com.nsappsstudio.shravanimela.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nsappsstudio.shravanimela.Animation.Animations;
import com.nsappsstudio.shravanimela.DoctorShift;
import com.nsappsstudio.shravanimela.Model.AmbulanceModel;
import com.nsappsstudio.shravanimela.Model.ContactModel;
import com.nsappsstudio.shravanimela.R;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.annotations.EverythingIsNonNull;

public class AmbulanceAdapter extends RecyclerView.Adapter<AmbulanceAdapter.ViewHolder> {

    private List<AmbulanceModel> items=new ArrayList<>();
    private Context ctx;

    public AmbulanceAdapter(Context ctx) {
        this.ctx = ctx;
    }
    @EverythingIsNonNull
    @Override
    public ViewHolder onCreateViewHolder(@EverythingIsNonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ambulance_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@EverythingIsNonNull final ViewHolder holder, int position) {
        AmbulanceModel item=items.get(position);
        holder.vehicle.setText((position+1)+". "+item.getVehicleNo());
        holder.place.setText(item.getPlace());
        holder.shift.setText(item.getShift());

        holder.recyclerView.hasFixedSize();
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(ctx));

        ContactAdapter adapter=new ContactAdapter(ctx,3);
        holder.recyclerView.setAdapter(adapter);

        for (int i=0;i<item.getContactModels().size();i++){
            adapter.insertItem(item.getContactModels().get(i));
        }

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView vehicle;
        TextView place;
        TextView shift;
        RecyclerView recyclerView;
        ConstraintLayout card;
        public ViewHolder(@EverythingIsNonNull View itemView) {
            super(itemView);
            vehicle =itemView.findViewById(R.id.textView21);
            place =itemView.findViewById(R.id.textView23);
            shift =itemView.findViewById(R.id.textView34);
            recyclerView =itemView.findViewById(R.id.recyclerView);
            card=itemView.findViewById(R.id.card);

        }
    }
    public void insertItem(AmbulanceModel item) {
        this.items.add(item);
        notifyItemInserted(getItemCount());
    }
}

