package com.elhaj.med.fbalbums.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.elhaj.med.fbalbums.R;
import com.elhaj.med.fbalbums.utilities.DownloadImageTask;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
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
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        imgProfileUser = (ImageView)findViewById(R.id.imgProfile);
        nameUserTxt = (TextView)findViewById(R.id.txt_nameUser);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfoFBAcount();
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

    }
}
