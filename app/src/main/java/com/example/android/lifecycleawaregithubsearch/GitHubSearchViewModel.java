package com.example.android.lifecycleawaregithubsearch;

import com.example.android.lifecycleawaregithubsearch.data.GitHubRepo;
import com.example.android.lifecycleawaregithubsearch.data.GitHubSearchRepository;
import com.example.android.lifecycleawaregithubsearch.data.LoadingStatus;
import com.example.android.lifecycleawaregithubsearch.data.apps;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class GitHubSearchViewModel extends ViewModel {
    private GitHubSearchRepository repository;
    private LiveData<List<apps>> searchResults;
    private LiveData<LoadingStatus> loadingStatus;

    public GitHubSearchViewModel() {
        this.repository = new GitHubSearchRepository();
        this.searchResults = this.repository.getSearchResults();
        this.loadingStatus = this.repository.getLoadingStatus();
    }

    public LiveData<List<apps>> getSearchResults() {
        return this.searchResults;
    }

    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }

    public void loadSearchResults() {
        this.repository.loadSearchResults();
    }
}
