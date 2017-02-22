package com.collge.afile.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.collge.afile.R;

public class FileViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public RelativeLayout parent;
    public TextView countryName;
    public ImageView countryPhoto;

    public FileViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        parent = (RelativeLayout)itemView.findViewById(R.id.parent);
        countryName = (TextView)itemView.findViewById(R.id.country_name);
        countryPhoto = (ImageView)itemView.findViewById(R.id.country_photo);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }
}