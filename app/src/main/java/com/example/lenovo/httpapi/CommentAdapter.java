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


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    ArrayList<Comment> mList = new ArrayList<>();
    private Context mcontext = null;

    public void  initContext (Context mcontext){
        this.mcontext = mcontext;

    }

    private CommentAdapter.OnItemClickListener mOnItemClickListener;

    private static final CommentAdapter mInstance = new CommentAdapter();

    public static CommentAdapter getInstance() {
        return mInstance;
    }

    private CommentAdapter() {
        //mList.add(new Issue());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.commentitem, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.comment_user.setText((CharSequence) mList.get(position).getUser().getLogin());
        holder.create_time.setText(mList.get(position).getCreated_at());
        holder.comment_text.setText(String.valueOf( mList.get(position).getBody()));
         try {

            Picasso.with(mcontext).load(mList.get(position).getUser().getAvatar_url())
                    .noFade()
                    .into( holder.comment_icon);
        } catch (Exception e) {

        }


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
        TextView comment_user, create_time,comment_text;
        ImageView comment_icon;

        public ViewHolder(View view) {
            super(view);
            comment_user = view.findViewById(R.id.comment_user);
            create_time = view.findViewById(R.id.create_time);
            comment_text = view.findViewById(R.id.comment_text);
            comment_icon = view.findViewById(R.id.comment_icon);

        }
    }

    public void setOnItemClickListener(CommentAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public Comment getItem(int pos) {
        return mList.get(pos) ;
    }

    public void addObjects( ArrayList<Comment> objs){
        mList = objs;
        notifyDataSetChanged();
    }
    public void addObject( Comment obj){
        mList.add(obj);
        notifyDataSetChanged();
    }
    public void cleanList() {
        this.mList.clear();
        notifyDataSetChanged();
    }

}
