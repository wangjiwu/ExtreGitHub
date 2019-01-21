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


public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.ViewHolder> {

    ArrayList<Issue> mList = new ArrayList<>();
    private Context mcontext = null;

    public void  initContext (Context mcontext){
        this.mcontext = mcontext;

    }

    private IssueAdapter.OnItemClickListener mOnItemClickListener;

    private static final IssueAdapter mInstance = new IssueAdapter();

    public static IssueAdapter getInstance() {
        mInstance.cleanList();
        return mInstance;
    }

    private IssueAdapter() {
        //mList.add(new Issue());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.issueitem, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.repo_title_text.setText((CharSequence) mList.get(position).getTitle());
        holder.create_time_text.setText((CharSequence) mList.get(position).getCreated_at().substring(0, 10)+ "  " +  mList.get(position).getCreated_at().substring(11, 19));

        holder.prob_state_text.setText(String.valueOf( mList.get(position).getState()));
        holder.issue_detail_text.setText((CharSequence) mList.get(position).getBody());
        try {

            Picasso.with(mcontext).load(mList.get(position).getUser().getAvatar_url())
                    .noFade()
                    .into( holder.repo_icon);
        } catch (Exception e) {

        }

        holder.issue_comment_number.setText((CharSequence) mList.get(position).getComments());

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
        TextView repo_title_text, create_time_text,prob_state_text,issue_detail_text,issue_comment_number ;
        ImageView repo_icon;

        public ViewHolder(View view) {
            super(view);
            repo_title_text = view.findViewById(R.id.repo_title_text);
            create_time_text = view.findViewById(R.id.create_time_text);
            prob_state_text = view.findViewById(R.id.prob_state_text);
            issue_detail_text = view.findViewById(R.id.issue_detail_text);
            repo_icon = view.findViewById(R.id.repo_icon);
            issue_comment_number = view.findViewById(R.id.issue_comment_number);
        }
    }

    public void setOnItemClickListener(IssueAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public Issue getItem(int pos) {
        return mList.get(pos) ;
    }

    public void addObjects( ArrayList<Issue> objs){
        mList = objs;
        notifyDataSetChanged();
    }
    public void addObject( Issue obj){
        mList.add(obj);
        notifyDataSetChanged();
    }
    public void cleanList() {
        this.mList.clear();
        notifyDataSetChanged();
    }

}
