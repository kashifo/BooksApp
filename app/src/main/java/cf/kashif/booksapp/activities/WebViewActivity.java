package cf.kashif.booksapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;


public class WebViewActivity extends AppCompatActivity {

    final String TAG = getClass().getSimpleName();
    WebView webView;

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
