package com.example.simpleviralgamestask

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simpleviralgamestask.databinding.ActivityMyRecentlyGeneratedDogsScreenBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyRecentlyGeneratedDogsScreen : AppCompatActivity() {
    private lateinit var binding: ActivityMyRecentlyGeneratedDogsScreenBinding

    private val generatedDogsAdapter by lazy { GeneratedDogsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyRecentlyGeneratedDogsScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.my_recently_generated_dogs)
        setupRecyclerView()
        binding.btnClearDogs.setOnClickListener {
            clearDogs()
        }
    }

    private fun setupRecyclerView() {
        binding.generatedDogsList.apply {
            layoutManager = LinearLayoutManager(
                this@MyRecentlyGeneratedDogsScreen, LinearLayoutManager.HORIZONTAL, false
            )
            adapter = generatedDogsAdapter
        }

        loadCachedDogImages()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadCachedDogImages() {
        lifecycleScope.launch {
            val cachedDogImages = DogImageCache.getAllCachedDogImages()
            withContext(Dispatchers.Main) {
                generatedDogsAdapter.setDogImages(cachedDogImages)
                generatedDogsAdapter.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearDogs() {
        lifecycleScope.launch {
            DogImageCache.clearCache()
            generatedDogsAdapter.clearDogImages()
            generatedDogsAdapter.notifyDataSetChanged()
        }
    }
}