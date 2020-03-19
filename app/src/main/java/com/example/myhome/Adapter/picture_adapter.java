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

import com.example.myhome.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class picture_adapter extends RecyclerView.Adapter<picture_adapter.ViewHolder>{
    Context c;
    ArrayList<Uri> list= new ArrayList<Uri>();

    public picture_adapter(Context c, ArrayList<Uri> list) {
        this.c = c;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inf= LayoutInflater.from(viewGroup.getContext());
        View view=inf.inflate(R.layout.one_item_pic,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Picasso.with(c).load(list.get(i)).resize(400, 250)
                .centerCrop().into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.one_item_picture);
        }
    }
    public interface OnItemClickedListener {
        void onItemClick(String ID, String Name);
    }

    private one_item_text_Adapter.OnItemClickedListener onItemClickedListener;

    public void setOnItemClickedListener(one_item_text_Adapter.OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }
}
