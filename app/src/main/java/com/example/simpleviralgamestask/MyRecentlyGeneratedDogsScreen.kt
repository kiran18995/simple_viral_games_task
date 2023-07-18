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
    private lateinit var dogImageCache: DogImageCache

    private val generatedDogsAdapter by lazy { GeneratedDogsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyRecentlyGeneratedDogsScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.my_recently_generated_dogs)

        dogImageCache = DogImageCache(application)

        setupRecyclerView()

        binding.btnClearDogs.setOnClickListener {
            clearDogs()
        }
    }

    private fun setupRecyclerView() {
        binding.rvGeneratedDogs.apply {
            layoutManager = LinearLayoutManager(
                this@MyRecentlyGeneratedDogsScreen,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = generatedDogsAdapter
        }

        loadCachedDogImages()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadCachedDogImages() {
        lifecycleScope.launch {
            val cachedDogImages = getAllCachedDogImages()
            generatedDogsAdapter.setDogImages(cachedDogImages)
            generatedDogsAdapter.notifyDataSetChanged()
        }
    }

    private suspend fun getAllCachedDogImages(): List<DogImageResponse> =
        withContext(Dispatchers.IO) {
            return@withContext dogImageCache.getAllCachedDogImages()
        }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearDogs() {
        lifecycleScope.launch {
            dogImageCache.clearCache()
            generatedDogsAdapter.clearDogImages()
            generatedDogsAdapter.notifyDataSetChanged()
        }
    }
}