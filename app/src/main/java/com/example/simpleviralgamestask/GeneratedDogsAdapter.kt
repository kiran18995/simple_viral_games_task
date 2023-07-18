package com.example.simpleviralgamestask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GeneratedDogsAdapter : RecyclerView.Adapter<GeneratedDogsAdapter.GeneratedDogsViewHolder>() {

    private val dogImagesList = mutableListOf<DogImageResponse>()

    fun setDogImages(dogImages: List<DogImageResponse>) {
        dogImagesList.clear()
        dogImagesList.addAll(dogImages)
    }

    fun clearDogImages() {
        dogImagesList.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeneratedDogsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_dog_poster, parent, false)
        return GeneratedDogsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GeneratedDogsViewHolder, position: Int) {
        val dogImage = dogImagesList[position]
        holder.bind(dogImage)
    }

    override fun getItemCount(): Int = dogImagesList.size

    inner class GeneratedDogsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivDogImage: ImageView = itemView.findViewById(R.id.dog_poster)

        fun bind(dogImage: DogImageResponse) {
            Glide.with(itemView).load(dogImage.message).into(ivDogImage)
        }
    }
}