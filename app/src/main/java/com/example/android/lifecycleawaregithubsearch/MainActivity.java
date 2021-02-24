package com.example.android.lifecycleawaregithubsearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.android.lifecycleawaregithubsearch.data.GitHubRepo;
import com.example.android.lifecycleawaregithubsearch.data.LoadingStatus;
import com.example.android.lifecycleawaregithubsearch.data.Apps;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GitHubSearchAdapter.OnSearchResultClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SEARCH_RESULTS_LIST_KEY = "MainActivity.searchResultsList";

    private RecyclerView searchResultsRV;
    private EditText searchBoxET;
    private ProgressBar loadingIndicatorPB;
    private TextView errorMessageTV;

    private GitHubSearchAdapter githubSearchAdapter;
    private GitHubSearchViewModel githubSearchViewModel;

    private RequestQueue requestQueue;

    private String searchQuery;

    private ArrayList<GitHubRepo> searchResultsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.searchBoxET = findViewById(R.id.et_search_box);
        this.searchResultsRV = findViewById(R.id.rv_search_results);
        this.loadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        this.errorMessageTV = findViewById(R.id.tv_error_message);

        this.searchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        this.searchResultsRV.setHasFixedSize(true);

        this.githubSearchAdapter = new GitHubSearchAdapter(this);
        this.searchResultsRV.setAdapter(this.githubSearchAdapter);

//        this.requestQueue = Volley.newRequestQueue(this);

        this.githubSearchViewModel = new ViewModelProvider(this)
                .get(GitHubSearchViewModel.class);

        this.githubSearchViewModel.getSearchResults().observe(
                this,
                new Observer<List<Apps>>() {
                    @Override
                    public void onChanged(List<Apps> apps) {
                        if(apps != null) {
                            List<Apps> appList = new ArrayList<Apps>();
                            for (int i = 0; i < apps.size(); i++) {
                                if (apps.get(i).name.contains(searchQuery)) {
                                    appList.add(apps.get(i));
                                }
                            }
                            githubSearchAdapter.updateSearchResults(appList);
                        }
                    }
                }
        );

        this.githubSearchViewModel.getLoadingStatus().observe(
                this,
                new Observer<LoadingStatus>() {
                    @Override
                    public void onChanged(LoadingStatus loadingStatus) {
                        if (loadingStatus == LoadingStatus.LOADING) {
                            loadingIndicatorPB.setVisibility(View.VISIBLE);
                        } else if (loadingStatus == LoadingStatus.SUCCESS) {
                            loadingIndicatorPB.setVisibility(View.INVISIBLE);
                            searchResultsRV.setVisibility(View.VISIBLE);
                            errorMessageTV.setVisibility(View.INVISIBLE);
                        } else {
                            loadingIndicatorPB.setVisibility(View.INVISIBLE);
                            searchResultsRV.setVisibility(View.INVISIBLE);
                            errorMessageTV.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );

        Button searchButton = (Button)findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchQuery = searchBoxET.getText().toString();
                githubSearchViewModel.loadSearchResults();
                if (!TextUtils.isEmpty(searchQuery)) {
//                    doGitHubSearch(searchQuery);
                }
            }
        });

//        if (savedInstanceState != null && savedInstanceState.containsKey(SEARCH_RESULTS_LIST_KEY)) {
//            this.searchResultsList = (ArrayList) savedInstanceState.getSerializable(SEARCH_RESULTS_LIST_KEY);
//            this.githubSearchAdapter.updateSearchResults(this.searchResultsList);
//        }
    }

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        Log.d(TAG, "onSaveInstanceState()");
//        super.onSaveInstanceState(outState);
//        if (this.searchResultsList != null) {
//            outState.putSerializable(SEARCH_RESULTS_LIST_KEY, this.searchResultsList);
//        }
//    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }

//    private void doGitHubSearch(String query) {
//        String url = GitHubUtils.buildGitHubSearchURL(query);
//        Log.d(TAG, "searching with this URL: " + url);
//
//        StringRequest req = new StringRequest(
//                Request.Method.GET,
//                url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        searchResultsList = GitHubUtils.parseGitHubSearchResults(response);
//                        githubSearchAdapter.updateSearchResults(searchResultsList);
//                        loadingIndicatorPB.setVisibility(View.INVISIBLE);
//                        searchResultsRV.setVisibility(View.VISIBLE);
//                        errorMessageTV.setVisibility(View.INVISIBLE);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                        loadingIndicatorPB.setVisibility(View.INVISIBLE);
//                        searchResultsRV.setVisibility(View.INVISIBLE);
//                        errorMessageTV.setVisibility(View.VISIBLE);
//                    }
//                }
//        );
//
//        loadingIndicatorPB.setVisibility(View.VISIBLE);
//        this.requestQueue.add(req);
//    }

    @Override
    public void onSearchResultClicked(Apps repo) {
        Log.d(TAG, "Search result clicked: " + repo.name);
        Intent intent = new Intent(this, RepoDetailActivity.class);
        intent.putExtra(RepoDetailActivity.EXTRA_GITHUB_REPO, repo);
        startActivity(intent);
    }
}