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


public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.ViewHolder> {

    private ArrayList<Repo> mList = new ArrayList<>();
    private Context mcontext = null;

    private RepoAdapter.OnItemClickListener mOnItemClickListener;

    private static final RepoAdapter mInstance = new RepoAdapter();

    public static RepoAdapter getInstance() {
        return mInstance;
    }

    private RepoAdapter() {
        //mList.add(new Repo("1", "1", "1",1));
    }

    public void  initContext (Context mcontext){
        this.mcontext = mcontext;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.repoitem, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (mList.get(position).getName().length() < 20) {
            holder.repo_name_text.setText((CharSequence) mList.get(position).getName());
        } else {
            holder.repo_name_text.setText( mList.get(position).getName().substring(0,19) + "...");
        }


        holder.repo_id_test.setText(mList.get(position).getId());
        holder.prob_num_text.setText(String.valueOf( mList.get(position).getOpen_issues()));
        holder.repo_detail_text.setText((CharSequence) mList.get(position).getDescription());
        Picasso.with(mcontext).load(mList.get(position).getOwner().getAvatar_url())
                .noFade()
                .into( holder.user_image);
        holder.repo_language.setText((CharSequence) mList.get(position).getLanguage());
        holder.repo_starnum.setText((CharSequence) mList.get(position).getStargazers_count());
        holder.repo_forknum.setText((CharSequence) mList.get(position).getForks_count());
        holder.repo_watchernum.setText((CharSequence) mList.get(position).getWatchers());
        holder.repo_undate_time.setText((CharSequence) mList.get(position).getUpdated_at().substring(0, 10)+ "  " +  mList.get(position).getUpdated_at().substring(11, 19));



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
        TextView repo_name_text, repo_id_test,prob_num_text,repo_detail_text,
                repo_language, repo_starnum,repo_forknum,repo_watchernum,repo_undate_time ;
        ImageView user_image;

        public ViewHolder(View view) {
            super(view);
            repo_name_text = view.findViewById(R.id.repo_name_text);
            repo_id_test = view.findViewById(R.id.repo_id_test);
            prob_num_text = view.findViewById(R.id.prob_num_text);
            repo_detail_text = view.findViewById(R.id.repo_detail_text);
            user_image = view.findViewById(R.id.user_image);
            repo_language = view.findViewById(R.id.repo_language);
            repo_starnum = view.findViewById(R.id.repo_starnum);
            repo_forknum = view.findViewById(R.id.repo_forknum);
            repo_watchernum = view.findViewById(R.id.repo_watchernum);
            repo_undate_time = view.findViewById(R.id.repo_undate_time);
        }
    }

    public void setOnItemClickListener(RepoAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public Repo getItem(int pos) {
        return mList.get(pos);
    }

    public void addObjects( ArrayList<Repo> objs){
        mList = objs;
        notifyDataSetChanged();
    }

    public void cleanList() {
        this.mList.clear();
        notifyDataSetChanged();
    }
}
