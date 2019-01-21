package com.example.lenovo.httpapi;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class GistAdapter extends RecyclerView.Adapter<GistAdapter.ViewHolder> {

    ArrayList<Gist> mList = new ArrayList<>();
    private Context mcontext = null;

    public void  initContext (Context mcontext){
        this.mcontext = mcontext;

    }

    private GistAdapter.OnItemClickListener mOnItemClickListener;

    private static final GistAdapter mInstance = new GistAdapter();

    public static GistAdapter getInstance() {
        mInstance.cleanList();
        return mInstance;
    }

    private GistAdapter() {
        //mList.add(new Repo("1", "1", "1",1));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gistitem, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.creattime.setText((CharSequence) mList.get(position).getCreated_at().substring(0, 10)+ "  " +  mList.get(position).getCreated_at().substring(11, 19));
        holder.updateTime.setText((CharSequence) mList.get(position).getUpdated_at().substring(0, 10)+ "  " +  mList.get(position).getUpdated_at().substring(11, 19));
        holder.despretion.setText((CharSequence) mList.get(position).getDescription());
        holder.git_comment_number2.setText((CharSequence) mList.get(position).getComments());
        Picasso.with(mcontext).load(mList.get(position).getOwner().getAvatar_url())
                .noFade()
                .into( holder.actionIcon);

        if( mOnItemClickListener!= null){
            holder.itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            });
            holder.itemView.setOnLongClickListener( new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(v, position);
                    return false;
                }
            });
        }

    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView creattime,updateTime,despretion ,git_comment_number2 ;
        ImageView actionIcon;

        public ViewHolder(View view) {
            super(view);
            creattime = view.findViewById(R.id.creattime);
            updateTime = view.findViewById(R.id.updateTime);
            despretion = view.findViewById(R.id.despretion);
            git_comment_number2 = view.findViewById(R.id.git_comment_number2);
            actionIcon = view.findViewById(R.id.actionIcon);
        }
    }

    public void setOnItemClickListener(GistAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public Gist getItem(int pos) {
        return mList.get(pos);
    }

    public void addObjects( ArrayList<Gist> objs){
        mList = objs;
        notifyDataSetChanged();
    }

    public void cleanList() {
        this.mList.clear();
        notifyDataSetChanged();
    }
}
