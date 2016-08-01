package com.work.chenxb.newgit.login;

/**
 * 作者：ChenXb on 2016/7/29.20:51
 * 邮箱：810464826@qq.com
 */
public interface LoginView {
    // 显示进度
    void showProgress();

    void showMessage(String msg);

    // 重置WebView
    void resetWeb();

    // 导航切换至Main页面
    void navigateToMain();
}
