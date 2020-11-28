package com.gstatic.test.ui.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gstatic.test.App;
import com.gstatic.test.R;
import com.gstatic.test.pojo.LocationData;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<LocationData> marketList;
    RecycleViewInterface onLoadMoreListener;

    public RecyclerViewAdapter() {
        marketList = new ArrayList<>();
    }

    public void setOnLoadMoreListener(RecycleViewInterface onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_layout, parent, false);

        RecyclerViewAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        LocationData locationData = marketList.get(position);
        holder.tvCount.setText(String.valueOf(position+1));
        //holder.tvId.setText(locationData.getId());
        holder.tvName.setText(locationData.getName());
        //holder.tvCountry.setText(locationData.getCountry());
        holder.cardView.setOnClickListener((view)->{
            if(onLoadMoreListener!=null) onLoadMoreListener.onItemClick(locationData);

        });
        if(onLoadMoreListener != null && holder.getAdapterPosition() == getItemCount() - 1) {
            //System.out.println("onLoadMore "+(getItemCount()+2)/10);
            onLoadMoreListener.onLoadMore(((getItemCount()+1)/10)+1);
        }

                Glide
                .with(App.getInstance().getApplicationContext())
                .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRpxVKdPQtC6CVnZ7iV4P_pOVyG2ayHRVHRgw&usqp=CAU0")
                .centerCrop()
                .placeholder(R.drawable.common_full_open_on_phone)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return marketList.size();
    }

    public void setData(List<LocationData> data) {
        this.marketList.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvCount;
        public CardView cardView;
        public ImageView imageView;
        public ViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvCount = view.findViewById(R.id.tvCount);
            cardView = view.findViewById(R.id.cardView);
            imageView = view.findViewById(R.id.imageView);
        }
    }

    public interface RecycleViewInterface {
        void onLoadMore(int page);
        void onItemClick(LocationData locationData);
    }
}
