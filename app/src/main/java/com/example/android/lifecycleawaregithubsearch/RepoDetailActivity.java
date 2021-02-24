package com.example.android.lifecycleawaregithubsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.lifecycleawaregithubsearch.data.GitHubRepo;

public class RepoDetailActivity extends AppCompatActivity {
    public static final String EXTRA_GITHUB_REPO = "GitHubRepo";

    private static final String TAG = RepoDetailActivity.class.getSimpleName();

    private Toast errorToast;

    private GitHubRepo repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_detail);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_GITHUB_REPO)) {
            this.repo = (GitHubRepo)intent.getSerializableExtra(EXTRA_GITHUB_REPO);
            Log.d(TAG, "Got repo with name: " + repo.fullName);

            TextView repoNameTV = findViewById(R.id.tv_repo_name);
            TextView repoStarsTV = findViewById(R.id.tv_repo_stars);
            TextView repoDescriptionTV = findViewById(R.id.tv_repo_description);

            repoNameTV.setText(repo.fullName);
            repoStarsTV.setText(String.valueOf(repo.stars));
            repoDescriptionTV.setText(repo.description);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.repo_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_on_web:
                viewRepoOnWeb();
                return true;
            case R.id.action_share:
                shareRepo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void viewRepoOnWeb() {
        if (this.repo != null) {
            Uri githubRepoUri = Uri.parse(this.repo.htmlUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW, githubRepoUri);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                if (this.errorToast != null) {
                    this.errorToast.cancel();
                }
                this.errorToast = Toast.makeText(this, "Error...", Toast.LENGTH_LONG);
                this.errorToast.show();
            }
        }
    }

    private void shareRepo() {
        if (this.repo != null) {
            String shareText = getString(
                    R.string.share_repo_text,
                    this.repo.fullName,
                    this.repo.htmlUrl
            );
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, shareText);
            intent.setType("text/plain");

            Intent chooserIntent = Intent.createChooser(intent, null);
            startActivity(chooserIntent);
        }
    }
}