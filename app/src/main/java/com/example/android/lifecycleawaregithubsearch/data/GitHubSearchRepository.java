package com.example.android.lifecycleawaregithubsearch.data;

import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GitHubSearchRepository {
    private static final String TAG = GitHubSearchRepository.class.getSimpleName();
    private static final String BASE_URL = "https://api.steampowered.com/";

    private MutableLiveData<List<apps>> searchResults;
    private MutableLiveData<LoadingStatus> loadingStatus;

    private String currentQuery;

    private GitHubService gitHubService;

    public GitHubSearchRepository() {
        this.searchResults = new MutableLiveData<>();
        this.searchResults.setValue(null);

        this.loadingStatus = new MutableLiveData<>();
        this.loadingStatus.setValue(LoadingStatus.SUCCESS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.gitHubService = retrofit.create(GitHubService.class);
    }

    public LiveData<List<apps>> getSearchResults() {
        return this.searchResults;
    }

    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }

    public void loadSearchResults() {
        if (1 == 1) {
            //this.currentQuery = query;
            this.searchResults.setValue(null);
            this.loadingStatus.setValue(LoadingStatus.LOADING);
            Log.d(TAG, "running new search for this query: ");
            Call<GitHubSearchResults> results = this.gitHubService.searchRepos();
            results.enqueue(new Callback<GitHubSearchResults>() {
                @Override
                public void onResponse(Call<GitHubSearchResults> call, Response<GitHubSearchResults> response) {
                    if (response.code() == 200) {
                        Log.d(TAG, "GOT STUFF");
                        searchResults.setValue(response.body().apps.apps);
                        loadingStatus.setValue(LoadingStatus.SUCCESS);
                        Log.d(TAG, "GOT STUFF" + response.body().apps.apps);
                    } else {
                        loadingStatus.setValue(LoadingStatus.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<GitHubSearchResults> call, Throwable t) {
                    t.printStackTrace();
                    loadingStatus.setValue(LoadingStatus.ERROR);
                }
            });
        } else {
            //Log.d(TAG, "using cached results for this query: " + query);
        }
    }

    private boolean shouldExecuteSearch(String query) {
        return !TextUtils.equals(query, this.currentQuery)
                || this.loadingStatus.getValue() == LoadingStatus.ERROR;
    }
}
