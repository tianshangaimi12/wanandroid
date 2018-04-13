package com.example.wanandroid.contoller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wanandroid.R;
import com.example.wanandroid.javabean.SystemBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhangchong on 18-3-20.
 */
public class SystemItemAdapter extends RecyclerView.Adapter<SystemItemAdapter.ItemHolder>{

    private Context context;
    private SystemBean systemBean;
    private List<ItemHolder> itemHolders;
    private ItemClickListener itemClickListener;

    public SystemItemAdapter(Context context, SystemBean systemBean)
    {
        this.context = context;
        this.systemBean = systemBean;
        itemHolders = new ArrayList<>();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_system_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {
        final SystemBean.SystemClass systemClass = systemBean.getData().get(position);
        holder.txtTitle.setText(systemClass.getName());
        if(position == systemBean.getIndex())
        {
            holder.imgIndex.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.imgIndex.setVisibility(View.INVISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemClickListener != null) {
                    itemClickListener.onItemClick(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return systemBean.getData().size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    class ItemHolder extends RecyclerView.ViewHolder
    {
        TextView txtTitle;
        ImageView imgIndex;

        public ItemHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView)itemView.findViewById(R.id.txt_system_item_title);
            imgIndex = (ImageView)itemView.findViewById(R.id.img_system_item_index);
        }
    }
}
