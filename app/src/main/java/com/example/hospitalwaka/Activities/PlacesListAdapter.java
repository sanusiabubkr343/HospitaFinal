package com.example.hospitalwaka.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hospitalwaka.Models.myModel;
import com.example.hospitalwaka.R;
import com.example.hospitalwaka.Util.WakaApi;

import java.text.MessageFormat;
import java.util.List;

public class PlacesListAdapter  extends RecyclerView.Adapter<PlacesListAdapter.ViewHolder> {
    final List<myModel> arrayList  = WakaApi.getInstance().getMyList();
    Context context;

    public PlacesListAdapter(Context context) {

        this.context = context;
    }

    @NonNull
    @Override
    public PlacesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_element, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesListAdapter.ViewHolder holder, int position) {
        myModel model = arrayList.get(position);
        holder.Vicinity.setText(model.getVicinity());
        holder.hospitalName.setText(model.getHospitalName());
        holder.geographicalLocation.setText(MessageFormat.format("Location{0}", model.getGeographicalLocation()));
        Glide.with(context).load(model.getPhotoUrl()).into(holder.row_imageview);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView row_imageview;
        public TextView hospitalName;
        public TextView Vicinity;
        public  TextView geographicalLocation;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Vicinity = itemView.findViewById(R.id.vicinity);
            hospitalName = itemView.findViewById(R.id.hospitalName);
            geographicalLocation = itemView.findViewById(R.id.geograhicalLocation);
            row_imageview = itemView.findViewById(R.id.row_imageview);
        }
    }



}
