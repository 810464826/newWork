package com.work.chenxb.newgit.repoinfo;

/**
 * 获取readme的响应结果
 * 作者 ChenXb 2016/8/1 16:50
 * 邮箱 chenxb8089@163.com
 */
public class RepoContentResult {
    private String content;
    private String encoding;

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    //    {
    //        "encoding": "base64",
    //            "content": "encoded content ..."
    //    }
}
