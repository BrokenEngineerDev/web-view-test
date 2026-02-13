package com.example.webviewbrowser

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var urlEditText: EditText
    private lateinit var goButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        webView = findViewById(R.id.webView)
        urlEditText = findViewById(R.id.urlEditText)
        goButton = findViewById(R.id.goButton)

        // Configure WebView
        setupWebView()

        // Set up button click listener
        goButton.setOnClickListener {
            loadUrl()
        }

        // Handle keyboard "Go" action
        urlEditText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO || 
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                loadUrl()
                true
            } else {
                false
            }
        }

        // Handle incoming intents (e.g., from external links)
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun setupWebView() {
        webView.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                builtInZoomControls = true
                displayZoomControls = false
                setSupportZoom(true)
            }

            // WebViewClient to handle page navigation
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    // Update URL in EditText when page finishes loading
                    url?.let { urlEditText.setText(it) }
                }
            }

            // WebChromeClient to handle JavaScript dialogs and progress
            webChromeClient = WebChromeClient()
        }
    }

    private fun loadUrl() {
        var url = urlEditText.text.toString().trim()

        if (url.isEmpty()) {
            Toast.makeText(this, "Please enter a URL", Toast.LENGTH_SHORT).show()
            return
        }

        // Add http:// if no protocol specified
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            // Check if it looks like a domain
            if (url.contains(".") && !url.contains(" ")) {
                url = "https://$url"
            } else {
                // Treat as search query
                url = "https://www.google.com/search?q=${url.replace(" ", "+")}"
            }
        }

        webView.loadUrl(url)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.let {
            if (Intent.ACTION_VIEW == it.action) {
                val url = it.dataString
                url?.let { urlString ->
                    urlEditText.setText(urlString)
                    webView.loadUrl(urlString)
                }
            }
        }

        // Load default page if nothing else is loaded
        if (webView.url == null) {
            val defaultUrl = getString(R.string.default_url)
            urlEditText.setText(defaultUrl)
            webView.loadUrl(defaultUrl)
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
