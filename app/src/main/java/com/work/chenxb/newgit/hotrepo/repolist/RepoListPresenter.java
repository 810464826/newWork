package com.work.chenxb.newgit.hotrepo.repolist;

import com.work.chenxb.newgit.hotrepo.Language;
import com.work.chenxb.newgit.hotrepo.repolist.model.Repo;
import com.work.chenxb.newgit.hotrepo.repolist.model.RepoResult;
import com.work.chenxb.newgit.hotrepo.repolist.view.RepoListView;
import com.work.chenxb.newgit.netWork.GitHubClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 执行相关的业务逻辑
 * 作者：ChenXb on 2016/7/28.23:04
 * 邮箱：810464826@qq.com
 */
public class RepoListPresenter {
    //视图的接口
    private RepoListView repoListView;
    private int nextPage = 0;
    private Language language;
    private Call<RepoResult> repoResultCall;

    public RepoListPresenter(RepoListView repoListView, Language language) {
        this.repoListView = repoListView;
        this.language = language;
    }

    //下拉刷新***********************************************************
    public void refresh() {
        //先隐藏加载更多
        repoListView.hideLoadMore();
        repoListView.showContentView();
        //永远刷新最新的数据
        nextPage = 1;
        repoResultCall = GitHubClient.getInstance().searchRepos(
                "language:" + language.getPath(), nextPage
        );
        repoResultCall.enqueue(repoCallback);
    }

    // 上拉加载更多处理****************************************************
    public void loadMore() {
        repoListView.showLoadMoreLoading();
        repoResultCall = GitHubClient.getInstance().searchRepos(
                "language:" + language.getPath()
                , nextPage);
        repoResultCall.enqueue(loadMoreCallback);
    }

    //刷新------------------------------------------------------
    private final Callback<RepoResult> repoCallback = new Callback<RepoResult>() {
        @Override public void onResponse(Call<RepoResult> call, Response<RepoResult> response) {
            // 视图停止刷新
            repoListView.stopRefresh();
            // 得到响应结果
            RepoResult repoResult = response.body();
            if (repoResult == null) {
                repoListView.showErrorView("结果为空!");
                return;
            }
            // 当前搜索的语言,没有仓库
            if (repoResult.getTotalCount() <= 0) {
                repoListView.refreshData(null);
                repoListView.showEmptyView();
                return;
            }
            // 取出当前语言下的所有仓库
            List<Repo> repoList = repoResult.getRepoList();
            repoListView.refreshData(repoList);
            // 下拉刷新成功(1), 下一面则更新为2
            nextPage = 2;
        }

        @Override public void onFailure(Call<RepoResult> call, Throwable t) {
            // 视图停止刷新
            repoListView.stopRefresh();
            repoListView.showMessage("repoCallback onFailure" + t.getMessage());
        }
    };

    //加载更多---------------------------------------------------
    private final Callback<RepoResult> loadMoreCallback = new Callback<RepoResult>(){

        @Override public void onResponse(Call<RepoResult> call, Response<RepoResult> response) {
            repoListView.hideLoadMore();
            // 得到响应结果
            RepoResult repoResult = response.body();
            if (repoResult == null) {
                repoListView.showLoadMoreErro("结果为空!");
                return;
            }
            // 取出当前语言下的所有仓库
            List<Repo> repoList = repoResult.getRepoList();
            repoListView.addMoreData(repoList);
            nextPage++;
        }

        @Override public void onFailure(Call<RepoResult> call, Throwable t) {
            // 视图停止刷新
            repoListView.hideLoadMore();
            repoListView.showMessage("repoCallback onFailure" + t.getMessage());
        }
    };

}
