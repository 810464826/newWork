package com.work.chenxb.newgit.repoinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.work.chenxb.newgit.R;
import com.work.chenxb.newgit.commons.ActivityUtils;
import com.work.chenxb.newgit.hotrepo.repolist.model.Repo;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RepoInfoActivity extends AppCompatActivity implements RepoInfoPresenter.RepoInfoView {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.ivIcon)
    ImageView ivIcon;
    @Bind(R.id.tvRepoName)
    TextView tvRepoName;
    @Bind(R.id.tvRepoStars)
    TextView tvRepoStars;
    @Bind(R.id.layoutIntroduction)
    RelativeLayout layoutIntroduction;
    @Bind(R.id.tvRepoInfo)
    TextView tvRepoInfo;
    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    private ActivityUtils activityUtils;
    private RepoInfoPresenter presenter;
    private Repo repo;
    private static final String KEY_REPO = "key_repo";

    //带参数跳转
    public static void open(Context context, @NonNull Repo repo) {
        Intent intent = new Intent(context, RepoInfoActivity.class);
        intent.putExtra(KEY_REPO, repo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        setContentView(R.layout.activity_repo_info);
        presenter = new RepoInfoPresenter(this);
        presenter.getReadme(repo);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        // 获取到Repo
        repo = (Repo) getIntent().getSerializableExtra(KEY_REPO);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 使用仓库名称做为title
        getSupportActionBar().setTitle(repo.getName());
        // 设置仓库信息
        tvRepoName.setText(repo.getFullName());
        tvRepoInfo.setText(repo.getDescription());
        tvRepoStars.setText(String.format("start: %d  fork: %d", repo.getStarCount(), repo.getForkCount()));
        ImageLoader.getInstance().displayImage(repo.getOwner().getAvatar(), ivIcon);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void setData(String htmlContent) {
        webView.loadData(htmlContent, "text/html", "UTF-8");
    }
}
