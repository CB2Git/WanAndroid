package com.wanandroid.business.articledetail;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.wanandroid.R;
import com.wanandroid.business.base.BaseMVPActivity;
import com.wanandroid.business.login.LoginOrResisterActivity;
import com.wanandroid.model.entity.Article;
import com.wanandroid.model.entity.WanAndroidUser;
import com.wanandroid.utils.ActivityUtils;
import com.wanandroid.utils.SharesUtils;
import com.wanandroid.widget.WebViewFragment;

/**
 * 一个显示WebView的Activity
 * <p>
 * TODO：点击网页图片 白屏问题！！！
 */
public class ArticleDetailActivity extends BaseMVPActivity<ArticleDetailContract.View, ArticleDetailPresenter> implements ArticleDetailContract.View {

    private static final String TAG = "ArticleDetailActivity";

    private Toolbar mWebToolbar;

    private WebView mWebView;

    private WebViewFragment mWebViewFragment;

    private ProgressBar mWebProgress;

    //当前显示的文章信息
    private Article mCurrArticle;

    private static final String ARTICLE_KEY = "article_key";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mCurrArticle = (Article) getIntent().getExtras().getSerializable(ARTICLE_KEY);
        initView();
    }

    @Override
    public ArticleDetailPresenter bindPresenter() {
        return new ArticleDetailPresenter();
    }

    @Override
    public ArticleDetailContract.View bindView() {
        return this;
    }

    /**
     * WebView是否已经初始化过了
     */
    boolean isWebViewInit = false;

    @Override
    protected void onStart() {
        super.onStart();
        if (!isWebViewInit) {
            mWebView = mWebViewFragment.getWebView();
            initWebView();
            mWebView.loadUrl(mCurrArticle.getLink());
            isWebViewInit = true;
        }
    }

    /**
     * 设置WebView的一些属性
     */
    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setLoadsImagesAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);

        mWebView.setWebViewClient(new CusWebViewClient());
        mWebView.setWebChromeClient(new CusWebChromeClient());
    }

    public static Intent newInstance(Context context, Article article) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARTICLE_KEY, article);
        intent.putExtras(bundle);
        return intent;
    }

    private void initView() {
        mWebToolbar = findViewById(R.id.web_title);
        setSupportActionBar(mWebToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mWebProgress = findViewById(R.id.web_progress);

        mWebViewFragment = new WebViewFragment();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mWebViewFragment, R.id.base_web_container);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.getItemId() == R.id.action_collect) {
                item.setIcon(mCurrArticle.isCollect() ? R.mipmap.ic_collect : R.mipmap.ic_uncollect);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_refresh:
                mWebView.reload();
                break;
            case R.id.action_default_browser:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mWebView.getUrl()));
                startActivity(intent);
                break;
            case R.id.action_share:
                SharesUtils.share(this, mWebView.getUrl());
                break;
            case R.id.action_copy_link:
                ClipboardManager cmd = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmd.setPrimaryClip(ClipData.newPlainText(getString(R.string.copy_link), mWebView.getUrl()));
                Snackbar.make(getWindow().getDecorView(), R.string.copy_link_success, Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.action_collect:
                doCollectAction();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 执行收藏/取消收藏动作
     */
    private void doCollectAction() {
        WanAndroidUser userInfo = getBindPresenter().getUserInfo();
        if (userInfo == null) {
            Log.i(TAG, "doCollectAction: UserInfo is null,try autoLogin or register");
            Intent intent = LoginOrResisterActivity.newInstance(this, true);
            startActivity(intent);
            return;
        }
        //如果已经登录过了
        //进行收藏/取消收藏动作
        if (mCurrArticle.isCollect()) {
            getBindPresenter().unCollectArticle(mCurrArticle.getId());
        } else {
            getBindPresenter().collectArticle(mCurrArticle.getId());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public void collectSuccess() {
        mCurrArticle.setCollect(true);
        Snackbar.make(mWebToolbar, R.string.collect_successed, Snackbar.LENGTH_SHORT).show();
        invalidateOptionsMenu();
    }

    @Override
    public void collectFail() {
        mCurrArticle.setCollect(false);
        invalidateOptionsMenu();
        Snackbar.make(mWebToolbar, R.string.collect_fail, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void unCollectSuccess() {
        mCurrArticle.setCollect(false);
        Snackbar.make(mWebToolbar, R.string.collect_success, Snackbar.LENGTH_SHORT).show();
        invalidateOptionsMenu();
    }

    @Override
    public void unCollectFail() {
        mCurrArticle.setCollect(true);
        invalidateOptionsMenu();
        Snackbar.make(mWebToolbar, R.string.uncollect_fail, Snackbar.LENGTH_SHORT).show();
    }

    private class CusWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }
    }

    private class CusWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            mWebToolbar.setTitle(title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress >= 99) {
                mWebProgress.setVisibility(View.GONE);
            } else {
                mWebProgress.setVisibility(View.VISIBLE);
            }
            mWebProgress.setProgress(newProgress);
        }
    }
}
