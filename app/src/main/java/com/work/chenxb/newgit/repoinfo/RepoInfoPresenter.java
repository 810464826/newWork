package com.work.chenxb.newgit.repoinfo;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.work.chenxb.newgit.hotrepo.repolist.model.Repo;
import com.work.chenxb.newgit.netWork.GitHubClient;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 此API不是JSON格式的，它以纯文本的形式text/plain接收Markdown文档，并返回该文档对应的原始文件。
 * 作者 ChenXb 2016/8/1 16:53
 * 邮箱 chenxb8089@163.com
 */
public class RepoInfoPresenter {

    //视图接口
    public interface RepoInfoView{
        void showProgress();
        void hideProgress();
        void showMessage(String msg);
        void setData(String htmlContent);
    }
    private RepoInfoView repoInfoView;
    private Call<RepoContentResult> repoContentResultCall;
    private Call<ResponseBody> mhtmlCall;
    public RepoInfoPresenter(@NonNull RepoInfoView repoInfoView){
        this.repoInfoView=repoInfoView;
    }
    public void getReadme(Repo repo){
        repoInfoView.showProgress();
        String login=repo.getOwner().getLogin();
        String name=repo.getName();
        if (repoContentResultCall!=null){
            repoContentResultCall.cancel();
        }
        repoContentResultCall= GitHubClient.getInstance().getReadme(login,name);
        repoContentResultCall.enqueue(repoContentCallback);
    }


    private Callback<RepoContentResult> repoContentCallback=new Callback<RepoContentResult>() {
        @Override
        public void onResponse(Call<RepoContentResult> call, Response<RepoContentResult> response) {
            String content=response.body().getContent();
            //base64解码
            byte[] data= Base64.decode(content,Base64.DEFAULT);
            //根据data获取到markdown（也就是readme文件）的html的格式文件
            MediaType mediaType=MediaType.parse("text/plain");
            RequestBody body=RequestBody .create(mediaType,data);
            if (mhtmlCall!=null){
                mhtmlCall.cancel();
            }
            mhtmlCall=GitHubClient.getInstance().markDown(body);
            mhtmlCall.enqueue(mhtmlCallback);
        }
        @Override
        public void onFailure(Call<RepoContentResult> call, Throwable t) {
            repoInfoView.hideProgress();
            repoInfoView.showMessage(t.getMessage());
        }
    };

    private Callback<ResponseBody> mhtmlCallback=new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            repoInfoView.hideProgress();
            try {
                String htmlContent=response.body().string();
                repoInfoView.setData(htmlContent);
            } catch (IOException e) {
               onFailure(call,e);
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
                repoInfoView.hideProgress();
                repoInfoView.showMessage(t.getMessage());
        }
    };
}
