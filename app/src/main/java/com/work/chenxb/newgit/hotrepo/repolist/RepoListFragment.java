package com.work.chenxb.newgit.hotrepo.repolist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mugen.Mugen;
import com.mugen.MugenCallbacks;
import com.work.chenxb.newgit.R;
import com.work.chenxb.newgit.commons.ActivityUtils;
import com.work.chenxb.newgit.hotrepo.Language;
import com.work.chenxb.newgit.hotrepo.repolist.model.FooterView;
import com.work.chenxb.newgit.hotrepo.repolist.model.Repo;
import com.work.chenxb.newgit.hotrepo.repolist.view.RepoListView;
import com.work.chenxb.newgit.repoinfo.RepoInfoActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

/**
 * 只是视图层  不会执行数据加载等业务逻辑的操作
 * 只是显示视图
 * 作者：ChenXb on 2016/7/28.22:59
 * 邮箱：810464826@qq.com
 */
public class RepoListFragment extends Fragment implements RepoListView {
    //fragment传递数据 最好不要在构造器里边传递
    //通过bundle的形式传递最好  （因为布局文件里边可能会引用fragment）
    private static final String KEY_LANGUAGE = "key_language";

    public static RepoListFragment getInstance(Language language) {
        RepoListFragment fragment = new RepoListFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_LANGUAGE, language);
        fragment.setArguments(args);
        return fragment;
    }

    private Language getLanguage() {
        return (Language) getArguments().getSerializable(KEY_LANGUAGE);
    }

    View view;
    @Bind(R.id.lvRepos)
    ListView listView;
    @Bind(R.id.ptrClassicFrameLayout)
    PtrClassicFrameLayout ptrClassicFrameLayout;
    @Bind(R.id.emptyView)
    TextView emptyView;
    @Bind(R.id.errorView)
    TextView errorView;
    private RepoListAdapter adapter;
    // 用来做当前页面业务逻辑及视图更新的
    private RepoListPresenter presenter;
    private FooterView footerView; // 上拉加载更多的视图
    private ActivityUtils activityUtils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_repo_list, container, false);
        ButterKnife.bind(this, view);
        activityUtils = new ActivityUtils(this);
        presenter = new RepoListPresenter(this, getLanguage());
        adapter = new RepoListAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Repo repo = adapter.getItem(i);
                RepoInfoActivity.open(getContext(), repo);
            }
        });
        //下拉刷新
        initPullToRefresh();
        //上拉加载
        initLoadMoreScroll();
        return view;
    }

    //上拉加载
    private void initLoadMoreScroll() {
        footerView = new FooterView(getContext());
        Mugen.with(listView, new MugenCallbacks() {
            // listView，滚动到底部,将触发此方法
            @Override
            public void onLoadMore() {
                // 执行上拉加载数据的业务处理
                presenter.loadMore();
            }

            // 是否正在加载中
            // 其内部将用此方法来判断是否触发onLoadMore
            @Override
            public boolean isLoading() {
                return listView.getFooterViewsCount() > 0 && footerView.isLoading();
            }

            // 是否已加载完成所有数据
            // 其内部将用此方法来判断是否触发onLoadMore
            @Override
            public boolean hasLoadedAllItems() {
                return listView.getFooterViewsCount() > 0 && footerView.isComplete();
            }
        }).start();
    }

    //下拉刷新
    private void initPullToRefresh() {
        // 使用当前对象做为key，来记录上一次的刷新时间,如果两次下拉太近，将不会触发新刷新
        ptrClassicFrameLayout.setLastUpdateTimeRelateObject(this);
        // 关闭header所用时长
        ptrClassicFrameLayout.setDurationToCloseHeader(1000);
        // 下拉刷新监听处理
        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            // 当你"下拉时",将触发此方法
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // 去做数据的加载，做具体的业务
                // 也就是说，你要抛开视图，到后台线程去做你的业务处理(数据刷新加载)
                presenter.refresh();
            }
        });
        // 以下代码（只是修改了header样式）
        StoreHouseHeader header = new StoreHouseHeader(getContext());
        header.initWithString("Running");
        header.setPadding(0, 60, 0, 60);
        // 修改Ptr的HeaderView效果
        ptrClassicFrameLayout.setHeaderView(header);
        ptrClassicFrameLayout.addPtrUIHandler(header);
        ptrClassicFrameLayout.setBackgroundResource(R.color.colorRefresh);
    }
    // 下拉刷新视图实现----------------------------------------
    @Override
    public void showContentView() {
        ptrClassicFrameLayout.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    @Override
    public void showErrorView(String errorMsg) {
        ptrClassicFrameLayout.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptyView() {
        ptrClassicFrameLayout.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void stopRefresh() {
        ptrClassicFrameLayout.refreshComplete();
    }

    @Override
    public void refreshData(List<Repo> datas) {
        adapter.clear();
        adapter.addAll(datas);
    }

    // 上拉加载更多视图实现----------------------------------------
    @Override
    public void showLoadMoreLoading() {
        if (listView.getFooterViewsCount() == 0) {
            listView.addFooterView(footerView);
        }
        footerView.showLoading();
    }

    @Override
    public void hideLoadMore() {
         listView.removeFooterView(footerView);
    }

    @Override
    public void showLoadMoreErro(String erroMsg) {
        if (listView.getFooterViewsCount() == 0) {
            listView.addFooterView(footerView);
        }
        footerView.showError(erroMsg);
    }

    @Override
    public void addMoreData(List<Repo> datas) {
        adapter.addAll(datas);
    }
}
