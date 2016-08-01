package com.work.chenxb.newgit.login;

import android.util.Log;

import com.work.chenxb.newgit.login.model.AccessTokenResult;
import com.work.chenxb.newgit.login.model.User;
import com.work.chenxb.newgit.netWork.GitHubApi;
import com.work.chenxb.newgit.netWork.GitHubClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者：ChenXb on 2016/7/29.20:56
 * 邮箱：810464826@qq.com
 */
public class LoginPresenter {
    private Call<AccessTokenResult> tokenCall;
    private Call<User> userCall;
    private LoginView loginView;

    public LoginPresenter(LoginView loginView){
        this.loginView = loginView;
    }

    // 登陆(先用code换token,再用token换用户信息)
    public void login(String code) {
        loginView.showProgress();
        if (tokenCall != null) tokenCall.cancel();
        // 获取token
        tokenCall = GitHubClient.getInstance().getOAuthToken(
                GitHubApi.CLIENT_ID,
                GitHubApi.CLIENT_SECRET,
                code);
        tokenCall.enqueue(tokenCallback);
    }

    private Callback<AccessTokenResult> tokenCallback = new Callback<AccessTokenResult>() {
        // token接口响应
        @Override public void onResponse(Call<AccessTokenResult> call, Response<AccessTokenResult> response) {
            // 响应体内数据(结果)
            AccessTokenResult result = response.body();
            String token = result.getAccessToken();
            // 缓存token
            UserRepo.setAccessToken(token);
            // 再将用此token执行获取用户信息接口,拿到用户信息
            if (userCall != null) userCall.cancel();
            userCall = GitHubClient.getInstance().getUserInfo();
            userCall.enqueue(userCallback);
        }

        @Override public void onFailure(Call<AccessTokenResult> call, Throwable t) {
            loginView.showMessage(t.getMessage());
            loginView.showProgress();
            loginView.resetWeb();
        }
    };

    private Callback<User> userCallback = new Callback<User>() {
        @Override public void onResponse(Call<User> call, Response<User> response) {
            User user = response.body();
            // 缓存user
            UserRepo.setUser(user);
            loginView.showMessage("登陆成功");
            loginView.navigateToMain();
        }

        @Override public void onFailure(Call<User> call, Throwable t) {
            loginView.showMessage(t.getMessage());
            loginView.showProgress();
            loginView.resetWeb();
        }
    };
}
