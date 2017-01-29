package com.collge.afile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileViewHolders> {

    private List<Item> itemList;
    private Context context;

    public FileAdapter(Context context, List<Item> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public FileViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout = ((FileActivity) context).isViewManagerType() ? R.layout.layout_list_view : R.layout.layout_grid_view;

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        FileViewHolders rcv = new FileViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(FileViewHolders holder, int position) {
        holder.countryName.setText(itemList.get(position).toString().trim());
        holder.countryPhoto.setImageResource(itemList.get(position).icon);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
