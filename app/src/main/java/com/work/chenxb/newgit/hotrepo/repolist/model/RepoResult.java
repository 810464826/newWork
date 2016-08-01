package com.work.chenxb.newgit.hotrepo.repolist.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 搜索仓库的返回结果
 * 作者 ChenXb 2016/8/1 13:00
 * 邮箱 chenxb8089@163.com
 */
public class RepoResult {
    @SerializedName("total_count")
    private int totalCount;

    @SerializedName("incomplete_results")
    private boolean incompleteResults;

    @SerializedName("items")
    private List<Repo> repoList;

    public int getTotalCount() {
        return totalCount;
    }

    public boolean isIncompleteResults() {
        return incompleteResults;
    }

    public List<Repo> getRepoList() {
        return repoList;
    }

//       {
//            "total_count": 2074901,
//            "incomplete_results": false,
//            "items": [{}]
//    }
}
