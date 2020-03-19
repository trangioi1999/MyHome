package com.example.myhome.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myhome.Model.House;
import com.example.myhome.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class house_adapter extends  RecyclerView.Adapter<house_adapter.ViewHolder>{
    Context context;
    ArrayList<House> houses= new ArrayList<House>();

    public house_adapter(Context context, ArrayList<House> houses) {
        this.context = context;
        this.houses = houses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inf= LayoutInflater.from(viewGroup.getContext());
        View view=inf.inflate(R.layout.one_item_house,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Picasso.with(context).load(houses.get(i).img).into(viewHolder.room_img);
        viewHolder.room_address.setText("Địa Chỉ: "+houses.get(i).address);
        viewHolder.room_price.setText("Giá Phòng: "+houses.get(i).price);
        viewHolder.room_detail.setText("Mô Tả: "+houses.get(i).detail);
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickedListener != null) {
                    onItemClickedListener.onItemClick(houses.get(i).ID);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return houses.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView room_img;
        TextView room_address,room_detail,room_price;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            room_img=itemView.findViewById(R.id.room_img);
            room_address=itemView.findViewById(R.id.room_address);
            room_price=itemView.findViewById(R.id.room_price);
            room_detail=itemView.findViewById(R.id.room_detail);
            layout=itemView.findViewById(R.id.one_item_room);
        }
    }

    public interface OnItemClickedListener {
        void onItemClick(String ID);
    }

    private OnItemClickedListener onItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }
}

