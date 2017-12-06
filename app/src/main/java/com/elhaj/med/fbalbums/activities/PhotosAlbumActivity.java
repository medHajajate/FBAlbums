package com.elhaj.med.fbalbums.activities;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.elhaj.med.fbalbums.R;
import com.elhaj.med.fbalbums.adapters.PhotoGridAdapter;
import com.elhaj.med.fbalbums.models.Photo;

import java.util.ArrayList;

public class PhotosAlbumActivity extends AppCompatActivity {

    private GridView photoGrid;
    private ArrayList<Photo> photoslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_album);

        Bundle bundle = getIntent().getExtras();
        photoslist = bundle.getParcelableArrayList("fbphotoList");

        photoGrid=(GridView)findViewById(R.id.photos_grid);
        photoGrid.setAdapter(new PhotoGridAdapter(this, photoslist));

        photoGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String imageUrl = photoslist.get(i).getUrlPhoto();
                Intent intent = new Intent(getApplicationContext(), SinglePhotoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("imageUrl", imageUrl);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                    finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
