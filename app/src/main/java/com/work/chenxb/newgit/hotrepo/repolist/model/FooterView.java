package com.work.chenxb.newgit.hotrepo.repolist.model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.work.chenxb.newgit.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 本视图用来实现上拉功能视图，
 * <p/>
 * 当ListView滚动到尾部时，我们将在会过listView的addFooterView方法将此视图添加上去，
 * <p/>
 * 并且将在后台线程去加更多数据
 * <p/>
 * 此视图将考虑三种不同状态：
 * 展示loading (进度条)
 * 显示错误（当点击时，重新load）
 * 显示没有更多数据
 * <p/>
 * 作者：yuanchao on 2016/7/28 0028 11:01
 * 邮箱：yuanchao@feicuiedu.com
 */
public class FooterView extends FrameLayout {
    private static final int STATE_LOADING = 0;
    private static final int STATE_COMPLETE = 1;
    private static final int STATE_ERROR = 2;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.tv_complete)
    TextView tvComplete;
    @Bind(R.id.tv_error)
    TextView tvError;

    private int state = STATE_LOADING;


    public FooterView(Context context) {
        this(context, null);
    }

    public FooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.content_load_footer, this, true);
        ButterKnife.bind(this);
    }

    //** 是否正在加载中*/
    public boolean isLoading(){
        return state == STATE_LOADING;
    }
    /** 是否加载完成(没有更多数据)*/
    public boolean isComplete(){
        return state == STATE_COMPLETE;
    }

    /** 显示加载中*/
    public void showLoading(){
        state = STATE_LOADING;
        progressBar.setVisibility(View.VISIBLE);
        tvComplete.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
    }

    /** 显示加载完成(没有更多数据时)*/
    public void showComplete(){
        state = STATE_COMPLETE;
        tvComplete.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
    }

    /** 显示没有更多数据*/
    public void showError(String erroMsg){
        state = STATE_ERROR;
        tvError.setVisibility(View.VISIBLE);
        tvComplete.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    public void setErrorClickListener(OnClickListener onClickListener){
        tvError.setOnClickListener(onClickListener);
    }
}
