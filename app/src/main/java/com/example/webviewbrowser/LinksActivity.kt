package com.example.webviewbrowser

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.webviewbrowser.databinding.ActivityLinksBinding
import org.json.JSONArray

class LinksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLinksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLinksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.saved_links_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val links = loadLinksFromRaw()
        binding.linksRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.linksRecyclerView.adapter = LinksAdapter(links) { link ->
            openInWebView(link.url)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun loadLinksFromRaw(): List<LinkItem> {
        return try {
            val inputStream = resources.openRawResource(R.raw.links)
            val json = inputStream.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(json)
            val links = mutableListOf<LinkItem>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                links.add(LinkItem(obj.getString("title"), obj.getString("url")))
            }
            links
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error_loading_links), Toast.LENGTH_SHORT).show()
            emptyList()
        }
    }

    private fun openInWebView(url: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            data = android.net.Uri.parse(url)
        }
        startActivity(intent)
    }
}
