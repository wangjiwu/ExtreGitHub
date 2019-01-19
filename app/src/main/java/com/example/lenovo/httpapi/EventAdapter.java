package com.example.lenovo.httpapi;


import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    ArrayList<Event> mList = new ArrayList<>();
    private Context mcontext = null;

    public void  initContext (Context mcontext){
        this.mcontext = mcontext;

    }

    private EventAdapter.OnItemClickListener mOnItemClickListener;

    private static final EventAdapter mInstance = new EventAdapter();

    public static EventAdapter getInstance() {
        return mInstance;
    }

    private EventAdapter() {
        //mList.add(new Repo("1", "1", "1",1));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.eventitem, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.creattime.setText((CharSequence) mList.get(position).getCreated_at().substring(0, 10)+ "  " +  mList.get(position).getCreated_at().substring(11, 19));
        holder.actionerName.setText((CharSequence) mList.get(position).getActors().getLogin());
        holder.actions.setText((CharSequence) mList.get(position).getType());
        holder.repoName.setText((CharSequence) mList.get(position).getRepos().getName());
        try {
            holder.detail.setText((CharSequence) mList.get(position).getPayloads().getIssue().getBody());

        } catch (Exception e) {

            try {
                holder.detail.setText((CharSequence) mList.get(position).getPayloads().getCommits().getMessage());

            } catch (Exception e2) {

            }

        }
        Picasso.with(mcontext).load(mList.get(position).getActors().getUrl())
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
        TextView creattime,actionerName,actions,repoName,detail ;
        ImageView actionIcon;

        public ViewHolder(View view) {
            super(view);
            creattime = view.findViewById(R.id.creattime);
            actionerName = view.findViewById(R.id.actionerName);
            actions = view.findViewById(R.id.actions);
            repoName = view.findViewById(R.id.repoName);
            actionIcon = view.findViewById(R.id.actionIcon);
            detail = view.findViewById(R.id.detail);
        }
    }

    public void setOnItemClickListener(EventAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public Event getItem(int pos) {
        return mList.get(pos);
    }

    public void addObjects( ArrayList<Event> objs){
        mList = objs;
        notifyDataSetChanged();
    }

    public void cleanList() {
        this.mList.clear();
        notifyDataSetChanged();
    }
}
