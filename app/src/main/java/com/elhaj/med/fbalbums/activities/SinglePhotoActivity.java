package com.elhaj.med.fbalbums.activities;

import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.elhaj.med.fbalbums.R;
import com.elhaj.med.fbalbums.utilities.DownloadImageTask;

public class SinglePhotoActivity extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_photo);
        imageView = (ImageView) findViewById(R.id.photo);

        Bundle bundle = getIntent().getExtras();
        String imgUrl = bundle.getString("imageUrl");
        DownloadImageTask dTask = new DownloadImageTask(imageView);
        dTask.execute(imgUrl);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
