package com.example.demo.swiperrefreshlayoutwebview;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private View view;
    public WebView webview;
    private SwipeRefreshLayout swipeLayout;
    private LinearLayout containerLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        containerLL = (LinearLayout) findViewById(R.id.containerLL);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setRefreshing(false);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //重新刷新页面
                webview.loadUrl(webview.getUrl());
            }
        });
        swipeLayout.setColorScheme(R.color.holo_blue_bright,
                R.color.holo_green_light, R.color.holo_orange_light,
                R.color.holo_red_light);

        webview = (WebView) findViewById(R.id.webview);

        webview.loadUrl("http://www.baidu.com");
        //添加javaScript支持
        webview.getSettings().setJavaScriptEnabled(true);
        //取消滚动条
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        //触摸焦点起作用
        webview.requestFocus();
        //点击链接继续在当前browser中响应
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            /**
             * Notify the host application that an SSL error occurred while loading a
             * resource. The host application must call either handler.cancel() or
             * handler.proceed(). Note that the decision may be retained for use in
             * response to future SSL errors. The default behavior is to cancel the
             * load.
             *
             * @param view    The WebView that is initiating the callback.
             * @param handler An SslErrorHandler object that will handle the user's
             *                response.
             * @param error   The SSL error object.
             */
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                if (!swipeLayout.isRefreshing()) {
                    view.setVisibility(View.GONE);
                }
            }

            /**
             * Notify the host application that a page has started loading. This method
             * is called once for each main frame load so a page with iframes or
             * framesets will call onPageStarted one time for the main frame. This also
             * means that onPageStarted will not be called when the contents of an
             * embedded frame changes, i.e. clicking a link whose target is an iframe,
             * it will also not be called for fragment navigations (navigations to
             * #fragment_id).
             *
             * @param view    The WebView that is initiating the callback.
             * @param url     The url to be loaded.
             * @param favicon The favicon for this page if it already exists in the
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!swipeLayout.isRefreshing()) {
                    view.setVisibility(View.GONE);
                }
            }

            /**
             * Notify the host application that a page has finished loading. This method
             * is called only for main frame. When onPageFinished() is called, the
             * rendering picture may not be updated yet. To get the notification for the
             * new Picture, use {@link WebView.PictureListener#onNewPicture}.
             *
             * @param view The WebView that is initiating the callback.
             * @param url  The url of the page.
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.setVisibility(View.VISIBLE);
            }
        });
        //设置进度条
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    //隐藏进度条
                    swipeLayout.setRefreshing(false);
                } else {
                    if (!swipeLayout.isRefreshing())
                        swipeLayout.setRefreshing(true);
                }

                super.onProgressChanged(view, newProgress);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        int random = (new Random()).nextInt(10);
        if ( random < 5){
            swipeLayout.setVisibility(View.VISIBLE);
            containerLL.setVisibility(View.GONE);
        } else {
            containerLL.setVisibility(View.VISIBLE);
            swipeLayout.setVisibility(View.GONE);
        }
    }
}
