package com.elhaj.med.fbalbums.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.elhaj.med.fbalbums.R;
import com.elhaj.med.fbalbums.adapters.PhotoGridAdapter;
import com.elhaj.med.fbalbums.models.Photo;

import java.util.ArrayList;

public class PhotosAlbumActivity extends AppCompatActivity {

    private GridView gv;
    private ArrayList<Photo> arraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_album);

        Bundle bundle = getIntent().getExtras();
        arraylist = bundle.getParcelableArrayList("fbphotoList");

        gv=(GridView)findViewById(R.id.photos_grid);
        gv.setAdapter(new PhotoGridAdapter(this, arraylist));

    }
}
