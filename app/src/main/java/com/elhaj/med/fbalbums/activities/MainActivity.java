package com.elhaj.med.fbalbums.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elhaj.med.fbalbums.R;
import com.elhaj.med.fbalbums.adapters.AlbumGridAdapter;
import com.elhaj.med.fbalbums.models.Album;
import com.elhaj.med.fbalbums.models.Photo;
import com.elhaj.med.fbalbums.utilities.AppLifeCycleService;
import com.elhaj.med.fbalbums.utilities.DownloadImageTask;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    LoginButton loginButton;
    ImageView imgProfileUser;
    TextView nameUserTxt;
    Button showALbumButton;

    String userId;
    List<Album> fbalbum;
    ArrayList<Photo> fbphoto;

    LinearLayout profileLayout;
    LinearLayout buttonLayout;
    LinearLayout gridLayout;

    GridView albumGrid;

    Menu menu;
    MenuItem itemLogOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(getBaseContext(), AppLifeCycleService.class));

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        imgProfileUser = (ImageView)findViewById(R.id.imgProfile);
        nameUserTxt = (TextView)findViewById(R.id.txt_nameUser);
        showALbumButton = (Button)findViewById(R.id.show_album_button);


        profileLayout = (LinearLayout) findViewById(R.id.layout_profile);
        buttonLayout = (LinearLayout) findViewById(R.id.layout_button);
        gridLayout = (LinearLayout) findViewById(R.id.layout_grid);

        albumGrid = (GridView) findViewById(R.id.grid_album);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfoFBAcount();
            }
        });

        showALbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileLayout.setVisibility(View.GONE);
                buttonLayout.setVisibility(View.GONE);
                gridLayout.setVisibility(View.VISIBLE);
                itemLogOut.setVisible(true);
            }
        });

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void getInfoFBAcount(){
        loginButton.setReadPermissions("public_profile", "email","user_photos");
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                userId = loginResult.getAccessToken().getUserId().toString();

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    setUserProfile(object);
                                    GraphRequest request = new GraphRequest(
                                            AccessToken.getCurrentAccessToken(),
                                            "/me/albums?fields=id,name,picture,photo_count",
                                            null,
                                            HttpMethod.GET,
                                            new GraphRequest.Callback() {
                                                public void onCompleted(GraphResponse response) {
                                                    String albumID = null;
                                                    String albumName=null;
                                                    String albumUrl=null;
                                                    String albumCount=null;
                                                    try{
                                                        JSONObject json = response.getJSONObject();
                                                        JSONArray jarray = json.getJSONArray("data");
                                                        fbalbum = new ArrayList<>();
                                                        for(int i = 0; i < jarray.length(); i++) {
                                                            JSONObject oneAlbum = jarray.getJSONObject(i);
                                                            albumID = oneAlbum.getString("id");
                                                            albumName=oneAlbum.getString("name");
                                                            albumCount=oneAlbum.getString("photo_count");
                                                            albumUrl=oneAlbum.getJSONObject("picture").getJSONObject("data").getString("url");
                                                            Album fba = new Album();
                                                            fba.setId(albumID);
                                                            fba.setName(albumName);
                                                            fba.setUrl(albumUrl);
                                                            fba.setCount(albumCount);
                                                            fbalbum.add(fba);

                                                        }
                                                        AlbumGridAdapter albumAdp = new AlbumGridAdapter(getApplicationContext(), R.layout.album_grid, fbalbum);
                                                        albumGrid.setAdapter(albumAdp);
                                                    }
                                                    catch(JSONException e){
                                                        e.printStackTrace();
                                                    }
                                                }}
                                    );
                                    request.executeAsync();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,first_name,last_name,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();

                albumGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        TextView title=(TextView)view.findViewById(R.id.album_title);
                        final String chosenAlbumName = title.getText().toString();
                        GraphRequest requestPhotos = new GraphRequest(
                                AccessToken.getCurrentAccessToken(),
                                "/me?fields=albums.fields(id,name,cover_photo,photos.fields(name,picture,source))",
                                null,
                                HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {
                                        String albumName=null;
                                        String photoUrl=null;

                                        try{
                                            JSONObject json = response.getJSONObject().getJSONObject("albums");

                                            JSONArray jarray = json.getJSONArray("data");
                                            fbphoto = new ArrayList<>();

                                            for(int i = 0; i < jarray.length(); i++) {

                                                JSONObject oneAlbum = jarray.getJSONObject(i);
                                                albumName=oneAlbum.getString("name");

                                                if(albumName.equals(chosenAlbumName)){

                                                    JSONArray dataphotoUrl=oneAlbum.getJSONObject("photos").getJSONArray("data");
                                                    for(int j =0;j< dataphotoUrl.length();j++){
                                                        JSONObject onephoto = dataphotoUrl.getJSONObject(j);
                                                        photoUrl=onephoto.getString("picture");
                                                        Photo ph=new Photo();
                                                        ph.setUrlPhoto(photoUrl);

                                                        fbphoto.add(ph);

                                                    }
                                                }}
                                            Intent intent = new Intent(getApplicationContext(), PhotosAlbumActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putParcelableArrayList("fbphotoList", fbphoto);
                                            bundle.putString("AlbumName", chosenAlbumName);
                                            intent.putExtras(bundle);
                                            startActivity(intent);



                                        }
                                        catch(JSONException e){
                                            e.printStackTrace();
                                        }
                                    }}
                        );
                        requestPhotos.executeAsync();


                    }
                });
            }
            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    public void setUserProfile(JSONObject object) throws JSONException {

        String first_name,last_name;
        first_name="";
        last_name="";


        try {
            first_name=object.getString("first_name");
            last_name=object.getString("last_name");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        nameUserTxt.setText(first_name+"  "+last_name);
        DownloadImageTask downloadImageTask=new DownloadImageTask(imgProfileUser);
        downloadImageTask.execute("https://graph.facebook.com/"+userId+"/picture?type=large");
        buttonLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        itemLogOut = this.menu.findItem(R.id.action_logOut);
        itemLogOut.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logOut) {
            disconnectFromFacebook();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
        startActivity(new Intent(MainActivity.this, MainActivity.class));
    }

}
