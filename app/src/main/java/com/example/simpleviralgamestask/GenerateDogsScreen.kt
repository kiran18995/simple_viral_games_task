package com.example.simpleviralgamestask

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.simpleviralgamestask.databinding.ActivityGenerateDogsScreenBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GenerateDogsScreen : AppCompatActivity() {
    private lateinit var dogImageCache: DogImageCache
    private lateinit var binding: ActivityGenerateDogsScreenBinding
    private val dogApiService: DogApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(DogApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenerateDogsScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dogImageCache = DogImageCache(application)

        binding.btnGenerate.setOnClickListener {
            generateDogImage()
        }
    }

    private fun generateDogImage() {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val dogImage = getRandomDogImage()
                if (dogImage != null) {
                    // Display the dog image using Glide
                    Glide.with(this@GenerateDogsScreen)
                        .load(dogImage.message)
                        .into(binding.ivDogImage)

                    // Cache the dog image
                    cacheDogImage(dogImage)
                } else {
                    Toast.makeText(this@GenerateDogsScreen, "Failed to fetch the dog image.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@GenerateDogsScreen, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun getRandomDogImage(): DogImageResponse? = withContext(Dispatchers.IO) {
        try {
            val response = dogApiService.getRandomDogImage()
            if (response.status == "success") {
                return@withContext response
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext null
    }

    private suspend fun cacheDogImage(dogImage: DogImageResponse) = withContext(Dispatchers.IO) {
        // Cache the dog image in the LRU cache
        dogImageCache.cacheDogImage(dogImage)
    }
}