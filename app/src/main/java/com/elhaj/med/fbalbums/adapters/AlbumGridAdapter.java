package com.elhaj.med.fbalbums.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elhaj.med.fbalbums.R;
import com.elhaj.med.fbalbums.models.Album;
import com.elhaj.med.fbalbums.utilities.DownloadImageTask;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by med on 12/4/17.
 */

public class AlbumGridAdapter extends ArrayAdapter {

    private List<Album> fbList;
    private int resource;
    private LayoutInflater inflater;

    public AlbumGridAdapter(Context context, int resource, List<Album> object) {
        super(context, resource, object);
        fbList =object;
        this.resource=resource;
        inflater=(LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){

            convertView=inflater.inflate(resource,null);

        }

        TextView nameAlbum;
        nameAlbum=(TextView)convertView.findViewById(R.id.album_title);
        nameAlbum.setText(fbList.get(position).getName());

        TextView countAlbum;
        countAlbum=(TextView)convertView.findViewById(R.id.album_count);
        countAlbum.setText(fbList.get(position).getCount()+" photos ");


        ImageView imgAlbum=(ImageView)convertView.findViewById(R.id.album_image);

        DownloadImageTask dTask=new DownloadImageTask(imgAlbum);
        dTask.execute(fbList.get(position).getUrl());

        return convertView;
    }
}