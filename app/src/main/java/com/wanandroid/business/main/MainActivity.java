package com.wanandroid.business.main;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wanandroid.R;
import com.wanandroid.business.articles.ArticlesFragment;
import com.wanandroid.business.cid.CidFragment;
import com.wanandroid.business.classify.ClassifyDialog;
import com.wanandroid.business.callback.OnClassifyClickListener;
import com.wanandroid.business.callback.OnSearchKeyClickListener;
import com.wanandroid.business.search.SearchFragment;
import com.wanandroid.model.entity.Cid;
import com.wanandroid.utils.ActivityUtils;
import com.wanandroid.utils.ImeUtils;

/**
 * 应用主界面
 * <p>
 * 主要负责Fragment的切换以及事件的传递
 * </p>
 */
public class MainActivity extends AppCompatActivity implements OnSearchKeyClickListener {

    private static final String TAG = "MainActivity";

    private Toolbar mToolbar;

    private EditText mSearchEdit;

    //显示首页数据的Fragment
    private ArticlesFragment mArticleFragment;

    //显示搜索结果的Fragment
    private SearchFragment mSearchFragment;

    //显示"知识体系"详情的Fragment
    private CidFragment mCidFragment;

    //显示"知识体系"分类的Dialog
    private ClassifyDialog mClassifyDialog;

    //是否进入了搜索状态，搜索状态需要隐藏菜单等
    private boolean mInSearchMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView(savedInstanceState);
    }


    private void initView(Bundle savedInstanceState) {
        //设置顶部标题栏
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        //为EditText设置搜索监听
        mSearchEdit = (EditText) findViewById(R.id.main_edit_search);
        mSearchEdit.setOnEditorActionListener(getEditorAction());

        if (mArticleFragment == null) {
            mArticleFragment = new ArticlesFragment();
        }
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mArticleFragment, R.id.main_fragment_container, false);

//        //初始化Fragment，将显示首页的Fragment添加到Activity
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        if (savedInstanceState != null) {
//            mArticleFragment = (ArticlesFragment) fragmentManager.findFragmentByTag(ArticlesFragment.class.getName());
//            fragmentManager.beginTransaction()
//                    .show(mArticleFragment)
//                    .commit();
//        } else {
//            mArticleFragment = new ArticlesFragment();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.add(R.id.main_fragment_container, mArticleFragment, ArticlesFragment.class.getName());
//            transaction.commit();
//        }
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
                changeSearchMode(true);
                break;
            case android.R.id.home:
                changeSearchMode(false);
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
            mClassifyDialog = new ClassifyDialog(this, mToolbar);
            mClassifyDialog.setOnClassifyClickListener(new OnClassifyClickListener() {
                @Override
                public void onClassifyClickListener(final Cid cid) {
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
            });
        }
        mClassifyDialog.show();
    }

    /**
     * 当用户点搜索按钮的时候切换到搜索视图
     */
    private void changeSearchMode(boolean isSearch) {
        mInSearchMode = isSearch;
        mSearchEdit.setVisibility(isSearch ? View.VISIBLE : View.GONE);
        mSearchEdit.setText("");
        //刷新菜单
        invalidateOptionsMenu();
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
                    mSearchEdit.requestFocus();
                    ImeUtils.showIme(mSearchEdit);
                }
            }, 200);
        } else {
            if (mSearchFragment != null && mSearchFragment.isAdded()) {
                fragmentManager.popBackStack();
                mSearchFragment = null;
            }
        }
    }

    @Override
    public void onBackPressed() {
        boolean consumed = false;
        if (mArticleFragment != null) {
            consumed = mArticleFragment.onBackPressed();
        }
        if (!consumed) {
            //自己处理返回事件
            if (mInSearchMode) {
                changeSearchMode(false);
                return;
            }
            super.onBackPressed();
        }
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
                    if (search(v.getText().toString())) return true;
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
        mSearchEdit.setText(key.trim());
        mSearchEdit.setSelection(key.trim().length());
        ImeUtils.hideIme(mSearchEdit);
        search(key);
    }
}