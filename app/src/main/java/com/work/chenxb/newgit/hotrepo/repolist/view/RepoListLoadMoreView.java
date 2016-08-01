package com.work.chenxb.newgit.hotrepo.repolist.view;

import com.work.chenxb.newgit.hotrepo.repolist.model.Repo;

import java.util.List;

/**
 * 提供回调接口 外部实现该接口 实现具体的方法
 * 作者：ChenXb on 2016/7/28.23:05
 * 邮箱：810464826@qq.com
 */
public interface RepoListLoadMoreView {
    void showLoadMoreLoading();
    void showLoadMoreErro(String erroMsg);
    void hideLoadMore();
    void addMoreData(List<Repo> datas);
}
