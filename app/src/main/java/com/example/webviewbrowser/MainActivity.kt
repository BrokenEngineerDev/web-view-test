package com.example.webviewbrowser

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.webviewbrowser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure WebView
        setupWebView()

        // Set up button click listener
        binding.goButton.setOnClickListener {
            loadUrl()
        }

        // Set up links button click listener
        binding.linksButton.setOnClickListener {
            startActivity(Intent(this, LinksActivity::class.java))
        }

        // Handle keyboard "Go" action
        binding.urlEditText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO || 
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                loadUrl()
                true
            } else {
                false
            }
        }

        // Handle back button press using OnBackPressedCallback
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.webView.canGoBack()) {
                    binding.webView.goBack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })

        // Handle incoming intents (e.g., from external links)
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun setupWebView() {
        binding.webView.apply {
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
                    url?.let { binding.urlEditText.setText(it) }
                }
            }

            // WebChromeClient to handle JavaScript dialogs and progress
            webChromeClient = WebChromeClient()
        }
    }

    private fun loadUrl() {
        var url = binding.urlEditText.text.toString().trim()

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

        binding.webView.loadUrl(url)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.let {
            if (Intent.ACTION_VIEW == it.action) {
                val url = it.dataString
                url?.let { urlString ->
                    binding.urlEditText.setText(urlString)
                    binding.webView.loadUrl(urlString)
                }
            }
        }

        // Load default page if nothing else is loaded
        if (binding.webView.url == null) {
            val defaultUrl = getString(R.string.default_url)
            binding.urlEditText.setText(defaultUrl)
            binding.webView.loadUrl(defaultUrl)
        }
    }
}
