package com.example.webviewbrowser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.webviewbrowser.databinding.ItemLinkBinding

class LinksAdapter(
    private val links: List<LinkItem>,
    private val onLinkClick: (LinkItem) -> Unit
) : RecyclerView.Adapter<LinksAdapter.LinkViewHolder>() {

    class LinkViewHolder(val binding: ItemLinkBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        val binding = ItemLinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LinkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        val link = links[position]
        holder.binding.linkTitle.text = link.title
        holder.binding.linkUrl.text = link.url
        holder.itemView.setOnClickListener { onLinkClick(link) }
    }

    override fun getItemCount(): Int = links.size
}
