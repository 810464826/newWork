package com.work.chenxb.newgit;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.work.chenxb.newgit.commons.ActivityUtils;
import com.work.chenxb.newgit.hotrepo.HotRepoFragment;
import com.work.chenxb.newgit.login.LoginActivity;
import com.work.chenxb.newgit.login.UserRepo;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 这里不要忘记修改了！！！
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.navigationView)
    NavigationView navigationView;
    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    // 热门仓库Fragment
    private HotRepoFragment hotRepoFragment;
    private Button btnLogin;
    private ImageView ivIcon;
    private ActivityUtils activityUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        activityUtils=new ActivityUtils(this);
        // ActionBar处理
        setSupportActionBar(toolbar);
        // 设置navigationView的监听器
        navigationView.setNavigationItemSelectedListener(this);
        // 构建抽屉的监听
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // 根据drawerLayout同步其当前状态
        toggle.syncState();
        // 设置抽屉监听
        drawerLayout.addDrawerListener(toggle);

        btnLogin = ButterKnife.findById(navigationView.getHeaderView(0), R.id.btnLogin);
        ivIcon = ButterKnife.findById(navigationView.getHeaderView(0), R.id.ivIcon);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                activityUtils.startActivity(LoginActivity.class);
                finish();
            }
        });
        // 默认显示的是热门仓库fragment
        hotRepoFragment = new HotRepoFragment();
        replaceFragment(hotRepoFragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 没有授权的话
        if (UserRepo.isEmpty()) {
            btnLogin.setText(R.string.login_github);
            return;
        }
        btnLogin.setText(R.string.switch_account);
        // 设置Title
        getSupportActionBar().setTitle(UserRepo.getUser().getName());
        // 设置用户头像
        String photoUrl = UserRepo.getUser().getAvatar();
        ImageLoader.getInstance().displayImage(photoUrl, ivIcon);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 热门仓库
            case R.id.github_hot_repo:
                Toast.makeText(MainActivity.this, "热门仓库!", Toast.LENGTH_SHORT).show();
                break;
        }
        // 返回true，代表将该菜单项变为checked状态
        return true;
    }
}
