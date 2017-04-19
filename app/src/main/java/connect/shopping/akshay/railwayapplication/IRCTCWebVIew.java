package connect.shopping.akshay.railwayapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class IRCTCWebVIew extends AppCompatActivity {


    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irctcweb_view);

        webView = (WebView)findViewById(R.id.irctcWebView);

        webView.loadUrl("https://www.irctc.co.in/eticketing/loginHome.jsf");


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}


