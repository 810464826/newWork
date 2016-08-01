package com.work.chenxb.newgit.login;

import com.work.chenxb.newgit.login.model.User;

/**
 * 此类是一个用来缓存当前用户信息的，极简单的实现
 *
 * 我们就是强用来保存
 * 作者：ChenXb on 2016/7/29.20:51
 * 邮箱：810464826@qq.com
 */
public class UserRepo {
    //采用单例模式
    private UserRepo(){  }
    private static String accessToken;
    private static User user;

    //判断当前是否有token
    public static boolean hasAccessToken(){
        return accessToken!=null;
    }

    //判断当前是否为空（还没有登录）
    public static boolean isEmpty(){
        return accessToken==null||user==null;
    }

    //清除信息
    public static void clear(){
        accessToken=null;
        user=null;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        UserRepo.accessToken = accessToken;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        UserRepo.user = user;
    }
}
