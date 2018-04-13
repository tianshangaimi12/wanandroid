package com.example.wanandroid.contoller;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wanandroid.MainApplication;
import com.example.wanandroid.R;
import com.example.wanandroid.javabean.CollectNewsBean;
import com.example.wanandroid.utils.RetrofitUtils;


import java.util.List;


/**
 * Created by zhangchong on 18-4-13.
 */
public class CollectPageAdapter extends RecyclerView.Adapter<CollectPageAdapter.NewsHolder>{
    private Context context;
    private List<CollectNewsBean> newsBeens;
    private CollectItemClickListener itemClickListener;
    private RetrofitUtils retrofitUtils;

    private final String TAG = "FirstPageNewsAdapter";

    public CollectPageAdapter(Activity activity, List<CollectNewsBean> newsBeens)
    {
        this.context = activity;
        this.newsBeens = newsBeens;
        retrofitUtils = MainApplication.retrofitUtils;

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
    public void onBindViewHolder(final NewsHolder holder, final int position) {
        if(getItemCount() == 0) {
            return;
        }
        CollectNewsBean newsBean = newsBeens.get(position);
        holder.txtName.setText(newsBean.getAuthor());
        holder.txtSubject.setText(newsBean.getChapterName());
        holder.txtContent.setText(newsBean.getTitle());
        holder.txtDate.setText(newsBean.getNiceDate());
        holder.imgLoved.setImageResource(R.drawable.ic_love_on);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemClickListener != null)
                {
                    itemClickListener.onItemClick(view, position);
                }
            }
        });
        holder.imgLoved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemClickListener != null){
                    itemClickListener.onImgClick(view, position);
                }
            }
        });
    }

    public void setItemClickListener(CollectItemClickListener itemClickListener)
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
