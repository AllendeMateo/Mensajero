package com.example.messengerwrapper;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();

        // Configure WebView settings
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.clearCache(true);
        webView.clearHistory();
        webSettings.setDomStorageEnabled(true);
        webSettings.setSaveFormData(false);
        webSettings.setGeolocationEnabled(false);
        webSettings.setSupportMultipleWindows(false);
        webSettings.setUserAgentString("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");

        // Set up WebViewClient to inject JavaScript when the page finishes loading
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                view.postDelayed(() -> injectModificationScript(view), 3000); // Wait 3 seconds before injecting
            }
        });

        // Load the Messenger website
        webView.loadUrl("https://www.messenger.com");
    }

    // Method to inject JavaScript into the WebView
    private void injectModificationScript(WebView webView) {
    String js = "javascript:(function() {" +

            // Function to hide the sidebar
            "function hideSidebar() {" +
            "   var sidebar = document.querySelector('[role=\"navigation\"], .left-panel, .sidebar, .navigation');" +
            "   if (sidebar) {" +
            "       sidebar.style.display = 'none';" +
            "   }" +
            "}" +

            // Function to remove the outer frame and maximize the chat area
            "function removeOuterFrame() {" +
            "   var body = document.body;" +
            "   if (body) {" +
            "       body.style.backgroundColor = 'transparent';" +
            "       body.style.margin = '0';" +
            "       body.style.padding = '0';" +
            "   }" +

            "   var chatPanel = document.querySelector('[role=\"main\"], .chat-panel, .conversation, .messenger');" +
            "   if (chatPanel) {" +
            "       chatPanel.style.width = '100vw';" +
            "       chatPanel.style.height = '100vh';" +
            "       chatPanel.style.margin = '0';" +
            "       chatPanel.style.padding = '0';" +
            "       chatPanel.style.border = 'none';" +
            "       chatPanel.style.boxShadow = 'none';" +
            "   }" +
            "}" +

            // Function to set a fixed font size only once
            "function setFixedFontSize() {" +
            "   var chatTexts = document.querySelectorAll('p, span, div');" +
            "   chatTexts.forEach(function(element) {" +
            "       if (!element.hasAttribute('data-fixed-font')) {" +  // Check if size is already set
            "           element.style.fontSize = '16px';" +  // Set a reasonable fixed size
            "           element.setAttribute('data-fixed-font', 'true');" +  // Mark as processed
            "       }" +
            "   });" +
            "}" +

            // Run the functions immediately
            "hideSidebar();" +
            "removeOuterFrame();" +
            "setFixedFontSize();" +

            // Observe dynamic DOM changes to apply modifications only when necessary
            "var observer = new MutationObserver(function(mutations) {" +
            "   mutations.forEach(function(mutation) {" +
            "       if (mutation.type === 'childList') {" +
            "           hideSidebar();" +
            "           removeOuterFrame();" +
            "           setFixedFontSize();" +
            "       }" +
            "   });" +
            "});" +

            "observer.observe(document.body, { childList: true, subtree: true });" +
            "})()";

    // Evaluate the JavaScript in the WebView
    webView.evaluateJavascript(js, null);
}

    // Handle the back button press
    @Override
    public void onBackPressed() {
        WebView webView = findViewById(R.id.webview);
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}

