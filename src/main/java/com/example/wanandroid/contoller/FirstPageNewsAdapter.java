package com.example.wanandroid.contoller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wanandroid.R;
import com.example.wanandroid.javabean.NewsBean;

import java.util.List;

/**
 * Created by zhangchong on 18-3-19.
 */
public class FirstPageNewsAdapter  extends RecyclerView.Adapter<FirstPageNewsAdapter.NewsHolder>{

    private Context context;
    private List<NewsBean> newsBeens;
    private ItemClickListener itemClickListener;

    public FirstPageNewsAdapter(Context context, List<NewsBean> newsBeens)
    {
        this.context = context;
        this.newsBeens = newsBeens;
    }


    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public int getItemCount() {
        return newsBeens.size();
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, final int position) {
        if(getItemCount() == 0) {
            return;
        }
        NewsBean newsBean = newsBeens.get(position);
        holder.txtName.setText(newsBean.getAuthor());
        holder.txtSubject.setText(newsBean.getChapterName());
        holder.txtContent.setText(newsBean.getTitle());
        holder.txtDate.setText(newsBean.getNiceDate());
        if(newsBean.isCollect() == true)
        {
            holder.imgLoved.setImageResource(R.drawable.ic_love_on);
        }
        else
        {
            holder.imgLoved.setImageResource(R.drawable.ic_love_off);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemClickListener != null)
                {
                    itemClickListener.onItemClick(view, position);
                }
            }
        });
    }

    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    class NewsHolder extends RecyclerView.ViewHolder
    {
        TextView txtName;
        TextView txtDate;
        TextView txtContent;
        TextView txtSubject;
        ImageView imgPerson;
        ImageView imgLoved;

        public NewsHolder(View itemView) {
            super(itemView);
            txtName = (TextView)itemView.findViewById(R.id.txt_item_news_person);
            txtDate = (TextView)itemView.findViewById(R.id.txt_item_news_date);
            txtContent = (TextView)itemView.findViewById(R.id.txt_item_news_content);
            txtSubject = (TextView)itemView.findViewById(R.id.txt_item_news_subject);
            imgPerson = (ImageView)itemView.findViewById(R.id.img_item_news_person);
            imgLoved = (ImageView)itemView.findViewById(R.id.img_item_news_loved);
        }
    }
}
