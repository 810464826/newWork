package com.work.chenxb.newgit.hotrepo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.work.chenxb.newgit.hotrepo.repolist.RepoListFragment;

import java.util.List;

/**
 * 作者：ChenXb on 2016/7/28.22:56
 * 邮箱：810464826@qq.com
 */
public class HotRepoAdapter extends FragmentPagerAdapter {
    private List<Language> languages;
    public HotRepoAdapter(FragmentManager fm, Context context) {
        super(fm);
        languages=Language.getDefaultLanguages(context);
    }

    @Override public Fragment getItem(int position) {
        return RepoListFragment.getInstance(languages.get(position));
    }

    @Override public int getCount() {
        return languages.size();
    }

    @Override public CharSequence getPageTitle(int position) {
        return languages.get(position).getName();
    }
}
