package com.collge.afile.ui;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.collge.afile.R;
import com.collge.afile.pojo.Item;
import com.collge.afile.util.FileType;
import com.collge.afile.util.ImageLoader;

import java.io.File;
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
        Item item = itemList.get(position);

        if(item.isSelected())
            holder.parent.setBackgroundColor(context.getResources().getColor(R.color.gray));
        else
            holder.parent.setBackgroundColor(context.getResources().getColor(R.color.white));

        holder.countryName.setText(item.getFile().trim());
        String path =  ((FileActivity) context).getPath() + File.separator + item.getFile();
        if(item.getType() == FileType.FOLDER)
            ImageLoader.getInstance().loadImage(R.mipmap.folder_empty, holder.countryPhoto);
        else
            ImageLoader.getInstance().loadImage(Uri.fromFile(new File(path)), getPlaceHolder(item.getType()), R.mipmap.file, holder.countryPhoto);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    private int getPlaceHolder(int fileType){
        switch (fileType){
            case FileType.FILE:
                return R.mipmap.file;

            case FileType.FOLDER:
                return R.mipmap.folder_empty;
        }
        return FileType.FILE;
    }
}
