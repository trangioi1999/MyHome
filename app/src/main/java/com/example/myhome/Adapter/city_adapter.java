package com.example.myhome.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myhome.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class city_adapter extends RecyclerView.Adapter<city_adapter.ViewHolder> {
    Context context;
    ArrayList<Integer> pic = new ArrayList<Integer>();
    ArrayList<String> id = new ArrayList<String>();
    ArrayList<String> name = new ArrayList<String>();

    public city_adapter(Context context, ArrayList<Integer> pic, ArrayList<String> id, ArrayList<String> name) {
        this.context = context;
        this.pic = pic;
        this.id = id;
        this.name = name;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inf= LayoutInflater.from(viewGroup.getContext());
        View view=inf.inflate(R.layout.one_item_city,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.name_city.setText(name.get(i));
        Picasso.with(context).load(pic.get(i)).resize(200, 140)
                .centerCrop().into(viewHolder.img_city);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickedListener != null) {
                    onItemClickedListener.onItemClick(id.get(i), name.get(i));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return id.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView img_city;
        TextView name_city;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_city=itemView.findViewById(R.id.img_city);
            name_city=itemView.findViewById(R.id.name_city);
            cardView=itemView.findViewById(R.id.one_item_city);
        }
    }
    public interface OnItemClickedListener {
        void onItemClick(String ID, String Name);
    }

    private OnItemClickedListener onItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }
}
