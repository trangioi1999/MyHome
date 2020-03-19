package com.example.myhome.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myhome.R;

import java.util.ArrayList;

public class one_item_text_Adapter extends RecyclerView.Adapter<one_item_text_Adapter.ViewHolder> {
    Context c;
    ArrayList<String> list_name = new ArrayList<String>();
    ArrayList<String> list_id = new ArrayList<String>();

    public one_item_text_Adapter(Context c, ArrayList<String> list_name,ArrayList<String> list_id) {
        this.c = c;
        this.list_name = list_name;
        this.list_id = list_id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inf= LayoutInflater.from(viewGroup.getContext());
        View view=inf.inflate(R.layout.one_item_text,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.tv.setText(list_name.get(i));
        viewHolder.line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickedListener != null) {
                    onItemClickedListener.onItemClick(list_id.get(i), list_name.get(i));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        LinearLayout line;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            tv=itemView.findViewById(R.id.one_item_text);
            line=itemView.findViewById(R.id.one_item_textline);
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
