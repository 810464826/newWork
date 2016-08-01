package com.work.chenxb.newgit.login.model;

import com.google.gson.annotations.SerializedName;

/**
 * 作者：ChenXb on 2016/7/29.20:39
 * 邮箱：810464826@qq.com
 */
public class AccessTokenResult{
    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("scope")
    private String scope;

    @SerializedName("token_type")
    private String tokenType;

    public String getAccessToken() {
        return accessToken;
    }

    public String getScope() {
        return scope;
    }

    public String getTokenType() {
        return tokenType;
    }

    //    Accept: application/json
//    {
//        "access_token":"e72e16c7e42f292c6912e7710c838347ae178b4a", "scope":"repo,gist",
//            "token_type":"bearer"
//    }
}
