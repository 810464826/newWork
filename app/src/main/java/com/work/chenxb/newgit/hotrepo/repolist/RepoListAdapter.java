package com.work.chenxb.newgit.hotrepo.repolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.work.chenxb.newgit.R;
import com.work.chenxb.newgit.hotrepo.repolist.model.Repo;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者 ChenXb 2016/8/1 13:58
 * 邮箱 chenxb8089@163.com
 */
public class RepoListAdapter extends BaseAdapter {

    private final ArrayList<Repo> datas;

    public RepoListAdapter() {
        datas = new ArrayList<>();
    }

    public void addAll(Collection<Repo> repos) {
        datas.addAll(repos);
        notifyDataSetChanged();
    }

    public void clear() {
        datas.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Repo getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.layout_item_repo, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        Repo repo = getItem(position); // 当前item选的"数据"
        viewHolder.tvRepoName.setText(repo.getFullName());
        viewHolder.tvRepoInfo.setText(repo.getDescription());
        viewHolder.tvRepoStars.setText(repo.getStarCount() + "");
        ImageLoader.getInstance().displayImage(repo.getOwner().getAvatar(), viewHolder.ivIcon);
        return convertView;
       }
    }
    class ViewHolder {
        @Bind(R.id.ivIcon)
        ImageView ivIcon;
        @Bind(R.id.tvRepoName)
        TextView tvRepoName;
        @Bind(R.id.tvRepoInfo)
        TextView tvRepoInfo;
        @Bind(R.id.tvRepoStars)
        TextView tvRepoStars;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
}
