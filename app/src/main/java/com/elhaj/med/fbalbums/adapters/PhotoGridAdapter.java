package com.elhaj.med.fbalbums.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.elhaj.med.fbalbums.R;
import com.elhaj.med.fbalbums.models.Photo;
import com.elhaj.med.fbalbums.utilities.DownloadImageTask;

import java.util.ArrayList;

/**
 * Created by med on 12/4/17.
 */

public class PhotoGridAdapter extends BaseAdapter {
    private Context context;
    private final ArrayList<Photo> mobileValues;

    public PhotoGridAdapter(Context context, ArrayList<Photo> mobileValues) {
        this.context = context;
        this.mobileValues = mobileValues;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {
            gridView = new View(context);
            gridView = inflater.inflate(R.layout.photo_grid, null);

            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.img_photo);
            DownloadImageTask dTask =new DownloadImageTask(imageView);
            dTask.execute(mobileValues.get(position).getUrlPhoto());

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return mobileValues.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}
