package com.example.android.lifecycleawaregithubsearch.utils;

import android.net.Uri;

import com.example.android.lifecycleawaregithubsearch.data.GitHubSearchResults;
import com.example.android.lifecycleawaregithubsearch.data.Apps;
import com.google.gson.Gson;

import java.util.ArrayList;

public class GitHubUtils {
    private final static String GITHUB_SEARCH_BASE_URL = "https://api.github.com/search/repositories";
    private final static String GITHUB_SEARCH_QUERY_PARAM = "q";
    private final static String GITHUB_SEARCH_SORT_PARAM = "sort";
    private final static String GITHUB_SEARCH_SORT_VALUE = "stars";

    public static String buildGitHubSearchURL(String query) {
        return Uri.parse(GITHUB_SEARCH_BASE_URL).buildUpon()
                .appendQueryParameter(GITHUB_SEARCH_QUERY_PARAM, query)
                .appendQueryParameter(GITHUB_SEARCH_SORT_PARAM, GITHUB_SEARCH_SORT_VALUE)
                .build()
                .toString();
    }

    public static ArrayList<Apps> parseGitHubSearchResults(String json) {
        Gson gson = new Gson();
        GitHubSearchResults results = gson.fromJson(json, GitHubSearchResults.class);
        return results != null ? results.applist.apps : null;
    }
}
