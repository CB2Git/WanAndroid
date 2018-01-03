package com.wanandroid.business.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.wanandroid.R;
import com.wanandroid.business.about.AboutMeActivity;
import com.wanandroid.business.articles.ArticlesFragment;
import com.wanandroid.business.callback.OnClassifyClickListener;
import com.wanandroid.business.callback.OnSearchKeyClickListener;
import com.wanandroid.business.callback.OnSetToolbarTitleCallBack;
import com.wanandroid.business.cid.CidFragment;
import com.wanandroid.business.classify.ClassifyDialog;
import com.wanandroid.business.collects.CollectFragment;
import com.wanandroid.business.login.LoginOrResisterActivity;
import com.wanandroid.business.search.SearchFragment;
import com.wanandroid.model.db.UserManger;
import com.wanandroid.model.entity.Tree;
import com.wanandroid.model.utils.WanAndroidCookieJar;
import com.wanandroid.utils.ActivityUtils;
import com.wanandroid.utils.ImeUtils;
import com.wanandroid.utils.PackageUtils;
import com.wanandroid.widget.AutoClearEditText;

/**
 * 应用主界面
 * <p>
 * 主要负责Fragment的切换以及事件的传递
 * </p>
 */
public class MainActivity extends AppCompatActivity implements OnSearchKeyClickListener, OnSetToolbarTitleCallBack {

    private static final String TAG = "MainActivity";

    //管理全部控件的Holder
    private MainActivityHolder mHolder;

    //显示首页数据的Fragment
    private ArticlesFragment mArticleFragment;

    //显示搜索结果的Fragment
    private SearchFragment mSearchFragment;

    //显示"知识体系"详情的Fragment
    private CidFragment mCidFragment;

    //显示"知识体系"分类的Dialog
    private ClassifyDialog mClassifyDialog;

    //显示收藏列表的Fragment
    private CollectFragment mCollectFragment;

    //是否进入了搜索状态，搜索状态需要隐藏菜单等
    private boolean mInSearchMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView(savedInstanceState);
        //5.0及以上使用透明状态栏，这样侧滑菜单滑出来好看点(ps:4.4无法兼容啊，只能放弃5.0以下的体验了)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //每一次进入界面都刷新用户信息，因为可能开始没有登录，后来登录了
        mHolder.initUserInfo(UserManger.getUserInfo());
    }

    @Override
    protected void onStop() {
        super.onStop();
        //自动关闭侧滑菜单哦~~~
        mHolder.closeDrawer();
    }

    private void initView(Bundle savedInstanceState) {
        //初始化全部控件
        mHolder = new MainActivityHolder(this);
        //初始化登录用户信息
        mHolder.initUserInfo(UserManger.getUserInfo());
        //为EditText设置搜索监听
        mHolder.getSearchEdit().setOnEditorActionListener(getEditorAction());
        mHolder.getSearchEdit().setOnClearTextListener(getOnClearEditTextListenrt());
        if (mArticleFragment == null) {
            mArticleFragment = new ArticlesFragment();
        }
        //设置滑出菜单选中"主页"
        mHolder.getNavigationView().setCheckedItem(R.id.action_home);
        mHolder.getNavigationView().setNavigationItemSelectedListener(getNavigationItemSelectedListener());
        //设置点击用户头像响应
        mHolder.getUserImage().setOnClickListener(getOnClickUserImageListener());
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mArticleFragment, R.id.main_fragment_container, false);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //如果是搜索状态，则将菜单全部隐藏
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setVisible(!mInSearchMode);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                switchSearchMode(true);
                break;
            case android.R.id.home:
                switchSearchMode(false);
                break;
            case R.id.action_show_classify:
                showClassifyDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 显示分类对话框
     */
    private void showClassifyDialog() {
        if (mClassifyDialog == null) {
            mClassifyDialog = new ClassifyDialog(this, mHolder.getToolbar());
            mClassifyDialog.setOnClassifyClickListener(getOnCidClickListener());
        }
        mClassifyDialog.show();
    }

    /**
     * 当用户点搜索按钮的时候切换到搜索视图
     */
    private void switchSearchMode(boolean isSearch) {
        mInSearchMode = isSearch;
        mHolder.getSearchEdit().setVisibility(isSearch ? View.VISIBLE : View.GONE);
        mHolder.getSearchEdit().setText("");
        //刷新菜单
        invalidateOptionsMenu();
        //搜索视图不显示侧滑菜单
        if (isSearch) {
            mHolder.getToolbar().setNavigationIcon(null);
        } else {
            mHolder.getToolbar().setNavigationIcon(R.mipmap.ic_menu);
        }
        mHolder.getDrawerLayout().setEnabled(!isSearch);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (isSearch) {
            if (mSearchFragment == null) {
                mSearchFragment = new SearchFragment();
            }
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mSearchFragment, R.id.main_fragment_container);
            //这里加点延迟才能打开键盘
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mHolder.getSearchEdit().requestFocus();
                    ImeUtils.showIme(mHolder.getSearchEdit());
                }
            }, 200);
        } else {
            if (mSearchFragment != null && mSearchFragment.isAdded()) {
                fragmentManager.popBackStack();
                mSearchFragment = null;
            }
        }
    }

    /**
     * 侧滑菜单菜单项被点击的处理
     */
    private NavigationView.OnNavigationItemSelectedListener getNavigationItemSelectedListener() {
        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                //显示首页
                if (itemId == R.id.action_home) {
                    doShowHomePage();
                    return true;
                }
                //展示收藏列表
                if (itemId == R.id.action_collect_list) {
                    return doShowCollects();
                }
                //退出登录
                if (itemId == R.id.action_quit_login) {
                    doQuitLogin();
                }
                //反馈
                if (itemId == R.id.action_feedback) {
                    doFeedBack();
                }
                //关于我
                if (itemId == R.id.action_about_me) {
                    doAboutMe();
                }
                return false;
            }
        };
    }

    /**
     * 当搜索框被清空的回调
     */
    private AutoClearEditText.OnClearTextListener getOnClearEditTextListenrt() {
        return new AutoClearEditText.OnClearTextListener() {
            @Override
            public void onClearText() {
                if (mSearchFragment != null && mInSearchMode) {
                    //显示"热搜"
                    mSearchFragment.toggleViewStatue(0);
                }
            }
        };
    }

    //反馈
    private void doFeedBack() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.prompt);
        if (!PackageUtils.checkApkExist(this, "com.tencent.mobileqq")) {
            builder.setMessage(R.string.qq_is_no_exist);
            builder.setPositiveButton(R.string.confirm, null);
        } else {
            builder.setMessage(R.string.temp_chat_tip);
            builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=1479076807&version=1")));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, R.string.open_qq_error, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        builder.show();
    }

    /**
     * 显示关于我
     */
    private void doAboutMe() {
        Intent intent = new Intent(this, AboutMeActivity.class);
        startActivity(intent);
    }

    /**
     * 显示收藏列表
     */
    private boolean doShowCollects() {

        //如果没有登录，则点击跳转到登录界面
        if (UserManger.getUserInfo() == null) {
            Intent intent = LoginOrResisterActivity.newInstance(this, true);
            startActivity(intent);
            return false;
        }

        int count = getSupportFragmentManager().getBackStackEntryCount();
        Log.i(TAG, "doShowHomePage: fragment count = " + count);
        mHolder.closeDrawer();
        if (mCollectFragment == null) {
            mCollectFragment = new CollectFragment();
            for (int i = 0; i < count; i++) {
                getSupportFragmentManager().popBackStack();
            }
        }
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mCollectFragment, R.id.main_fragment_container, false);
        return true;
    }

    /**
     * 显示主页
     */
    private void doShowHomePage() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        Log.i(TAG, "doShowHomePage: fragment count = " + count);
        //清空回退栈
        for (int i = 0; i < count; i++) {
            getSupportFragmentManager().popBackStack();
        }
        //移除掉收藏列表，如果存在的话
        if (mCollectFragment != null && mCollectFragment.isAdded()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(mCollectFragment);
            transaction.commit();
            mCollectFragment = null;
        }
        mHolder.closeDrawer();
    }

    /**
     * 退出登录
     */
    private void doQuitLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.prompt);
        builder.setMessage(R.string.are_you_sure_quit_login);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //清理数据库信息
                UserManger.clearUserInfo();
                //清理cookie信息
                WanAndroidCookieJar.clearCookie();
                //清理界面显示
                mHolder.clearUserInfo();
                Snackbar.make(mHolder.getToolbar(), R.string.unlogin_tip, Snackbar.LENGTH_SHORT).show();
                //返回主页
                doShowHomePage();
                mHolder.getNavigationView().setCheckedItem(R.id.action_home);
            }
        });
        builder.show();
        mHolder.closeDrawer();
    }

    /**
     * 用户切换了"知识体系"的回调
     */
    @NonNull
    private OnClassifyClickListener getOnCidClickListener() {
        return new OnClassifyClickListener() {
            @Override
            public void onClassifyClickListener(final Tree cid) {
                if (mCidFragment == null) {
                    mCidFragment = new CidFragment();
                }
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mCidFragment, R.id.main_fragment_container);
                //加一个延迟，不然可能fragment还没初始化ok
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mCidFragment.onClassifyClickListener(cid);
                    }
                }, 200);
            }
        };
    }

    private long last_click_back_time = 0;

    @Override
    public void onBackPressed() {
        boolean consumed = false;
        if (mArticleFragment != null && mInSearchMode) {
            consumed = mArticleFragment.onBackPressed();
        }
        if (mSearchFragment != null && mInSearchMode) {
            consumed = mSearchFragment.onBackPressed();
        }
        if (!consumed) {
            //自己处理返回事件
            //如果在搜索模式，退出搜索
            if (mInSearchMode) {
                switchSearchMode(false);
                return;
            }
            //如果侧滑菜单开启，关闭
            if (mHolder.isDrawerOpen()) {
                mHolder.closeDrawer();
                return;
            }
            //双击应用才退出应用
            if (System.currentTimeMillis() - last_click_back_time > 2000) {
                Toast.makeText(this, R.string.double_click_back_exit, Toast.LENGTH_SHORT).show();
                last_click_back_time = System.currentTimeMillis();
                return;
            }
            super.onBackPressed();
        }
    }

    private View.OnClickListener getOnClickUserImageListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserManger.getUserInfo() == null) {
                    Intent intent = LoginOrResisterActivity.newInstance(MainActivity.this, true);
                    startActivity(intent);
                } else {
                    //TODO 跳转到个人中心
                    Toast.makeText(MainActivity.this, "未完待续哦~~", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    /**
     * 用户点击了搜索按钮的响应
     */
    public TextView.OnEditorActionListener getEditorAction() {
        //返回True可以让键盘不收起
        return new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (search(v.getText().toString())) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    private boolean search(String key) {
        if (mSearchFragment != null) {
            if (TextUtils.isEmpty(key.trim())) {
                Toast.makeText(MainActivity.this, R.string.search_key_error, Toast.LENGTH_SHORT).show();
                return true;
            }
            mSearchFragment.doSearch(key);
        }
        return false;
    }

    /**
     * Fragment回调过来需进行搜索，Activity进行UI上面的一些修改然后再调用回去搜索
     *
     * @param key 关键词
     */
    @Override
    public void OnSearchKeyClick(String key) {
        mHolder.getSearchEdit().setText(key.trim());
        mHolder.getSearchEdit().setSelection(key.trim().length());
        ImeUtils.hideIme(mHolder.getSearchEdit());
        search(key);
    }

    /**
     * Fragment需要改变顶部标题文字
     */
    @Override
    public void setToolbarTitle(String title) {
        mHolder.getToolbar().setTitle(title);
    }
}