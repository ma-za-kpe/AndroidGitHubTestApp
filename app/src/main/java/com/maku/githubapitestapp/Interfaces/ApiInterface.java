package com.maku.githubapitestapp.Interfaces;

import com.maku.githubapitestapp.models.MyRepoList;
import com.maku.githubapitestapp.models.Profile;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("users/ma-za-kpe")
    Call<Profile> getProfileInfo();

    @GET("users/ma-za-kpe/repos")
    Call<ArrayList<MyRepoList>> getRepoList();

}
