package cf.kashif.booksapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

import cf.kashif.booksapp.R;


public class WebViewActivity extends AppCompatActivity {

    final String TAG = getClass().getSimpleName();
    WebView webView;

    //Even if try to show book on webview, it is redirected to browser automatically because of Google policies

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        String url = getIntent().getExtras().getString("url");
        Log.d(TAG, "url=" + url);

        if (url != null && !url.isEmpty()) {
            webView.loadUrl(url);
        }

    }//onCreate


}
