package com.example.justin.myapplication.util;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.justin.myapplication.R;
import com.example.justin.myapplication.bean.Music;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private List<Music> ml;

    public MusicAdapter(List<Music> ml) {
        this.ml = ml;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.music_item,viewGroup,false);
        final ViewHolder vh=new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Music m=ml.get(i);
        viewHolder.tv1.setText(m.getArtist());
        viewHolder.tv2.setText(m.getTitle());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onClick(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ml.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View v;
        TextView tv1;
        TextView tv2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            v=itemView;
            tv1= itemView.findViewById(R.id.listArtist);
            tv2= itemView.findViewById(R.id.listTitle);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }

    /*
    * 暴露点击事件
    * */
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}
