package com.maku.githubapitestapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.maku.githubapitestapp.Interfaces.ApiInterface;
import com.maku.githubapitestapp.adapters.RepoListInfoAdapter;
import com.maku.githubapitestapp.models.MyRepoList;
import com.maku.githubapitestapp.models.Profile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    //widgets
    private RecyclerView mRecyclerView;
    public ImageView mImageView;
    public TextView mTextViewName;

    //vars
    private LinearLayoutManager mLinearLayoutManager;
    private RepoListInfoAdapter mProfileInfoAdapter;
    ArrayList<MyRepoList> mMyRepoLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.moviewRecyclerView);
        mTextViewName = findViewById(R.id.profileName);
        mImageView = findViewById(R.id.profileImage);

        mMyRepoLists = new ArrayList<>();

        initRecyclerview();
        getProfileInfo();
        getRepoList();
    }


    private void initRecyclerview() {
        Log.d(TAG, "initRecyclerview: ");
        mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
    }

    private void getRepoList() {
        String url = "https://api.github.com/";

        Log.d(TAG, "getMovies: url " + url);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        ApiInterface mApiInterface = retrofit.create(ApiInterface.class);
        Call<ArrayList<MyRepoList>> call = mApiInterface.getRepoList();

        call.enqueue(new Callback<ArrayList<MyRepoList>>() {
            @Override
            public void onResponse(Call<ArrayList<MyRepoList>> call, Response<ArrayList<MyRepoList>> response) {

                if (response.isSuccessful())  {
                    Log.d(TAG, "onResponse: " + response.toString());
                    processList(response);
                }else{
                    Log.d(TAG, "onResponse: there is no response");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MyRepoList>> call, Throwable t) {
                Log.e(TAG,t.toString());
            }
        });
    }

    private void processList(Response<ArrayList<MyRepoList>> response) {

        mMyRepoLists =  response.body();

        Log.d(TAG, "process: " );

        mProfileInfoAdapter = new RepoListInfoAdapter(this, mMyRepoLists);
        mRecyclerView.setAdapter(mProfileInfoAdapter);

    }


    private void getProfileInfo() {

        String url = "https://api.github.com/";

        Log.d(TAG, "getMovies: url " + url);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        ApiInterface mApiInterface = retrofit.create(ApiInterface.class);
        Call<Profile> call = mApiInterface.getProfileInfo();

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {

                if (response.isSuccessful())  {
                    Log.d(TAG, "onResponse: " + response.toString());
                    process(response);
                }else{
                    Log.d(TAG, "onResponse: there is no response");
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Log.e(TAG,t.toString());
            }
        });

    }

    private void process(Response<Profile> response) {

        String name = (String) response.body().getName();
        String img = (String) response.body().getAvatarUrl();

        Log.d(TAG, "process: " + img);

        /*attach profile to widgets*/
        mTextViewName.setText(name);
        Picasso.get().load(img).into(mImageView);

//        mProfileInfoAdapter = new RepoListInfoAdapter(this, mProfiles);
//        mRecyclerView.setAdapter(mProfileInfoAdapter);

    }

    private static OkHttpClient okClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        // Change the label of the menu based on the state of the app.
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        if(nightMode == AppCompatDelegate.MODE_NIGHT_YES){
            menu.findItem(R.id.night_mode).setTitle(R.string.day_mode);
        } else{
            menu.findItem(R.id.night_mode).setTitle(R.string.night_mode);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Check if the correct item was clicked
        if(item.getItemId()==R.id.night_mode){
            // Get the night mode state of the app.
            int nightMode = AppCompatDelegate.getDefaultNightMode();
            //Set the theme mode for the restarted activity
            if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode
                        (AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode
                        (AppCompatDelegate.MODE_NIGHT_YES);
            }
            // Recreate the activity for the theme change to take effect.
            recreate();
        }
        // TODO: Get the night mode state of the app.
        return true;
    }
}
