package com.example.wanandroid.contoller;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wanandroid.MainActivity;
import com.example.wanandroid.MainApplication;
import com.example.wanandroid.R;
import com.example.wanandroid.javabean.NewsBean;
import com.example.wanandroid.utils.RetrofitUtils;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zhangchong on 18-3-19.
 */
public class FirstPageNewsAdapter  extends RecyclerView.Adapter<FirstPageNewsAdapter.NewsHolder>{

    private Context context;
    private List<NewsBean> newsBeens;
    private ItemClickListener itemClickListener;
    private RetrofitUtils retrofitUtils;

    private final String TAG = "FirstPageNewsAdapter";

    public FirstPageNewsAdapter(Activity activity, List<NewsBean> newsBeens)
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
        holder.imgLoved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doCollect(holder, position);
            }
        });
    }

    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    /**
     * 收藏和取消收藏文章
     * @param position
     */
    public void doCollect(final NewsHolder newsHolder, int position)
    {
        final NewsBean newsBean = newsBeens.get(position);
        if(newsBean.isCollect())
        {
            Call<ResponseBody> disCollect = retrofitUtils.disCollectInMessage(newsBean.getId());
            disCollect.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responseString = response.body().string();
                        Log.d(TAG, "response:"+responseString);
                        JSONObject jsonObject = new JSONObject(responseString);
                        int errorCode = jsonObject.optInt("errorCode");
                        String errorMsg = jsonObject.optString("errorMsg");
                        if(errorCode == 0)
                        {
                            newsBean.setCollect(false);
                            newsHolder.imgLoved.setImageResource(R.drawable.ic_love_off);
                        }
                        else
                        {
                            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d(TAG, t.getMessage());
                }
            });
        }
        else{
            Call<ResponseBody> collect = retrofitUtils.collectInMessage(newsBean.getId());
            collect.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responseString = response.body().string();
                        Log.d(TAG, "response:"+responseString);
                        JSONObject jsonObject = new JSONObject(responseString);
                        int errorCode = jsonObject.optInt("errorCode");
                        String errorMsg = jsonObject.optString("errorMsg");
                        if(errorCode == 0)
                        {
                            newsBean.setCollect(true);
                            newsHolder.imgLoved.setImageResource(R.drawable.ic_love_on);
                        }
                        else
                        {
                            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d(TAG, t.getMessage());
                }
            });
        }
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
