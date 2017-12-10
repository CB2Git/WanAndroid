package com.wanandroid.business.webview;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.wanandroid.R;
import com.wanandroid.utils.SharesUtils;

/**
 * 一个显示WebView的Activity
 *
 * TODO：点击网页图片 白屏问题！！！
 */
public class WebActivity extends AppCompatActivity {

    private static final String TAG = "WebActivity";

    private Toolbar mWebToolbar;

    private ViewGroup mWebViewContainer;

    private WebView mWebView;

    private ProgressBar mWebProgress;

    private static final String KEY_URL = "url";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initView();
        mWebView.loadUrl(getUrl());
    }

    public static Intent newInstance(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_URL, url);
        intent.putExtras(bundle);
        return intent;
    }

    private void initView() {

        mWebToolbar = (Toolbar) findViewById(R.id.web_title);
        setSupportActionBar(mWebToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mWebProgress = (ProgressBar) findViewById(R.id.web_progress);

        mWebViewContainer = (ViewGroup) findViewById(R.id.base_web_container);
        mWebView = new WebView(getApplicationContext());
        mWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mWebViewContainer.addView(mWebView);

        //设置WebView的一些属性
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
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

    private String getUrl() {
        String url = getIntent().getExtras().getString(KEY_URL);
        Log.i(TAG, "openUrl: " + url);
        return url;
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
                SharesUtils.share(this, getUrl());
                break;
            case R.id.action_copy_link:
                ClipboardManager cmd = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmd.setPrimaryClip(ClipData.newPlainText(getString(R.string.copy_link), mWebView.getUrl()));
                Snackbar.make(getWindow().getDecorView(), R.string.copy_link_success, Snackbar.LENGTH_SHORT).show();
                break;
            //TODO 收藏待服务端个人中心完成
            case R.id.action_collect:
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
            mWebView.resumeTimers();
        }
    }

    @Override
    protected void onPause() {
        if (mWebView != null) {
            mWebView.onPause();
            mWebView.pauseTimers();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mWebViewContainer != null) {
            mWebViewContainer.removeView(mWebView);
            mWebView.removeAllViews();
            mWebView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
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
