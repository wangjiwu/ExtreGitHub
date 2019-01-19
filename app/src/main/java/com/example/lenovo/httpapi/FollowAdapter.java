package com.example.lenovo.httpapi;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {

    ArrayList<Follower> mList = new ArrayList<>();
    private Context mcontext = null;

    public void  initContext (Context mcontext){
        this.mcontext = mcontext;

    }

    private FollowAdapter.OnItemClickListener mOnItemClickListener;

    private static final FollowAdapter mInstance = new FollowAdapter();

    public static FollowAdapter getInstance() {
        return mInstance;
    }

    private FollowAdapter() {
        //mList.add(new Repo("1", "1", "1",1));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.followitem, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.followName.setText((CharSequence) mList.get(position).getLogin());
        Picasso.with(mcontext).load(mList.get(position).getAvatar_url())
                .noFade()
                .into( holder.followIcon);
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
        TextView followName;
        ImageView followIcon;

        public ViewHolder(View view) {
            super(view);
            followIcon = view.findViewById(R.id.followIcon);
            followName = view.findViewById(R.id.followName);
        }
    }

    public void setOnItemClickListener(FollowAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public Follower getItem(int pos) {
        return mList.get(pos);
    }

    public void addObjects( ArrayList<Follower> objs){
        mList = objs;
        notifyDataSetChanged();
    }

    public void cleanList() {
        this.mList.clear();
        notifyDataSetChanged();
    }
}
