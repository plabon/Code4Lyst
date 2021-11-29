package com.jukti.code4lyst.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jukti.code4lyst.databinding.ImageItemBinding
import com.jukti.code4lyst.domain.model.ImageDomainModel
import com.jukti.code4lyst.utilities.loadFromUrl
import javax.inject.Inject
import kotlin.properties.Delegates

class ImageAdapter @Inject constructor(): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    internal var collection: MutableList<ImageDomainModel> by Delegates.observable(mutableListOf()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(collection[position])
    }

    override fun getItemCount(): Int {
        return collection.size
    }

    class ImageViewHolder(private val binding: ImageItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: ImageDomainModel) {
            image.url?.let { binding.image.loadFromUrl(it) }
        }
    }


}